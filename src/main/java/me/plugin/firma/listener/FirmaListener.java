package me.plugin.firma.listener;

import me.plugin.firma.chat.ChatInputManager;
import me.plugin.firma.gui.*;
import me.plugin.firma.manager.FirmaManager;

import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class FirmaListener implements Listener {

    private final FirmaManager manager;

    public FirmaListener(FirmaManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player)) return;
        Player player = (Player) e.getWhoClicked();

        String title = e.getView().getTitle();

        if (title.equals("§6BizCore")) {
            e.setCancelled(true);

            ItemStack item = e.getCurrentItem();
            if (item == null || !item.hasItemMeta()) return;

            String name = item.getItemMeta().getDisplayName();

            if (name.equals("§aZaložit firmu")) {
                player.closeInventory();
                player.sendMessage("§eNapiš název firmy do chatu:");
                ChatInputManager.add(player.getUniqueId());
            }
        }

        if (title.equals("§6Upgrade Shop")) {
            e.setCancelled(true);
            UpgradeGUI.handleClick(player, e, manager);
        }

        if (title.equals("§6Top Firmy")) {
            e.setCancelled(true);
        }
    }
}
