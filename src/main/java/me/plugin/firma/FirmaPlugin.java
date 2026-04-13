package me.plugin.firma;

import me.plugin.firma.company.CompanyManager;
import me.plugin.firma.data.DataManager;
import me.plugin.firma.listener.GUIListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class FirmaPlugin extends JavaPlugin {

    private static Economy econ;

    private CompanyManager companyManager;
    private DataManager dataManager;

    // ================= ENABLE =================
    @Override
    public void onEnable() {

        saveDefaultConfig();

        setupEconomy();

        companyManager = new CompanyManager();
        dataManager = new DataManager(this);

        // LOAD DATA
        companyManager.setCompanies(dataManager.load());

        // LISTENER
        getServer().getPluginManager().registerEvents(
                new GUIListener(companyManager, this),
                this
        );

        // COMMAND
        getCommand("firma").setExecutor(this);

        // AUTO SAVE každých 60s
        getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            dataManager.saveAsync(companyManager.getCompanies());
        }, 1200, 1200);

        getLogger().info("BizCore enabled");
    }

    // ================= COMMAND =================
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player p)) return true;

        var c = companyManager.getCompany(p);

        // OPEN GUI
        if (args.length == 0) {
            me.plugin.firma.gui.MainGUI.open(p, c);
            return true;
        }

        // CREATE COMPANY
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
        }

        // WORK
        if (args[0].equalsIgnoreCase("work")) {

            if (c == null) return true;

            double reward = getConfig().getDouble("jobs.reward");

            c.balance += reward;
            c.addXP(25, getConfig().getInt("levels.xp-per-level"));

            p.sendMessage("§aOdpracoval jsi práci!");
        }

        return true;
    }

    // ================= ECONOMY =================
    private void setupEconomy() {

        RegisteredServiceProvider<Economy> rsp =
                getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp != null) econ = rsp.getProvider();
    }

    public Economy getEconomy() {
        return econ;
    }
}
