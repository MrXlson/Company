package me.plugin.firma.listener;

import me.plugin.firma.manager.FirmaManager;
import me.plugin.firma.gui.*;
import me.plugin.firma.chat.ChatInputManager;

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

        // ❌ blokování braní itemů
        if (title.contains("BizCore") || title.contains("Členové") || title.contains("Práce") || title.contains("Questy") || title.contains("Upgrady")) {
            e.setCancelled(true);
        }

        if (e.getCurrentItem() == null) return;

        // ===============================
        // 🧠 MAIN GUI
        // ===============================
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

        // ===============================
        // 👥 MEMBERS GUI
        // ===============================
        if (title.equals("§aČlenové")) {

            String name = e.getCurrentItem().getItemMeta().getDisplayName();

            if (name == null) return;

            if (name.contains("Přidat")) {
                p.closeInventory();
                p.sendMessage("§aNapiš jméno hráče do chatu:");
                ChatInputManager.waitFor(p.getUniqueId(), "add");
            }

            if (name.contains("Odebrat")) {
                p.closeInventory();
                p.sendMessage("§cNapiš jméno hráče:");
                ChatInputManager.waitFor(p.getUniqueId(), "remove");
            }
        }

        // ===============================
        // 🛒 UPGRADES GUI
        // ===============================
        if (title.equals("§aUpgrady")) {

            String f = manager.getCompany(p.getUniqueId());
            if (f == null) return;

            if (e.getSlot() == 13) {

                if (manager.getBalance(f) >= 1000) {
                    manager.removeBalance(f, 1000);
                    manager.upgradeMultiplier(f);
                    p.sendMessage("§aUpgrade koupen!");
                } else {
                    p.sendMessage("§cNedostatek peněz!");
                }
            }
        }

        // ===============================
        // 🧑‍💼 JOBS GUI
        // ===============================
        if (title.equals("§bPráce")) {
            p.sendMessage("§aVybral jsi práci!");
        }

        // ===============================
        // 📜 QUEST GUI
        // ===============================
        if (title.equals("§dQuesty")) {
            p.sendMessage("§dQuest kliknut!");
        }
    }
}
