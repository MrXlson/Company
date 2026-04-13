package me.plugin.firma.gui;

import me.plugin.firma.company.Company;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import java.util.List;

public class BankGUI {

    public static void open(Player p, Company c) {

        Inventory inv = Bukkit.createInventory(null, 27, "§bBanka");

        inv.setItem(11, item(Material.GREEN_WOOL, "§aVložit"));
        inv.setItem(13, item(Material.RED_WOOL, "§cVybrat"));

        inv.setItem(22, item(Material.EMERALD, "§aBalance", "§7" + c.balance));

        p.openInventory(inv);
    }

    private static ItemStack item(Material m, String name, String... lore) {
        ItemStack i = new ItemStack(m);
        var meta = i.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(List.of(lore));
        i.setItemMeta(meta);
        return i;
    }
}
