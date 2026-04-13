package me.plugin.firma.listener;

import me.plugin.firma.chat.ChatInputManager;
import me.plugin.firma.manager.FirmaManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private final FirmaManager manager;

    public ChatListener(FirmaManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

        Player p = e.getPlayer();

        if (!ChatInputManager.has(p.getUniqueId())) return;

        e.setCancelled(true);

        String action = ChatInputManager.get(p.getUniqueId());
        String msg = e.getMessage();

        if (action.equals("add")) {
            manager.addMember(p, msg);
            p.sendMessage("§aPřidán!");
        }

        if (action.equals("remove")) {
            manager.removeMember(p, msg);
            p.sendMessage("§cOdebrán!");
        }

        ChatInputManager.remove(p.getUniqueId());
    }
}
