package me.plugin.firma.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MainGUI {

    public static void open(Player p) {

        Inventory inv = Bukkit.createInventory(null, 45, "§6Firma Menu");

        inv.setItem(10, item(Material.GOLD_INGOT, "§eBanka"));
        inv.setItem(12, item(Material.PLAYER_HEAD, "§bČlenové"));
        inv.setItem(14, item(Material.PAPER, "§dInvite"));
        inv.setItem(16, item(Material.ANVIL, "§cNastavení"));
        inv.setItem(22, item(Material.EMERALD, "§aTOP"));
        inv.setItem(40, item(Material.BARRIER, "§cZavřít"));

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
