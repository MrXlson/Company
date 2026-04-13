package me.plugin.firma.company;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Company implements Serializable {

    public String name;
    public UUID owner;
    public double balance = 0;

    public Map<UUID, Role> members = new HashMap<>();
    public Map<UUID, String> jobs = new HashMap<>();

    public Company(String name, UUID owner) {
        this.name = name;
        this.owner = owner;
        members.put(owner, Role.OWNER);
    }
}
