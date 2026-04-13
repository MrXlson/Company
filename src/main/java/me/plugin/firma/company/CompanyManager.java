package me.plugin.firma.company;

import org.bukkit.entity.Player;
import java.util.*;

public class CompanyManager {

    private final Map<String, Company> companies = new HashMap<>();
    private final Map<UUID, String> invites = new HashMap<>();

    public void createCompany(String name, Player p) {
        companies.put(name, new Company(name, p.getUniqueId()));
    }

    public Company getCompany(Player p) {
        for (Company c : companies.values()) {
            if (c.members.containsKey(p.getUniqueId())) return c;
        }
        return null;
    }

    public void invite(Player p, Player t) {
        Company c = getCompany(p);
        invites.put(t.getUniqueId(), c.name);
        t.sendMessage("§aPozvánka! /firma accept");
    }

    public void accept(Player p) {
        if (!invites.containsKey(p.getUniqueId())) return;
        Company c = companies.get(invites.remove(p.getUniqueId()));
        c.members.put(p.getUniqueId(), Role.WORKER);
    }

    public Map<String, Company> getCompanies() {
        return companies;
    }

    public void setCompanies(Map<String, Company> map) {
        companies.clear();
        companies.putAll(map);
    }
}
