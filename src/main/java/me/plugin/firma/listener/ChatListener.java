package me.plugin.firma.listener;

import me.plugin.firma.chat.ChatInputManager;
import me.plugin.firma.manager.FirmaManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
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

        String type = ChatInputManager.get(p.getUniqueId());
        String msg = e.getMessage();

        String firma = manager.getFirma(p);
        if (firma == null) return;

        if (type.equals("add")) {

            Player target = Bukkit.getPlayer(msg);

            if (target == null) {
                p.sendMessage("§cHráč není online!");
            } else {
                manager.addMember(firma, target.getUniqueId());
                p.sendMessage("§aPřidán!");
            }

        } else if (type.equals("remove")) {

            Player target = Bukkit.getPlayer(msg);

            if (target == null) {
                p.sendMessage("§cHráč není online!");
            } else {

                String role = manager.getRole(firma, target.getUniqueId());

                if (role.equals("OWNER")) {
                    p.sendMessage("§cNemůžeš odebrat ownera!");
                    return;
                }

                manager.removeMember(firma, target.getUniqueId());
                p.sendMessage("§cOdebrán!");
            }
        }

        ChatInputManager.remove(p.getUniqueId());
    }
}
