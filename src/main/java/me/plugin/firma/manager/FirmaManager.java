package me.plugin.firma.manager;

import me.plugin.firma.FirmaPlugin;

import java.util.*;

public class FirmaManager {

    private final FirmaPlugin plugin;
    private final Map<UUID, List<UUID>> companies = new HashMap<>();

    public FirmaManager(FirmaPlugin plugin) {
        this.plugin = plugin;
    }

    public List<UUID> getMembers(UUID owner) {
        return companies.getOrDefault(owner, new ArrayList<>());
    }

    public void addMember(UUID owner, UUID member) {
        companies.computeIfAbsent(owner, k -> new ArrayList<>()).add(member);
    }

    public void removeMember(UUID owner, UUID member) {
        if (companies.containsKey(owner)) {
            companies.get(owner).remove(member);
        }
    }
}
