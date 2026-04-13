package me.plugin.firma.company;

import java.util.*;

public class Company {

    public String name;
    public UUID owner;
    public double balance = 0;

    public int level = 1;
    public int xp = 0;

    public Map<UUID, Role> members = new HashMap<>();

    public Company(String name, UUID owner) {
        this.name = name;
        this.owner = owner;
        members.put(owner, Role.OWNER);
    }

    public void addXP(int a, int need) {
        xp += a;
        if (xp >= need) {
            xp = 0;
            level++;
        }
    }
}
