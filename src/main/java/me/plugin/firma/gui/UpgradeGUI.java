package me.plugin.firma.gui;

import me.plugin.firma.manager.FirmaManager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Arrays;

public class UpgradeGUI {

    public static void open(Player player, FirmaManager manager) {

        String firma = manager.getCompany(player.getUniqueId());

        Inventory inv = Bukkit.createInventory(null, 27, "§6Upgrade Shop");

        ItemStack multi = new ItemStack(Material.EMERALD);
        ItemMeta mm = multi.getItemMeta();
        mm.setDisplayName("§aMultiplier Upgrade");
        mm.setLore(Arrays.asList(
                "§7Aktuální: " + manager.getMultiplier(firma),
                "§6Cena: 1000"
        ));
        multi.setItemMeta(mm);

        inv.setItem(11, multi);

        player.openInventory(inv);
    }

    public static void handleClick(Player player, InventoryClickEvent e, FirmaManager manager) {

        String firma = manager.getCompany(player.getUniqueId());
        String name = e.getCurrentItem().getItemMeta().getDisplayName();

        if (name.equals("§aMultiplier Upgrade")) {

            if (manager.getBalance(firma) < 1000) {
                player.sendMessage("§cMálo peněz!");
                return;
            }

            manager.removeBalance(firma, 1000);
            manager.upgradeMultiplier(firma);

            player.sendMessage("§aUpgrade koupen!");
        }
    }
}
