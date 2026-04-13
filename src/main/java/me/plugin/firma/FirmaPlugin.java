package me.plugin.firma;

import me.plugin.firma.company.CompanyManager;
import me.plugin.firma.data.DataManager;
import me.plugin.firma.listener.GUIListener;
import me.plugin.firma.gui.MainGUI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class FirmaPlugin extends JavaPlugin implements CommandExecutor {

    private static Economy econ;

    private CompanyManager companyManager;
    private DataManager dataManager;

    @Override
    public void onEnable() {

        saveDefaultConfig();
        setupEconomy();

        companyManager = new CompanyManager();
        dataManager = new DataManager(this);

        companyManager.setCompanies(dataManager.load());

        // ✅ COMMAND FIX
        if (getCommand("firma") != null) {
            getCommand("firma").setExecutor(this);
        }

        // ✅ LISTENER
        getServer().getPluginManager().registerEvents(
                new GUIListener(companyManager, this),
                this
        );

        // AUTO SAVE
        getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            dataManager.saveAsync(companyManager.getCompanies());
        }, 1200, 1200);

        getLogger().info("BizCore ENABLED");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        // 🔥 DEBUG (můžeš pak smazat)
        getLogger().info("COMMAND /firma TRIGGERED");

        if (!(sender instanceof Player p)) return true;

        var c = companyManager.getCompany(p);

        // GUI OPEN
        if (args.length == 0) {
            MainGUI.open(p, c);
            return true;
        }

        // CREATE
        if (args[0].equalsIgnoreCase("create")) {

            if (c != null) {
                p.sendMessage("§cUž máš firmu!");
                return true;
            }

            if (args.length < 2) {
                p.sendMessage("§cPoužití: /firma create <name>");
                return true;
            }

            double cost = getConfig().getDouble("economy.create-cost");

            if (econ.getBalance(p) < cost) {
                p.sendMessage("§cNemáš dost peněz!");
                return true;
            }

            econ.withdrawPlayer(p, cost);

            companyManager.createCompany(args[1], p);

            p.sendMessage("§aFirma vytvořena!");
            return true;
        }

        // WORK
        if (args[0].equalsIgnoreCase("work")) {

            if (c == null) {
                p.sendMessage("§cNemáš firmu!");
                return true;
            }

            double reward = getConfig().getDouble("jobs.reward");

            c.balance += reward;
            c.addXP(25, getConfig().getInt("levels.xp-per-level"));

            p.sendMessage("§aPráce dokončena!");
            return true;
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
