package me.plugin.firma.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BankGUI {

    public static void open(Player p) {

        Inventory inv = Bukkit.createInventory(null, 27, "§bBanka");

        inv.setItem(11, item(Material.GREEN_WOOL, "§aVložit +100"));
        inv.setItem(13, item(Material.RED_WOOL, "§cVybrat -100"));
        inv.setItem(15, item(Material.BARRIER, "§cZpět"));

        p.openInventory(inv);
    }

    private static ItemStack item(Material m, String name) {
        ItemStack i = new ItemStack(m);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(name);
        i.setItemMeta(meta);
        return i;
    }
}
