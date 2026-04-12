package me.plugin.firma;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;

import java.util.*;

public class FirmaPlugin extends JavaPlugin implements CommandExecutor {

    private static Economy econ = null;

    private Map<String, Company> companies = new HashMap<>();
    private Map<UUID, String> invites = new HashMap<>();

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

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Pouze hráč!");
            return true;
        }

        Player p = (Player) sender;

        if (args.length == 0) {
            p.sendMessage("§cPoužití: /firma <create|invite|join|balance|info>");
            return true;
        }

        // ===== CREATE =====
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

        // ===== INVITE =====
        if (args[0].equalsIgnoreCase("invite")) {

            if (args.length < 2) {
                p.sendMessage("§cPoužití: /firma invite <hráč>");
                return true;
            }

            Company company = getPlayerCompany(p);

            if (company == null) {
                p.sendMessage("§cNejsi ve firmě!");
                return true;
            }

            if (!company.getOwner().equals(p.getUniqueId())) {
                p.sendMessage("§cPouze majitel může zvát!");
                return true;
            }

            Player target = getServer().getPlayer(args[1]);

            if (target == null) {
                p.sendMessage("§cHráč není online!");
                return true;
            }

            invites.put(target.getUniqueId(), company.getName());

            target.sendMessage("§aByl jsi pozván do firmy: " + company.getName());
            target.sendMessage("§7Použij /firma join " + company.getName());

            p.sendMessage("§aPozvánka odeslána.");
            return true;
        }

        // ===== JOIN =====
        if (args[0].equalsIgnoreCase("join")) {

            if (args.length < 2) {
                p.sendMessage("§cPoužití: /firma join <firma>");
                return true;
            }

            String name = args[1].toLowerCase();

            if (!companies.containsKey(name)) {
                p.sendMessage("§cFirma neexistuje!");
                return true;
            }

            if (!invites.containsKey(p.getUniqueId()) || !invites.get(p.getUniqueId()).equals(name)) {
                p.sendMessage("§cNemáš pozvánku!");
                return true;
            }

            Company company = companies.get(name);
            company.addEmployee(p.getUniqueId());

            invites.remove(p.getUniqueId());

            p.sendMessage("§aPřipojil ses do firmy: " + name);
            return true;
        }

        // ===== BALANCE =====
        if (args[0].equalsIgnoreCase("balance")) {

            Company company = getPlayerCompany(p);

            if (company == null) {
                p.sendMessage("§cNejsi ve firmě!");
                return true;
            }

            p.sendMessage("§aFirma má: §e" + company.getBalance() + "$");
            return true;
        }

        // ===== INFO =====
        if (args[0].equalsIgnoreCase("info")) {

            Company company = getPlayerCompany(p);

            if (company == null) {
                p.sendMessage("§cNejsi ve firmě!");
                return true;
            }

            p.sendMessage("§6=== Firma ===");
            p.sendMessage("§eNázev: §f" + company.getName());
            p.sendMessage("§eZaměstnanci: §f" + company.getEmployees().size());
            p.sendMessage("§eBalance: §f" + company.getBalance() + "$");

            return true;
        }

        return true;
    }

    private Company getPlayerCompany(Player p) {
        for (Company c : companies.values()) {
            if (c.getOwner().equals(p.getUniqueId()) || c.getEmployees().contains(p.getUniqueId())) {
                return c;
            }
        }
        return null;
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
