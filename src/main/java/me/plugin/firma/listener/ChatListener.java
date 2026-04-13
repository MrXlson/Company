package me.plugin.firma.listener;

import me.plugin.firma.FirmaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private final FirmaPlugin plugin;

    public ChatListener(FirmaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (plugin.getChatInputManager().isWaiting(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);

            String type = plugin.getChatInputManager().getType(e.getPlayer().getUniqueId());
            String message = e.getMessage();

            if (type.equalsIgnoreCase("rename")) {
                e.getPlayer().sendMessage("Firma přejmenována na: " + message);
            }

            plugin.getChatInputManager().set(e.getPlayer().getUniqueId(), message);
        }
    }
}
