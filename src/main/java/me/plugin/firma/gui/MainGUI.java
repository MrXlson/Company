package me.plugin.firma.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

public class MainGUI {

    public static void open(Player p) {

        Inventory inv = Bukkit.createInventory(null, 27, "§6BizCore");

        inv.setItem(11, item(Material.DIAMOND_PICKAXE, "§aPráce"));
        inv.setItem(13, item(Material.PAPER, "§bČlenové"));

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
