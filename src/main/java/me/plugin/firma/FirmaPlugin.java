package me.plugin.firma;

import me.plugin.firma.company.CompanyManager;
import me.plugin.firma.data.DataManager;
import me.plugin.firma.listener.GUIListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class FirmaPlugin extends JavaPlugin implements CommandExecutor {

    private CompanyManager companyManager;
    private DataManager dataManager;
    private Economy econ;

    @Override
    public void onEnable() {

        setupEconomy();

        companyManager = new CompanyManager();
        dataManager = new DataManager(this);

        companyManager.setCompanies(dataManager.load());

        getServer().getPluginManager().registerEvents(
                new GUIListener(companyManager), this
        );

        if (getCommand("firma") != null)
            getCommand("firma").setExecutor(this);

        getLogger().info("FirmaPlugin enabled");
    }

    @Override
    public void onDisable() {
        dataManager.save(companyManager.getCompanies());
    }

    private void setupEconomy() {
        RegisteredServiceProvider<Economy> rsp =
                getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp != null) econ = rsp.getProvider();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player p)) return true;

        if (args.length == 0) {
            p.sendMessage("§ePoužití: /firma create <název>");
            return true;
        }

        if (args[0].equalsIgnoreCase("create")) {

            if (companyManager.getCompany(p) != null) {
                p.sendMessage("§cUž máš firmu!");
                return true;
            }

            if (args.length < 2) {
                p.sendMessage("§cPoužití: /firma create <název>");
                return true;
            }

            companyManager.createCompany(args[1], p);
            p.sendMessage("§aFirma vytvořena!");
        }

        return true;
    }
}
