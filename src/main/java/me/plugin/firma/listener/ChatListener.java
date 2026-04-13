package me.plugin.firma.listener;

import me.plugin.firma.chat.ChatInputManager;
import me.plugin.firma.manager.FirmaManager;
import org.bukkit.event.*;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.entity.Player;

public class ChatListener implements Listener {

    private final FirmaManager manager;

    public ChatListener(FirmaManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

        Player p = e.getPlayer();

        if (!ChatInputManager.isWaiting(p.getUniqueId())) return;

        e.setCancelled(true);

        manager.createCompany(p.getUniqueId(), e.getMessage());
        ChatInputManager.remove(p.getUniqueId());

        p.sendMessage("§aFirma vytvořena!");
    }
}
