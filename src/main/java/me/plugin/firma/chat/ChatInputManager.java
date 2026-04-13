package me.plugin.firma.chat;

import java.util.HashMap;
import java.util.UUID;

public class ChatInputManager {

    private final HashMap<UUID, String> waiting = new HashMap<>();
    private final HashMap<UUID, String> inputs = new HashMap<>();

    public void waitFor(UUID uuid, String type) {
        waiting.put(uuid, type);
    }

    public boolean isWaiting(UUID uuid) {
        return waiting.containsKey(uuid);
    }

    public String getType(UUID uuid) {
        return waiting.get(uuid);
    }

    public void set(UUID uuid, String value) {
        inputs.put(uuid, value);
        waiting.remove(uuid);
    }

    public String get(UUID uuid) {
        return inputs.get(uuid);
    }

    public void remove(UUID uuid) {
        inputs.remove(uuid);
        waiting.remove(uuid);
    }
}
