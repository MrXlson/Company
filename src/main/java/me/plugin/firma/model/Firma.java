package me.plugin.firma.model;

import java.util.*;

public class Firma {

    private final String name;
    private final UUID owner;

    private final Set<UUID> members = new HashSet<>();
    private final Map<UUID, String> roles = new HashMap<>();

    private int level = 1;
    private int xp = 0;
    private double balance = 0;

    public Firma(String name, UUID owner) {
        this.name = name;
        this.owner = owner;

        roles.put(owner, "owner");
    }

    // ================= BASIC =================

    public String getName() { return name; }
    public UUID getOwner() { return owner; }

    public Set<UUID> getMembers() { return members; }
    public Map<UUID, String> getRoles() { return roles; }

    // ================= ROLE =================

    public String getRole(UUID uuid) {
        return roles.getOrDefault(uuid, "member");
    }

    public void addMember(UUID uuid) {
        members.add(uuid);
        roles.put(uuid, "member");
    }

    public void removeMember(UUID uuid) {
        members.remove(uuid);
        roles.remove(uuid);
    }

    public boolean isOwner(UUID uuid) {
        return owner.equals(uuid);
    }

    // ================= ECONOMY =================

    public double getBalance() { return balance; }

    public void addBalance(double amount) {
        balance += amount;
    }

    public void removeBalance(double amount) {
        balance = Math.max(0, balance - amount);
    }

    // ================= LEVEL =================

    public int getLevel() { return level; }
    public int getXp() { return xp; }

    public void addXP(int amount) {
        xp += amount;

        int needed = level * 100;

        if (xp >= needed) {
            xp -= needed;
            level++;
        }
    }
}
