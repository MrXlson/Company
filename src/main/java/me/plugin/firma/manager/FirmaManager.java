package me.plugin.firma.manager;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class FirmaManager {

    private final JavaPlugin plugin;

    // firma → data
    private final Map<String, Firma> data = new HashMap<>();

    public FirmaManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    // ===============================
    // 🏢 CREATE FIRMA
    // ===============================
    public void createFirma(String name, UUID owner) {
        Firma f = new Firma(name);
        f.getMembers().put(owner, "OWNER");
        data.put(name, f);
    }

    // ===============================
    // 👤 GET FIRMA
    // ===============================
    public String getFirma(Player p) {
        return getCompany(p.getUniqueId());
    }

    public String getCompany(UUID uuid) {
        for (String key : data.keySet()) {
            if (data.get(key).getMembers().containsKey(uuid)) {
                return key;
            }
        }
        return null;
    }

    public boolean hasCompany(UUID uuid) {
        return getCompany(uuid) != null;
    }

    // ===============================
    // 👥 MEMBERS
    // ===============================
    public Map<UUID, String> getMembers(String firma) {
        return data.get(firma).getMembers();
    }

    public void addMember(String firma, UUID uuid) {
        data.get(firma).getMembers().put(uuid, "MEMBER");
    }

    public void removeMember(String firma, UUID uuid) {
        data.get(firma).getMembers().remove(uuid);
    }

    public String getRole(String firma, UUID uuid) {
        return data.get(firma).getMembers().getOrDefault(uuid, "NONE");
    }

    public boolean isOwner(String firma, UUID uuid) {
        return getRole(firma, uuid).equals("OWNER");
    }

    // ===============================
    // 💰 ECONOMY
    // ===============================
    public double getBalance(String firma) {
        return data.get(firma).getBalance();
    }

    public void addBalance(String firma, double amount) {
        data.get(firma).setBalance(getBalance(firma) + amount);
    }

    public void removeBalance(String firma, double amount) {
        data.get(firma).setBalance(getBalance(firma) - amount);
    }

    // ===============================
    // ⭐ XP + LEVEL
    // ===============================
    public void addXP(String firma, int amount) {
        data.get(firma).setXp(data.get(firma).getXp() + amount);
    }

    public int getXP(String firma) {
        return data.get(firma).getXp();
    }

    public int getLevel(String firma) {
        return getXP(firma) / 100; // 100 XP = 1 level
    }

    // ===============================
    // 📦 LIMIT (např. max členů)
    // ===============================
    public int getLimit(String firma) {
        return 10 + getLevel(firma);
    }

    // ===============================
    // 📈 MULTIPLIER
    // ===============================
    public double getMultiplier(String firma) {
        return data.get(firma).getMultiplier();
    }

    public void upgradeMultiplier(String firma) {
        data.get(firma).setMultiplier(getMultiplier(firma) + 0.1);
    }

    // ===============================
    // 🔌 PLUGIN
    // ===============================
    public JavaPlugin getPlugin() {
        return plugin;
    }
}
