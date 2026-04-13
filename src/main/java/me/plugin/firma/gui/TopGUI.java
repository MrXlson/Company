package me.plugin.firma.gui;

import me.plugin.firma.manager.FirmaManager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class TopGUI {

    public static void open(Player player, FirmaManager manager) {

        Inventory inv = Bukkit.createInventory(null, 54, "§6Top Firmy");

        Map<String, Double> map = new HashMap<>();

        for (String f : manager.getPlugin().getConfig().getConfigurationSection("firma").getKeys(false)) {
            map.put(f, manager.getBalance(f));
        }

        List<Map.Entry<String, Double>> sorted = new ArrayList<>(map.entrySet());
        sorted.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        int slot = 10;

        for (Map.Entry<String, Double> e : sorted) {

            ItemStack item = new ItemStack(Material.GOLD_BLOCK);
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName("§e" + e.getKey());
            meta.setLore(Arrays.asList("§7Balance: " + e.getValue()));

            item.setItemMeta(meta);
            inv.setItem(slot++, item);

            if (slot >= 44) break;
        }

        player.openInventory(inv);
    }
}
