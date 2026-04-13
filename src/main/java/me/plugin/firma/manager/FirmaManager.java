package me.plugin.firma.manager;

import org.bukkit.entity.Player;

import java.util.*;

public class FirmaManager {

    private final HashMap<String, Firma> firmy = new HashMap<>();
    private final HashMap<UUID, String> playerFirma = new HashMap<>();

    // =========================
    // 🏢 CREATE FIRMA
    // =========================
    public void createFirma(Player p, String name) {

        Firma firma = new Firma(name, p.getUniqueId());

        firmy.put(name.toLowerCase(), firma);
        playerFirma.put(p.getUniqueId(), name.toLowerCase());
    }

    // =========================
    // 🔍 GET FIRMA
    // =========================
    public boolean hasFirma(Player p) {
        return playerFirma.containsKey(p.getUniqueId());
    }

    public Firma getFirma(Player p) {
        String name = playerFirma.get(p.getUniqueId());
        return firmy.get(name);
    }

    public String getFirmaName(Player p) {
        return playerFirma.get(p.getUniqueId());
    }

    // =========================
    // 👥 MEMBERS
    // =========================
    public void addMember(Player owner, String playerName) {

        Firma firma = getFirma(owner);
        if (firma == null) return;

        firma.getMembers().add(playerName.toLowerCase());
    }

    public void removeMember(Player owner, String playerName) {

        Firma firma = getFirma(owner);
        if (firma == null) return;

        firma.getMembers().remove(playerName.toLowerCase());
    }

    public List<String> getMembers(Player p) {
        Firma firma = getFirma(p);
        if (firma == null) return new ArrayList<>();

        return firma.getMembers();
    }

    // =========================
    // 💰 BALANCE
    // =========================
    public double getBalance(Player p) {
        Firma f = getFirma(p);
        return f == null ? 0 : f.getBalance();
    }

    public void addBalance(Player p, double amount) {
        Firma f = getFirma(p);
        if (f == null) return;

        f.setBalance(f.getBalance() + amount);
    }

    public void removeBalance(Player p, double amount) {
        Firma f = getFirma(p);
        if (f == null) return;

        f.setBalance(Math.max(0, f.getBalance() - amount));
    }

    // =========================
    // ⭐ LEVEL + XP
    // =========================
    public int getLevel(Player p) {
        Firma f = getFirma(p);
        return f == null ? 1 : f.getLevel();
    }

    public int getXP(Player p) {
        Firma f = getFirma(p);
        return f == null ? 0 : f.getXp();
    }

    public void addXP(Player p, int amount) {
        Firma f = getFirma(p);
        if (f == null) return;

        int xp = f.getXp() + amount;
        int level = f.getLevel();

        int needed = level * 100;

        if (xp >= needed) {
            xp -= needed;
            level++;
        }

        f.setXp(xp);
        f.setLevel(level);
    }

    // =========================
    // 📊 LIMIT (např. práce)
    // =========================
    public int getLimit(Player p) {
        return getLevel(p) * 5;
    }

    // =========================
    // 🚀 MULTIPLIER
    // =========================
    public double getMultiplier(Player p) {
        return 1.0 + (getLevel(p) * 0.1);
    }

    public void upgradeMultiplier(Player p) {
        // připravené do budoucna
    }

    // =========================
    // 🧠 FIRMA CLASS
    // =========================
    public static class Firma {

        private final String name;
        private final UUID owner;
        private final List<String> members;

        private double balance;
        private int level;
        private int xp;

        public Firma(String name, UUID owner) {
            this.name = name;
            this.owner = owner;
            this.members = new ArrayList<>();
            this.balance = 0;
            this.level = 1;
            this.xp = 0;
        }

        public String getName() {
            return name;
        }

        public UUID getOwner() {
            return owner;
        }

        public List<String> getMembers() {
            return members;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getXp() {
            return xp;
        }

        public void setXp(int xp) {
            this.xp = xp;
        }
    }
                     }
