package me.plugin.firma;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;

import java.util.*;

public class FirmaPlugin extends JavaPlugin implements CommandExecutor {

    private static Economy econ = null;

    // Firmy
    private final Map<String, UUID> owners = new HashMap<>();
    private final Map<UUID, String> playerCompany = new HashMap<>();
    private final Map<String, Double> companyMoney = new HashMap<>();

    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            getLogger().severe("Vault nenalezen! Plugin se vypíná.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getCommand("firma").setExecutor(this);
        getLogger().info("FirmaPlugin zapnut!");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp =
                getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null) {
            return false;
        }

        econ = rsp.getProvider();
        return econ != null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Pouze hráč!");
            return true;
        }

        Player p = (Player) sender;

        if (args.length == 0) {
            p.sendMessage("§e/firma create <název>");
            p.sendMessage("§e/firma info");
            p.sendMessage("§e/firma deposit <částka>");
            p.sendMessage("§e/firma withdraw <částka>");
            return true;
        }

        // CREATE
        if (args[0].equalsIgnoreCase("create")) {
            if (args.length < 2) {
                p.sendMessage("§cPoužití: /firma create <název>");
                return true;
            }

            if (playerCompany.containsKey(p.getUniqueId())) {
                p.sendMessage("§cUž máš firmu!");
                return true;
            }

            String name = args[1];

            if (owners.containsKey(name)) {
                p.sendMessage("§cFirma už existuje!");
                return true;
            }

            double price = 1000;

            if (!econ.has(p, price)) {
                p.sendMessage("§cNemáš dost peněz!");
                return true;
            }

            econ.withdrawPlayer(p, price);

            owners.put(name, p.getUniqueId());
            playerCompany.put(p.getUniqueId(), name);
            companyMoney.put(name, 0.0);

            p.sendMessage("§aFirma vytvořena: " + name);
            return true;
        }

        // INFO
        if (args[0].equalsIgnoreCase("info")) {
            if (!playerCompany.containsKey(p.getUniqueId())) {
                p.sendMessage("§cNemáš firmu!");
                return true;
            }

            String name = playerCompany.get(p.getUniqueId());
            double money = companyMoney.getOrDefault(name, 0.0);

            p.sendMessage("§6=== Firma ===");
            p.sendMessage("§eNázev: " + name);
            p.sendMessage("§ePeníze: " + money);

            return true;
        }

        // DEPOSIT
        if (args[0].equalsIgnoreCase("deposit")) {
            if (!playerCompany.containsKey(p.getUniqueId())) {
                p.sendMessage("§cNemáš firmu!");
                return true;
            }

            if (args.length < 2) {
                p.sendMessage("§cPoužití: /firma deposit <částka>");
                return true;
            }

            double amount;

            try {
                amount = Double.parseDouble(args[1]);
            } catch (Exception e) {
                p.sendMessage("§cŠpatné číslo!");
                return true;
            }

            if (!econ.has(p, amount)) {
                p.sendMessage("§cNemáš peníze!");
                return true;
            }

            econ.withdrawPlayer(p, amount);

            String name = playerCompany.get(p.getUniqueId());
            companyMoney.put(name, companyMoney.getOrDefault(name, 0.0) + amount);

            p.sendMessage("§aVložil jsi " + amount + " do firmy!");
            return true;
        }

        // WITHDRAW
        if (args[0].equalsIgnoreCase("withdraw")) {
            if (!playerCompany.containsKey(p.getUniqueId())) {
                p.sendMessage("§cNemáš firmu!");
                return true;
            }

            String name = playerCompany.get(p.getUniqueId());

            if (!owners.get(name).equals(p.getUniqueId())) {
                p.sendMessage("§cNejsi majitel!");
                return true;
            }

            if (args.length < 2) {
                p.sendMessage("§cPoužití: /firma withdraw <částka>");
                return true;
            }

            double amount;

            try {
                amount = Double.parseDouble(args[1]);
            } catch (Exception e) {
                p.sendMessage("§cŠpatné číslo!");
                return true;
            }

            double balance = companyMoney.getOrDefault(name, 0.0);

            if (balance < amount) {
                p.sendMessage("§cFirma nemá dost peněz!");
                return true;
            }

            companyMoney.put(name, balance - amount);
            econ.depositPlayer(p, amount);

            p.sendMessage("§aVybral jsi " + amount + " z firmy!");
            return true;
        }

        return true;
    }
}
