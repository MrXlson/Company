package me.plugin.firma.gui;

import me.plugin.firma.manager.FirmaManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class FirmaGUI {

    public static void openMainGUI(Player p, boolean has, String name, FirmaManager manager) {

        Inventory inv = Bukkit.createInventory(null, 27, "§6BizCore");

        if (!has) {
            ItemStack create = new ItemStack(Material.EMERALD);
            ItemMeta cm = create.getItemMeta();
            cm.setDisplayName("§aZaložit firmu");
            create.setItemMeta(cm);
            inv.setItem(13, create);
        } else {

            ItemStack info = new ItemStack(Material.PAPER);
            ItemMeta im = info.getItemMeta();
            im.setDisplayName("§e" + name);
            im.setLore(Arrays.asList(
                    "§7Balance: §a" + manager.getBalance(name),
                    "§7Level: §b" + manager.getLevel(name),
                    "§7XP: §e" + manager.getXP(name)
            ));
            info.setItemMeta(im);

            inv.setItem(13, info);
        }

        p.openInventory(inv);
    }
}
