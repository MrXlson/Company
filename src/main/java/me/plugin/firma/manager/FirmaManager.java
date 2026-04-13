package me.plugin.firma.manager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class FirmaManager {

    private final JavaPlugin plugin;

    public FirmaManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean hasCompany(UUID uuid) {
        return plugin.getConfig().contains("players." + uuid + ".firma");
    }

    public String getCompany(UUID uuid) {
        return plugin.getConfig().getString("players." + uuid + ".firma");
    }

    public void createCompany(UUID owner, String name) {
        FileConfiguration c = plugin.getConfig();

        c.set("firma." + name + ".owner", owner.toString());
        c.set("firma." + name + ".members", new ArrayList<>(Collections.singletonList(owner.toString())));
        c.set("firma." + name + ".balance", 0);
        c.set("firma." + name + ".level", 1);
        c.set("firma." + name + ".xp", 0);

        c.set("players." + owner + ".firma", name);

        plugin.saveConfig();
    }

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

    public double getBalance(String firma) {
        return plugin.getConfig().getDouble("firma." + firma + ".balance");
    }

    public void addBalance(String firma, double amount) {
        plugin.getConfig().set("firma." + firma + ".balance", getBalance(firma) + amount);
        plugin.saveConfig();
    }

    public int getLevel(String firma) {
        return plugin.getConfig().getInt("firma." + firma + ".level");
    }

    public int getXP(String firma) {
        return plugin.getConfig().getInt("firma." + firma + ".xp");
    }

    public void addXP(String firma, int amount) {
        int xp = getXP(firma) + amount;
        plugin.getConfig().set("firma." + firma + ".xp", xp);

        int level = getLevel(firma);
        int needed = plugin.getConfig().getInt("levels." + level + ".xp");

        if (xp >= needed) {
            plugin.getConfig().set("firma." + firma + ".xp", 0);
            plugin.getConfig().set("firma." + firma + ".level", level + 1);
        }

        plugin.saveConfig();
    }
}
