package me.plugin.firma.chat;

import java.util.HashMap;
import java.util.UUID;

public class ChatInputManager {

    private static final HashMap<UUID, String> input = new HashMap<>();

    public static void waitFor(UUID uuid, String type) {
        input.put(uuid, type);
    }

    public static boolean has(UUID uuid) {
        return input.containsKey(uuid);
    }

    public static String get(UUID uuid) {
        return input.get(uuid);
    }

    public static void remove(UUID uuid) {
        input.remove(uuid);
    }
}
