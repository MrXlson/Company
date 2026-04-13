package me.plugin.firma.listener;

import me.plugin.firma.manager.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.PlayerDeathEvent;

public class QuestListener implements Listener {

    private final FirmaManager manager;

    public QuestListener(FirmaManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onKill(PlayerDeathEvent e) {

        Player killer = e.getEntity().getKiller();
        if (killer == null) return;

        QuestManager.addKill(killer.getUniqueId());

        String firma = manager.getCompany(killer.getUniqueId());
        if (firma == null) return;

        if (QuestManager.getKills(killer.getUniqueId()) >= 10) {

            manager.addBalance(firma, 1000);
            manager.addXP(firma, 100);

            killer.sendMessage("§aQuest splněn!");

            QuestManager.reset(killer.getUniqueId());
        }
    }
}
