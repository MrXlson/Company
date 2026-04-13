package me.plugin.firma.gui;

import me.plugin.firma.manager.FirmaManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class JobsGUI {

    public static void open(Player p, FirmaManager manager) {

        Inventory inv = Bukkit.createInventory(null, 27, "§bPráce");

        ItemStack miner = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta m = miner.getItemMeta();

        m.setDisplayName("§aMiner");
        m.setLore(Arrays.asList(
                "§7Kopej bloky a vydělávej",
                "§7STONE = peníze + XP"
        ));

        miner.setItemMeta(m);

        inv.setItem(13, miner);

        p.openInventory(inv);
    }
}
