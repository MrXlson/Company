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

        if (!(e.getWhoClicked() instanceof Player)) return;

        Player p = (Player) e.getWhoClicked();
        String title = e.getView().getTitle();

        // 🔒 ZABLOKUJE BRANÍ ITEMŮ V GUI
        if (title.contains("BizCore") || title.contains("Menu") || title.contains("Firmy")) {
            e.setCancelled(true);
        }

        // 🧠 HLAVNÍ MENU
        if (title.equals("§6BizCore")) {

            e.setCancelled(true);

            switch (e.getSlot()) {

                case 10:
                    MembersGUI.open(p, manager);
                    break;

                case 12:
                    JobsGUI.open(p, manager);
                    break;

                case 14:
                    QuestGUI.open(p, manager);
                    break;

                case 16:
                    UpgradeGUI.open(p, manager);
                    break;

                case 22:
                    TopGUI.open(p, manager);
                    break;
            }
        }

        // 🛒 UPGRADY
        if (title.equals("§aUpgrady")) {

            e.setCancelled(true);

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
    }
}
