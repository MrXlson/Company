package me.plugin.firma.gui;

import me.plugin.firma.manager.FirmaManager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class FirmaGUI {

    public static void openMainGUI(Player player, boolean hasCompany, String name, FirmaManager manager) {

        Inventory inv = Bukkit.createInventory(null, 27, "§6BizCore");

        if (!hasCompany) {
            ItemStack item = new ItemStack(Material.EMERALD);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§aZaložit firmu");
            item.setItemMeta(meta);
            inv.setItem(13, item);
        } else {

            ItemStack info = new ItemStack(Material.PAPER);
            ItemMeta im = info.getItemMeta();
            im.setDisplayName("§eFirma: " + name);
            im.setLore(Arrays.asList(
                    "§7Balance: " + manager.getBalance(name),
                    "§7Level: " + manager.getLevel(name)
            ));
            info.setItemMeta(im);

            inv.setItem(13, info);
        }

        player.openInventory(inv);
    }
}
