package me.plugin.firma.listener;

import me.plugin.firma.chat.ChatInputManager;
import me.plugin.firma.gui.FirmaGUI;
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

        if (e.getView().getTitle().equals("§6BizCore")) {
            e.setCancelled(true);

            if (e.getSlot() == 13 && !manager.hasCompany(p.getUniqueId())) {
                ChatInputManager.add(p.getUniqueId());
                p.closeInventory();
                p.sendMessage("§eNapiš název firmy");
            }
        }
    }
}
