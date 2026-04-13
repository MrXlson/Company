package me.plugin.firma.listener;

import me.plugin.firma.FirmaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    private final FirmaPlugin plugin;

    public InventoryClickListener(FirmaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().contains("BizCore")) {
            e.setCancelled(true);

            if (e.getCurrentItem() == null) return;

            switch (e.getCurrentItem().getType()) {

                case PAPER:
                    e.getWhoClicked().sendMessage("Info o firmě");
                    break;

                case DIAMOND_PICKAXE:
                    e.getWhoClicked().sendMessage("Práce GUI");
                    break;

                case BOOK:
                    e.getWhoClicked().sendMessage("Questy GUI");
                    break;

                case CHEST:
                    e.getWhoClicked().sendMessage("Members GUI");
                    break;

                case EMERALD:
                    e.getWhoClicked().sendMessage("Upgrade GUI");
                    break;

                case NAME_TAG:
                    plugin.getChatInputManager().waitFor(e.getWhoClicked().getUniqueId(), "rename");
                    e.getWhoClicked().sendMessage("Napiš nový název firmy do chatu:");
                    e.getWhoClicked().closeInventory();
                    break;

                default:
                    break;
            }
        }
    }
}
