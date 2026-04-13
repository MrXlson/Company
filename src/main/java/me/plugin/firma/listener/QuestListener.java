package me.plugin.firma.listener;

import me.plugin.firma.manager.*;

import org.bukkit.event.*;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.entity.Player;

public class QuestListener implements Listener {

    private final FirmaManager manager;

    public QuestListener(FirmaManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onKill(PlayerDeathEvent e) {

        Player killer = e.getEntity().getKiller();
        if (killer == null) return;

        String firma = manager.getCompany(killer.getUniqueId());
        if (firma == null) return;

        QuestManager.add(firma);

        if (QuestManager.get(firma) >= 10) {
            manager.addBalance(firma, 500);
            manager.addXP(firma, 50);
            QuestManager.reset(firma);

            killer.sendMessage("§aQuest splněn!");
        }
    }
}
