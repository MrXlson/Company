package me.plugin.firma.listener;

import me.plugin.firma.chat.ChatInputManager;
import me.plugin.firma.gui.*;
import me.plugin.firma.manager.FirmaManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class FirmaListener implements Listener {

    private final FirmaManager manager;

    public FirmaListener(FirmaManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player)) return;

        Player p = (Player) e.getWhoClicked();
        String title = e.getView().getTitle();

        if (title.contains("BizCore") || title.contains("Člen")) {
            e.setCancelled(true);
        }

        if (e.getCurrentItem() == null) return;

        // MAIN
        if (title.contains("BizCore")) {

            switch (e.getSlot()) {
                case 11 -> JobsGUI.open(p, manager);
                case 13 -> UpgradeGUI.open(p, manager);
                case 15 -> QuestGUI.open(p, manager);
                case 22 -> MembersGUI.open(p, manager);
            }
        }

        // MEMBERS
        if (title.contains("Člen")) {

            String name = e.getCurrentItem().getItemMeta().getDisplayName();
            if (name == null) return;

            if (name.contains("Přidat")) {
                p.closeInventory();
                p.sendMessage("Napiš jméno:");
                ChatInputManager.waitFor(p.getUniqueId(), "add");
            }

            if (name.contains("Odebrat")) {
                p.closeInventory();
                p.sendMessage("Napiš jméno:");
                ChatInputManager.waitFor(p.getUniqueId(), "remove");
            }
        }
    }
}
