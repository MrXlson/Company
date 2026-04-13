package me.plugin.firma.manager;

import java.util.HashMap;
import java.util.UUID;

public class QuestManager {

    private static final HashMap<UUID, Integer> kills = new HashMap<>();

    public static void addKill(UUID uuid) {
        kills.put(uuid, kills.getOrDefault(uuid, 0) + 1);
    }

    public static int getKills(UUID uuid) {
        return kills.getOrDefault(uuid, 0);
    }

    public static void reset(UUID uuid) {
        kills.remove(uuid);
    }
}
