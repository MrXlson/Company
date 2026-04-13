package me.plugin.firma.manager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class FirmaManager {

    private final JavaPlugin plugin;

    public FirmaManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    // ===============================
    // 🔧 BASIC
    // ===============================

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public boolean hasCompany(UUID uuid) {
        return plugin.getConfig().contains("players." + uuid + ".firma");
    }

    public String getCompany(UUID uuid) {
        return plugin.getConfig().getString("players." + uuid + ".firma");
    }

    // ===============================
    // 🏢 CREATE
    // ===============================

    public void createCompany(UUID owner, String name) {
        FileConfiguration c = plugin.getConfig();

        c.set("firma." + name + ".owner", owner.toString());
        c.set("firma." + name + ".members", new ArrayList<>(Collections.singletonList(owner.toString())));
        c.set("firma." + name + ".balance", c.getDouble("economy.start-balance"));
        c.set("firma." + name + ".level", 1);
        c.set("firma." + name + ".xp", 0);

        c.set("firma." + name + ".multiplier", 1.0);
        c.set("firma." + name + ".limit", c.getInt("settings.max-members"));

        c.set("players." + owner + ".firma", name);

        plugin.saveConfig();
    }

    // ===============================
    // 👥 MEMBERS
    // ===============================

    public List<String> getMembers(String firma) {
        return plugin.getConfig().getStringList("firma." + firma + ".members");
    }

    public void addMember(String firma, UUID uuid) {
        List<String> list = getMembers(firma);
        list.add(uuid.toString());

        plugin.getConfig().set("firma." + firma + ".members", list);
        plugin.getConfig().set("players." + uuid + ".firma", firma);

        plugin.saveConfig();
    }

    public void removeMember(String firma, UUID uuid) {
        List<String> list = getMembers(firma);
        list.remove(uuid.toString());

        plugin.getConfig().set("firma." + firma + ".members", list);
        plugin.getConfig().set("players." + uuid, null);

        plugin.saveConfig();
    }

    // ===============================
    // 💰 ECONOMY
    // ===============================

    public double getBalance(String firma) {
        return plugin.getConfig().getDouble("firma." + firma + ".balance");
    }

    public void addBalance(String firma, double amount) {
        plugin.getConfig().set("firma." + firma + ".balance", getBalance(firma) + amount);
        plugin.saveConfig();
    }

    public void removeBalance(String firma, double amount) {
        plugin.getConfig().set("firma." + firma + ".balance", getBalance(firma) - amount);
        plugin.saveConfig();
    }

    // ===============================
    // ⭐ LEVEL & XP
    // ===============================

    public int getLevel(String firma) {
        return plugin.getConfig().getInt("firma." + firma + ".level");
    }

    public int getXP(String firma) {
        return plugin.getConfig().getInt("firma." + firma + ".xp");
    }

    public void addXP(String firma, int amount) {
        int xp = getXP(firma) + amount;

        int level = getLevel(firma);
        int needed = plugin.getConfig().getInt("levels." + level + ".xp");

        if (xp >= needed) {
            xp = 0;
            level++;

            plugin.getConfig().set("firma." + firma + ".level", level);

            // reward
            double reward = plugin.getConfig().getDouble("levels." + level + ".reward-money");
            addBalance(firma, reward);
        }

        plugin.getConfig().set("firma." + firma + ".xp", xp);
        plugin.saveConfig();
    }

    // ===============================
    // 🚀 UPGRADES
    // ===============================

    public double getMultiplier(String firma) {
        return plugin.getConfig().getDouble("firma." + firma + ".multiplier", 1.0);
    }

    public void upgradeMultiplier(String firma) {
        double current = getMultiplier(firma);
        double increase = plugin.getConfig().getDouble("upgrades.multiplier.increase");

        plugin.getConfig().set("firma." + firma + ".multiplier", current + increase);
        plugin.saveConfig();
    }

    public int getLimit(String firma) {
        return plugin.getConfig().getInt("firma." + firma + ".limit");
    }

    public void setLimit(String firma, int value) {
        plugin.getConfig().set("firma." + firma + ".limit", value);
        plugin.saveConfig();
    }
                               }
