package me.plugin.firma.company;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CompanyManager {

    private Map<String, Company> companies = new HashMap<>();

    public Company createCompany(String name, Player p) {
        Company c = new Company(name, p.getUniqueId());
        companies.put(name, c);
        return c;
    }

    public Company getCompany(Player p) {
        for (Company c : companies.values()) {
            if (c.members.containsKey(p.getUniqueId())) return c;
        }
        return null;
    }

    public Map<String, Company> getCompanies() {
        return companies;
    }

    public void setCompanies(Map<String, Company> companies) {
        this.companies = companies;
    }
}
