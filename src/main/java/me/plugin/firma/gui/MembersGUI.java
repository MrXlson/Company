package me.plugin.firma.gui;

import me.plugin.firma.manager.FirmaManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import java.util.UUID;

public class MembersGUI {

    public static void open(Player p, FirmaManager manager) {

        String f = manager.getCompany(p.getUniqueId());

        Inventory inv = Bukkit.createInventory(null, 54, "§6Členové");

        for (String s : manager.getMembers(f)) {
            Player member = Bukkit.getPlayer(UUID.fromString(s));

            if (member == null) continue;

            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            inv.addItem(head);
        }

        p.openInventory(inv);
    }
}
