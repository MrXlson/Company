package me.plugin.firma.listener;

import me.plugin.firma.gui.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player p)) return;

        String title = e.getView().getTitle();

        if (title.equals("§6BizCore") || title.equals("§aPráce") || title.equals("§bČlenové")) {
            e.setCancelled(true);
        }

        if (e.getCurrentItem() == null) return;

        // MAIN GUI
        if (title.equals("§6BizCore")) {

            switch (e.getSlot()) {
                case 11 -> JobsGUI.open(p);
                case 13 -> MembersGUI.open(p);
            }
        }
    }
}
