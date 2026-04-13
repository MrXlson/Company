package me.plugin.firma.listener;

import me.plugin.firma.chat.ChatInputManager;
import me.plugin.firma.manager.FirmaManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.entity.Player;

public class ChatListener implements Listener {

    private final FirmaManager manager;

    public ChatListener(FirmaManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

        Player player = e.getPlayer();

        if (!ChatInputManager.isWaiting(player.getUniqueId())) return;

        e.setCancelled(true);

        String name = e.getMessage();

        manager.createCompany(player.getUniqueId(), name);
        ChatInputManager.remove(player.getUniqueId());

        player.sendMessage("§aFirma vytvořena: " + name);
    }
}
