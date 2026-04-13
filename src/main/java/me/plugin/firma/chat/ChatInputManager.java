package me.plugin.firma.chat;

import java.util.HashMap;
import java.util.UUID;

public class ChatInputManager {

    private final HashMap<UUID, String> waiting = new HashMap<>();
    private final HashMap<UUID, String> inputs = new HashMap<>();

    // Nastaví hráče do čekání na input
    public void waitFor(UUID uuid, String type) {
        waiting.put(uuid, type);
    }

    // Zkontroluje jestli hráč čeká na input
    public boolean isWaiting(UUID uuid) {
        return waiting.containsKey(uuid);
    }

    // Získá typ inputu (např. "rename", "invite")
    public String getType(UUID uuid) {
        return waiting.get(uuid);
    }

    // Uloží input
    public void set(UUID uuid, String value) {
        inputs.put(uuid, value);
        waiting.remove(uuid);
    }

    // Získá input
    public String get(UUID uuid) {
        return inputs.get(uuid);
    }

    // Vymaže input
    public void remove(UUID uuid) {
        inputs.remove(uuid);
        waiting.remove(uuid);
    }
}
