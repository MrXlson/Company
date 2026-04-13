package me.plugin.firma.listener;

import me.plugin.firma.gui.*;
import me.plugin.firma.manager.FirmaManager;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    private final FirmaManager manager;

    public InventoryClickListener(FirmaManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player)) return;

        Player p = (Player) e.getWhoClicked();

        String title = e.getView().getTitle();

        // 🔥 ZABRÁNÍ BRANÍ ITEMŮ
        if (title.contains("BizCore") || title.contains("Firma") || title.contains("Členové")) {
            e.setCancelled(true);
        }

        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;

        String firma = manager.getFirma(p);
        if (firma == null) return;

        // ===============================
        // 🏢 MAIN GUI
        // ===============================
        if (title.contains("BizCore")) {

            switch (e.getSlot()) {

                case 11:
                    JobsGUI.open(p, manager);
                    break;

                case 13:
                    UpgradeGUI.open(p, manager);
                    break;

                case 15:
                    MembersGUI.open(p, manager);
                    break;
            }
        }

        // ===============================
        // 👥 MEMBERS GUI
        // ===============================
        if (title.contains("Členové")) {

            switch (e.getSlot()) {

                case 26:
                    p.sendMessage("§eNapiš jméno hráče do chatu pro přidání");
                    me.plugin.firma.chat.ChatInputManager.set(p.getUniqueId(), "add");
                    break;

                case 25:
                    p.sendMessage("§eNapiš jméno hráče do chatu pro odebrání");
                    me.plugin.firma.chat.ChatInputManager.set(p.getUniqueId(), "remove");
                    break;
            }
        }
    }
}
