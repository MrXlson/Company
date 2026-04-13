package me.plugin.firma.company;

import java.util.*;

public class Company {

    public String name;
    public UUID owner;

    public double balance = 0;
    public int level = 1;
    public double xp = 0;

    public Map<UUID, Role> members = new HashMap<>();

    public Company(String name, UUID owner) {
        this.name = name;
        this.owner = owner;
        members.put(owner, Role.OWNER);
    }

    public void addXP(double amount, int xpPerLevel) {
        xp += amount;

        if (xp >= xpPerLevel) {
            xp = 0;
            level++;
        }
    }
}
