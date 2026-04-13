package me.plugin.firma.chat;

import java.util.HashSet;
import java.util.UUID;

public class ChatInputManager {

    private static final HashSet<UUID> waiting = new HashSet<>();

    public static void add(UUID uuid) {
        waiting.add(uuid);
    }

    public static boolean isWaiting(UUID uuid) {
        return waiting.contains(uuid);
    }

    public static void remove(UUID uuid) {
        waiting.remove(uuid);
    }
}
