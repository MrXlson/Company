package me.plugin.firma.gui;

import me.plugin.firma.company.Company;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

public class BankGUI {

    public static void open(Player p, Company c) {

        Inventory inv = Bukkit.createInventory(null, 27, "§bBanka");

        inv.setItem(11, item(Material.GREEN_WOOL, "§a+100"));
        inv.setItem(13, item(Material.RED_WOOL, "§c-100"));
        inv.setItem(15, item(Material.BARRIER, "§cZpět"));

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
