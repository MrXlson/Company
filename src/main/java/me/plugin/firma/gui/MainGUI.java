package me.plugin.firma.gui;

import me.plugin.firma.company.Company;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

public class MainGUI {

    public static void open(Player p, Company c) {

        Inventory inv = Bukkit.createInventory(null, 45, "§6BizCore");

        inv.setItem(10, item(Material.GOLD_INGOT, "§eBanka"));
        inv.setItem(12, item(Material.PLAYER_HEAD, "§bČlenové"));
        inv.setItem(24, item(Material.DIAMOND, "§bTOP"));
        inv.setItem(40, item(Material.BARRIER, "§cZavřít"));

        p.openInventory(inv);
    }

    private static ItemStack item(Material m, String n) {
        ItemStack i = new ItemStack(m);
        var meta = i.getItemMeta();
        meta.setDisplayName(n);
        i.setItemMeta(meta);
        return i;
    }
}
