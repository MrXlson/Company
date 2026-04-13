package me.plugin.firma.gui;

import me.plugin.firma.company.Company;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class MembersGUI {

    public static void open(Player p, Company c) {

        if (c == null) return;

        Inventory inv = Bukkit.createInventory(null, 27, "§bČlenové");

        int i = 0;

        for (UUID u : c.members.keySet()) {

            OfflinePlayer op = Bukkit.getOfflinePlayer(u);

            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();

            meta.setDisplayName("§e" + op.getName());
            meta.setOwningPlayer(op);

            head.setItemMeta(meta);

            inv.setItem(i++, head);
        }

        p.openInventory(inv);
    }
}
