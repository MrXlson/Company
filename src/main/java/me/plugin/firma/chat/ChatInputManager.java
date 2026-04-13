package me.plugin.firma.chat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatInputManager {

    private static final Map<UUID, String> input = new HashMap<>();

    // ===============================
    // ➕ SET
    // ===============================
    public static void set(UUID uuid, String type) {
        input.put(uuid, type);
    }

    // ===============================
    // 📥 GET
    // ===============================
    public static String get(UUID uuid) {
        return input.get(uuid);
    }

    // ===============================
    // ❌ REMOVE
    // ===============================
    public static void remove(UUID uuid) {
        input.remove(uuid);
    }

    // ===============================
    // ✔ HAS
    // ===============================
    public static boolean has(UUID uuid) {
        return input.containsKey(uuid);
    }
}
