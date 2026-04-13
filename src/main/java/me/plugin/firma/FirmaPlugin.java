package me.plugin.firma;

import me.plugin.firma.company.*;
import me.plugin.firma.data.DataManager;
import me.plugin.firma.gui.MainGUI;
import me.plugin.firma.listener.GUIListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class FirmaPlugin extends JavaPlugin implements CommandExecutor {

    private static Economy econ;
    private CompanyManager manager;
    private DataManager data;

    private final Map<UUID, Long> cooldown = new HashMap<>();

    @Override
    public void onEnable() {

        saveDefaultConfig();
        setupEconomy();

        manager = new CompanyManager();
        data = new DataManager(this);

        manager.setCompanies(data.load());

        getCommand("firma").setExecutor(this);

        getServer().getPluginManager().registerEvents(
                new GUIListener(manager, this),
                this
        );

        getServer().getScheduler().runTaskTimerAsynchronously(this, () ->
                data.save(manager.getCompanies()), 1200, 1200);

        getLogger().info("BizCore v8 ENABLED");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player p)) return true;

        Company c = manager.getCompany(p);

        if (args.length == 0) {
            if (c == null) {
                p.sendMessage("§cNevlastníte firmu, prvně si ji založte §e/firma create");
                return true;
            }
            MainGUI.open(p, c);
            return true;
        }

        if (args[0].equalsIgnoreCase("create")) {

            if (c != null) return true;

            double cost = getConfig().getDouble("economy.create-cost");

            if (econ.getBalance(p) < cost) {
                p.sendMessage("§cNemáš dost peněz! §7- Cena: §e" + cost);
                return true;
            }

            econ.withdrawPlayer(p, cost);

            manager.createCompany(args[1], p);
            p.sendMessage("§aFirma vytvořena!");
        }

        if (args[0].equalsIgnoreCase("work")) {

            if (c == null) return true;

            long cd = getConfig().getLong("jobs.cooldown");

            if (cooldown.containsKey(p.getUniqueId())) {
                if (System.currentTimeMillis() - cooldown.get(p.getUniqueId()) < cd) {
                    p.sendMessage("§cPočkej!");
                    return true;
                }
            }

            cooldown.put(p.getUniqueId(), System.currentTimeMillis());

            double reward = getConfig().getDouble("jobs.reward");
            double bonus = c.level * getConfig().getDouble("levels.bonus-per-level");

            c.balance += reward + bonus;
            c.addXP(25, getConfig().getInt("levels.xp-per-level"));

            p.sendMessage("§aPráce hotová!");
        }

        if (args[0].equalsIgnoreCase("invite")) {
            Player t = getServer().getPlayer(args[1]);
            if (t != null) manager.invite(p, t);
        }

        if (args[0].equalsIgnoreCase("accept")) {
            manager.accept(p);
        }

        return true;
    }

    private void setupEconomy() {
        RegisteredServiceProvider<Economy> rsp =
                getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp != null) econ = rsp.getProvider();
    }

    public Economy getEconomy() {
        return econ;
    }
}
