package me.plugin.firma;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class FirmaPlugin extends JavaPlugin implements CommandExecutor, Listener {

    private static Economy econ = null;

    private final Map<String, Company> companies = new HashMap<>();
    private final Map<UUID, String> invites = new HashMap<>();

    private double createPrice;
    private int salaryInterval;
    private double salaryAmount;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        createPrice = getConfig().getDouble("company-create-price");
        salaryInterval = getConfig().getInt("salary.interval-seconds");
        salaryAmount = getConfig().getDouble("salary.amount-per-player");

        if (!setupEconomy()) {
            getLogger().severe("Vault nenalezen!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        startSalaryTask();

        // 🔥 FIX: kontrola commandu
        if (getCommand("firma") == null) {
            getLogger().severe("COMMAND /firma není registrovaný v plugin.yml!");
        } else {
            getCommand("firma").setExecutor(this);
            getLogger().info("Command /firma úspěšně napojen.");
        }

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    // ===== COMMAND HANDLER =====
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Pouze hráč může použít tento příkaz.");
            return true;
        }

        Player player = (Player) sender;

        // /firma
        if (args.length == 0) {
            player.sendMessage("§6/firma create <název>");
            player.sendMessage("§6/firma invite <hráč>");
            return true;
        }

        // /firma create
        if (args[0].equalsIgnoreCase("create")) {

            if (args.length < 2) {
                player.sendMessage("§cZadej název firmy!");
                return true;
            }

            String name = args[1].toLowerCase();

            if (companies.containsKey(name)) {
                player.sendMessage("§cFirma už existuje!");
                return true;
            }

            if (!econ.has(player, createPrice)) {
                player.sendMessage("§cNemáš dost peněz!");
                return true;
            }

            econ.withdrawPlayer(player, createPrice);

            Company company = new Company(name, player.getUniqueId());
            companies.put(name, company);

            player.sendMessage("§aFirma vytvořena: " + name);
            return true;
        }

        // /firma invite
        if (args[0].equalsIgnoreCase("invite")) {

            if (args.length < 2) {
                player.sendMessage("§cZadej hráče!");
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);

            if (target == null) {
                player.sendMessage("§cHráč není online!");
                return true;
            }

            String companyName = getPlayerCompany(player.getUniqueId());

            if (companyName == null) {
                player.sendMessage("§cNejsi ve firmě!");
                return true;
            }

            invites.put(target.getUniqueId(), companyName);

            target.sendMessage("§aByl jsi pozván do firmy: " + companyName);
            target.sendMessage("§7Použij /firma accept");

            player.sendMessage("§aPozvánka odeslána.");
            return true;
        }

        // /firma accept
        if (args[0].equalsIgnoreCase("accept")) {

            if (!invites.containsKey(player.getUniqueId())) {
                player.sendMessage("§cNemáš žádnou pozvánku!");
                return true;
            }

            String companyName = invites.remove(player.getUniqueId());
            Company company = companies.get(companyName);

            if (company == null) {
                player.sendMessage("§cFirma neexistuje.");
                return true;
            }

            company.members.put(player.getUniqueId(), "MEMBER");
            player.sendMessage("§aPřipojil ses do firmy: " + companyName);
            return true;
        }

        player.sendMessage("§cNeznámý příkaz.");
        return true;
    }

    // ===== HELPER =====
    private String getPlayerCompany(UUID uuid) {
        for (Company c : companies.values()) {
            if (c.members.containsKey(uuid)) {
                return c.name;
            }
        }
        return null;
    }

    // ===== SALARY TASK =====
    private void startSalaryTask() {
        new BukkitRunnable() {
            @Override
            public void run() {

                for (Company c : companies.values()) {

                    for (UUID uuid : c.members.keySet()) {

                        Player p = Bukkit.getPlayer(uuid);

                        if (p == null) continue;
                        if (c.balance < salaryAmount) continue;

                        econ.depositPlayer(p, salaryAmount);
                        c.balance -= salaryAmount;

                        c.addXP(5);
                    }
                }
            }
        }.runTaskTimer(this, 0, salaryInterval * 20L);
    }

    // ===== ECONOMY =====
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        var rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        econ = rsp.getProvider();
        return econ != null;
    }

    // ===== COMPANY =====
    static class Company {
        String name;
        Map<UUID, String> members = new HashMap<>();
        double balance = 0;

        int level = 1;
        int xp = 0;

        Company(String name, UUID owner) {
            this.name = name;
            if (owner != null)
                members.put(owner, "OWNER");
        }

        void addXP(int amount) {
            xp += amount;

            if (xp >= level * 100) {
                xp = 0;
                level++;
                balance += 500;
            }
        }
    }
            }
