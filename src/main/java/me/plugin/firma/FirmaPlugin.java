package me.plugin.firma;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;

import java.util.*;

public class FirmaPlugin extends JavaPlugin implements CommandExecutor {

    private static Economy econ = null;

    private Map<String, Company> companies = new HashMap<>();

    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            getLogger().severe("Vault nebyl nalezen!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getCommand("firma").setExecutor(this);
        getLogger().info("Firma plugin zapnut!");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp =
                getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null) return false;

        econ = rsp.getProvider();
        return econ != null;
    }

    // ===== COMMAND SYSTEM =====
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Pouze hráč!");
            return true;
        }

        Player p = (Player) sender;

        if (args.length == 0) {
            p.sendMessage("§cPoužití: /firma create <název>");
            return true;
        }

        // /firma create <název>
        if (args[0].equalsIgnoreCase("create")) {

            if (args.length < 2) {
                p.sendMessage("§cZadej název firmy!");
                return true;
            }

            String name = args[1].toLowerCase();

            if (companies.containsKey(name)) {
                p.sendMessage("§cTato firma už existuje!");
                return true;
            }

            double price = 1000;

            if (!econ.has(p, price)) {
                p.sendMessage("§cNemáš dost peněz!");
                return true;
            }

            econ.withdrawPlayer(p, price);

            Company company = new Company(name, p.getUniqueId());
            companies.put(name, company);

            p.sendMessage("§aFirma vytvořena: " + name);
            return true;
        }

        return true;
    }

    // ===== COMPANY CLASS =====
    public static class Company {

        private String name;
        private UUID owner;
        private Set<UUID> employees = new HashSet<>();
        private double balance = 0;

        public Company(String name, UUID owner) {
            this.name = name;
            this.owner = owner;
        }

        public String getName() {
            return name;
        }

        public UUID getOwner() {
            return owner;
        }

        public Set<UUID> getEmployees() {
            return employees;
        }

        public double getBalance() {
            return balance;
        }

        public void addEmployee(UUID uuid) {
            employees.add(uuid);
        }

        public void removeEmployee(UUID uuid) {
            employees.remove(uuid);
        }

        public void addMoney(double amount) {
            balance += amount;
        }

        public void removeMoney(double amount) {
            balance -= amount;
        }
    }
}
