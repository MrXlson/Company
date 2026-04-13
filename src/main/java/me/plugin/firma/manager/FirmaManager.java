package me.plugin.firma.manager;

import org.bukkit.entity.Player;

import java.util.*;

public class FirmaManager {

    private final HashMap<String, Firma> firmy = new HashMap<>();
    private final HashMap<UUID, String> playerFirma = new HashMap<>();

    public void createFirma(Player p, String name) {
        Firma f = new Firma(name, p.getUniqueId());
        firmy.put(name.toLowerCase(), f);
        playerFirma.put(p.getUniqueId(), name.toLowerCase());
    }

    public boolean hasFirma(Player p) {
        return playerFirma.containsKey(p.getUniqueId());
    }

    public Firma getFirma(Player p) {
        String name = playerFirma.get(p.getUniqueId());
        if (name == null) return null;
        return firmy.get(name);
    }

    // ================= MEMBERS =================

    public void addMember(Player p, String name) {
        Firma f = getFirma(p);
        if (f == null) return;

        f.members.add(name);
    }

    public void removeMember(Player p, String name) {
        Firma f = getFirma(p);
        if (f == null) return;

        f.members.remove(name);
    }

    public List<String> getMembers(Player p) {
        Firma f = getFirma(p);
        return f == null ? new ArrayList<>() : f.members;
    }

    // ================= MONEY =================

    public double getBalance(Player p) {
        Firma f = getFirma(p);
        return f == null ? 0 : f.balance;
    }

    public void addBalance(Player p, double amount) {
        Firma f = getFirma(p);
        if (f == null) return;

        f.balance += amount;
    }

    // ================= LEVEL =================

    public int getLevel(Player p) {
        Firma f = getFirma(p);
        return f == null ? 1 : f.level;
    }

    public int getXP(Player p) {
        Firma f = getFirma(p);
        return f == null ? 0 : f.xp;
    }

    public void addXP(Player p, int amount) {
        Firma f = getFirma(p);
        if (f == null) return;

        f.xp += amount;

        if (f.xp >= f.level * 100) {
            f.xp = 0;
            f.level++;
        }
    }

    public int getLimit(Player p) {
        return getLevel(p) * 5;
    }

    public double getMultiplier(Player p) {
        return 1.0 + (getLevel(p) * 0.1);
    }

    // ================= CLASS =================

    public static class Firma {
        String name;
        UUID owner;
        List<String> members = new ArrayList<>();

        double balance = 0;
        int level = 1;
        int xp = 0;

        public Firma(String name, UUID owner) {
            this.name = name;
            this.owner = owner;
        }
    }
}
