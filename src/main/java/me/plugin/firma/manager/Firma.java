package me.plugin.firma.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Firma {

    private final String name;

    private double balance = 0;
    private int xp = 0;
    private double multiplier = 1.0;

    private Map<UUID, String> members = new HashMap<>();

    public Firma(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    public Map<UUID, String> getMembers() {
        return members;
    }
}
