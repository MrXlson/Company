package me.plugin.firma.chat;

import java.util.HashMap;
import java.util.UUID;

public class ChatInputManager {

    private static final HashMap<UUID, String> map = new HashMap<>();

    public static void waitFor(UUID uuid, String action) {
        map.put(uuid, action);
    }

    public static boolean has(UUID uuid) {
        return map.containsKey(uuid);
    }

    public static String get(UUID uuid) {
        return map.get(uuid);
    }

    public static void remove(UUID uuid) {
        map.remove(uuid);
    }
}
