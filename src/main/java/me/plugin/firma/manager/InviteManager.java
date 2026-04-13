package me.plugin.firma.manager;

import java.util.HashMap;
import java.util.UUID;

public class InviteManager {

    private static final HashMap<UUID, String> invites = new HashMap<>();

    public static void sendInvite(UUID player, String firma) {
        invites.put(player, firma);
    }

    public static String getInvite(UUID player) {
        return invites.get(player);
    }

    public static void remove(UUID player) {
        invites.remove(player);
    }
}
