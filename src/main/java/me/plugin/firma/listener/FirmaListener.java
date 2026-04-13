package me.plugin.firma.listener;

import me.plugin.firma.gui.*;
import me.plugin.firma.manager.FirmaManager;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;

public class FirmaListener implements Listener {

    private final FirmaManager manager;

    public FirmaListener(FirmaManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        Player p = (Player) e.getWhoClicked();

        if (!e.getView().getTitle().equals("§6BizCore")) return;

        e.setCancelled(true);

        switch (e.getSlot()) {

            case 11:
                MembersGUI.open(p, manager);
                break;

            case 12:
                UpgradeGUI.open(p, manager);
                break;

            case 14:
                JobsGUI.open(p, manager);
                break;

            case 15:
                QuestGUI.open(p, manager);
                break;

            case 16:
                TopGUI.open(p, manager);
                break;
        }
    }
}
