package me.plugin.firma.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

public class MembersGUI {

    public static void open(Player p) {

        Inventory inv = Bukkit.createInventory(null, 27, "§bČlenové");

        inv.setItem(11, item(Material.LIME_DYE, "§aPozvat hráče"));
        inv.setItem(15, item(Material.RED_DYE, "§cVyhodit hráče"));

        p.openInventory(inv);
    }

    private static ItemStack item(Material mat, String name) {
        ItemStack i = new ItemStack(mat);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(name);
        i.setItemMeta(meta);
        return i;
    }
}
