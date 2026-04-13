package me.plugin.firma.chat;

import java.util.HashMap;
import java.util.UUID;

public class ChatInputManager {

    private static final HashMap<UUID, String> waiting = new HashMap<>();

    public static void waitFor(UUID uuid, String action) {
        waiting.put(uuid, action);
    }

    public static boolean has(UUID uuid) {
        return waiting.containsKey(uuid);
    }

    public static String get(UUID uuid) {
        return waiting.get(uuid);
    }

    public static void remove(UUID uuid) {
        waiting.remove(uuid);
    }
}
