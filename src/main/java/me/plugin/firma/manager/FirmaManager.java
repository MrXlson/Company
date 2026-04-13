package me.plugin.firma.manager;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class FirmaManager {

    private final JavaPlugin plugin;

    public FirmaManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public boolean hasCompany(UUID uuid) {
        return plugin.getConfig().contains("players." + uuid + ".firma");
    }

    public String getCompany(UUID uuid) {
        return plugin.getConfig().getString("players." + uuid + ".firma");
    }

    public void createCompany(UUID uuid, String name) {
        plugin.getConfig().set("players." + uuid + ".firma", name);
        plugin.getConfig().set("firma." + name + ".owner", uuid.toString());
        plugin.getConfig().set("firma." + name + ".balance", 0);
        plugin.getConfig().set("firma." + name + ".level", 1);
        plugin.getConfig().set("firma." + name + ".xp", 0);
        plugin.getConfig().set("firma." + name + ".multiplier", 1.0);
        plugin.getConfig().set("firma." + name + ".limit", 10000);
        plugin.saveConfig();
    }

    public double getBalance(String firma) {
        return plugin.getConfig().getDouble("firma." + firma + ".balance", 0);
    }

    public void addBalance(String firma, double amount) {
        plugin.getConfig().set("firma." + firma + ".balance", getBalance(firma) + amount);
        plugin.saveConfig();
    }

    public void removeBalance(String firma, double amount) {
        plugin.getConfig().set("firma." + firma + ".balance", getBalance(firma) - amount);
        plugin.saveConfig();
    }

    public int getLevel(String firma) {
        return plugin.getConfig().getInt("firma." + firma + ".level", 1);
    }

    public void addXP(String firma, int amount) {
        int xp = plugin.getConfig().getInt("firma." + firma + ".xp", 0);
        int level = getLevel(firma);

        xp += amount;

        if (xp >= level * 100) {
            xp = 0;
            level++;
            plugin.getConfig().set("firma." + firma + ".level", level);
        }

        plugin.getConfig().set("firma." + firma + ".xp", xp);
        plugin.saveConfig();
    }

    public double getMultiplier(String firma) {
        return plugin.getConfig().getDouble("firma." + firma + ".multiplier", 1.0);
    }

    public void upgradeMultiplier(String firma) {
        plugin.getConfig().set("firma." + firma + ".multiplier", getMultiplier(firma) + 0.5);
        plugin.saveConfig();
    }

    public double getLimit(String firma) {
        return plugin.getConfig().getDouble("firma." + firma + ".limit", 10000);
    }

    public void upgradeLimit(String firma) {
        plugin.getConfig().set("firma." + firma + ".limit", getLimit(firma) + 5000);
        plugin.saveConfig();
    }
}
