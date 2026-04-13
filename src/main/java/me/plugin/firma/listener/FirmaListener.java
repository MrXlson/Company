package me.plugin.firma.listener;

import me.plugin.firma.FirmaPlugin;
import me.plugin.firma.manager.FirmaManager;
import me.plugin.firma.gui.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class FirmaListener implements Listener {

    private final FirmaManager manager;
    private final FirmaPlugin plugin;

    public FirmaListener(FirmaPlugin plugin, FirmaManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player)) return;

        Player p = (Player) e.getWhoClicked();
        String title = e.getView().getTitle();

        // ❌ blokování itemů
        if (title.contains("BizCore") || title.contains("Členové")) {
            e.setCancelled(true);
        }

        if (e.getCurrentItem() == null) return;

        // =========================
        // 🧠 MAIN GUI
        // =========================
        if (title.equals("§6BizCore")) {

            switch (e.getSlot()) {

                case 11:
                    JobsGUI.open(p, manager);
                    break;

                case 13:
                    UpgradeGUI.open(p, manager);
                    break;

                case 15:
                    QuestGUI.open(p, manager);
                    break;

                case 22:
                    MembersGUI.open(p, manager);
                    break;
            }
        }

        // =========================
        // 👥 MEMBERS GUI
        // =========================
        if (title.equals("§aČlenové")) {

            if (e.getCurrentItem().getItemMeta() == null) return;
            String name = e.getCurrentItem().getItemMeta().getDisplayName();

            if (name == null) return;

            if (name.contains("Přidat")) {
                p.closeInventory();
                p.sendMessage("§aNapiš jméno hráče do chatu:");

                // ✅ SPRÁVNĚ
                plugin.getChatInputManager().waitFor(p.getUniqueId(), "add");
            }
        }
    }
}
