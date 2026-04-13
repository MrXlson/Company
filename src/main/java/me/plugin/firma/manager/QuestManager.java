package me.plugin.firma.manager;

import java.util.HashMap;

public class QuestManager {

    private static final HashMap<String, Integer> progress = new HashMap<>();

    public static void add(String firma) {
        progress.put(firma, progress.getOrDefault(firma, 0) + 1);
    }

    public static int get(String firma) {
        return progress.getOrDefault(firma, 0);
    }

    public static void reset(String firma) {
        progress.remove(firma);
    }
}
