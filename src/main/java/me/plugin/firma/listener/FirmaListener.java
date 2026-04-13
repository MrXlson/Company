package me.plugin.firma.listener;

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

        if (e.getView().getTitle().equals("§aUpgrady")) {

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
