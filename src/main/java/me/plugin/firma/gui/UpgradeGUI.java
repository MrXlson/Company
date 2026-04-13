package me.plugin.firma.gui;

import me.plugin.firma.manager.FirmaManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class UpgradeGUI {

    public static void open(Player p, FirmaManager manager) {

        Inventory inv = Bukkit.createInventory(null, 27, "§aUpgrady");

        String f = manager.getCompany(p.getUniqueId());

        ItemStack multi = new ItemStack(Material.EMERALD);
        ItemMeta m = multi.getItemMeta();
        m.setDisplayName("§aMultiplier");
        m.setLore(Arrays.asList(
                "§7Aktuální: " + manager.getMultiplier(f),
                "§7Cena: 1000"
        ));
        multi.setItemMeta(m);

        inv.setItem(13, multi);

        p.openInventory(inv);
    }
}
