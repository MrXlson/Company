package me.plugin.firma.manager;

import java.util.HashMap;

public class WarManager {

    private static final HashMap<String, String> wars = new HashMap<>();

    public static void startWar(String f1, String f2) {
        wars.put(f1, f2);
        wars.put(f2, f1);
    }

    public static boolean isInWar(String firma) {
        return wars.containsKey(firma);
    }

    public static String getEnemy(String firma) {
        return wars.get(firma);
    }
}
