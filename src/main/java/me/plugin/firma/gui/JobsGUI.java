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
        Inventory inv = Bukkit.createInventory(null, 27, "§aPráce");

        // ⛏️ Těžař
        ItemStack miner = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta minerMeta = miner.getItemMeta();
        minerMeta.setDisplayName("§7Těžař");
        minerMeta.setLore(Arrays.asList("§8Těž kámen", "§8a vydělávej"));
        miner.setItemMeta(minerMeta);
        inv.setItem(11, miner);

        // 🌲 Dřevorubec
        ItemStack wood = new ItemStack(Material.IRON_AXE);
        ItemMeta woodMeta = wood.getItemMeta();
        woodMeta.setDisplayName("§6Dřevorubec");
        woodMeta.setLore(Arrays.asList("§8Kácej stromy", "§8pro firmu"));
        wood.setItemMeta(woodMeta);
        inv.setItem(13, wood);

        // 🌾 Farmář
        ItemStack farm = new ItemStack(Material.WHEAT);
        ItemMeta farmMeta = farm.getItemMeta();
        farmMeta.setDisplayName("§eFarmář");
        farmMeta.setLore(Arrays.asList("§8Sklízej plodiny"));
        farm.setItemMeta(farmMeta);
        inv.setItem(15, farm);

        p.openInventory(inv);
    }
}
