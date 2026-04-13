package me.plugin.firma.gui;

import me.plugin.firma.company.Company;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

public class MembersGUI {

    public static void open(Player p, Company c) {

        Inventory inv = Bukkit.createInventory(null, 27, "§bČlenové");

        inv.setItem(13, new ItemStack(Material.PLAYER_HEAD));
        inv.setItem(26, new ItemStack(Material.BARRIER));

        p.openInventory(inv);
    }
}
