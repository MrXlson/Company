package me.plugin.firma;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.*;

public class FirmaPlugin extends JavaPlugin implements CommandExecutor, Listener {

    private static Economy econ;

    private final Map<String, Company> companies = new HashMap<>();
    private final Map<UUID, String> invites = new HashMap<>();

    private File file;
    private FileConfiguration data;

    private double createPrice;
    private int salaryInterval;
    private double salaryAmount;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        createPrice = getConfig().getDouble("company-create-price", 1000);
        salaryInterval = getConfig().getInt("salary.interval-seconds", 60);
        salaryAmount = getConfig().getDouble("salary.amount-per-player", 100);

        setupFile();
        loadCompanies();

        if (!setupEconomy()) {
            getLogger().severe("Vault nenalezen!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        startSalaryTask();

        getCommand("firma").setExecutor(this);
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    // ===== ECONOMY =====
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        econ = getServer().getServicesManager().getRegistration(Economy.class).getProvider();
        return econ != null;
    }

    // ===== SALARY TASK =====
    private void startSalaryTask() {
        new BukkitRunnable() {
            @Override
            public void run() {

                for (Company c : new ArrayList<>(companies.values())) {

                    for (UUID uuid : c.members.keySet()) {

                        Player p = Bukkit.getPlayer(uuid);
                        if (p == null) continue;

                        if (c.balance < salaryAmount) continue;

                        econ.depositPlayer(p, salaryAmount);
                        c.balance -= salaryAmount;

                        c.addXP(5);
                    }
                }

                saveCompanies();
            }
        }.runTaskTimer(this, 0L, salaryInterval * 20L);
    }

    // ===== FILE SYSTEM (placeholder – musíš mít svoje implementace) =====
    private void setupFile() {
        file = new File(getDataFolder(), "companies.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            saveResource("companies.yml", false);
        }
        data = getConfig();
    }

    private void loadCompanies() {
        // TODO: tvoje load logika
    }

    private void saveCompanies() {
        // TODO: tvoje save logika
    }

    // ===== COMPANY MODEL =====
    static class Company {

        String name;
        Map<UUID, Role> members = new HashMap<>();
        double balance = 0;

        int level = 1;
        int xp = 0;

        Company(String name, UUID owner) {
            this.name = name;
            if (owner != null) {
                members.put(owner, Role.OWNER);
            }
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

    enum Role {
        OWNER,
        ADMIN,
        MEMBER
    }
}
