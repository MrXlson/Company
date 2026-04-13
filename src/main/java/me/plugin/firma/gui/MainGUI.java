package me.plugin.firma.gui;

import me.plugin.firma.manager.FirmaManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MainGUI {

    public static void open(Player p, FirmaManager manager) {
        Inventory inv = Bukkit.createInventory(null, 27, "§6BizCore");

        // 🔨 PRÁCE
        ItemStack jobs = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta jobsMeta = jobs.getItemMeta();
        jobsMeta.setDisplayName("§aPráce");
        jobs.setItemMeta(jobsMeta);
        inv.setItem(11, jobs);

        // ⬆️ UPGRADE
        ItemStack upgrade = new ItemStack(Material.EMERALD);
        ItemMeta upgradeMeta = upgrade.getItemMeta();
        upgradeMeta.setDisplayName("§bUpgrade");
        upgrade.setItemMeta(upgradeMeta);
        inv.setItem(13, upgrade);

        // 📜 QUESTY
        ItemStack quests = new ItemStack(Material.BOOK);
        ItemMeta questMeta = quests.getItemMeta();
        questMeta.setDisplayName("§eQuesty");
        quests.setItemMeta(questMeta);
        inv.setItem(15, quests);

        // 👥 ČLENOVÉ
        ItemStack members = new ItemStack(Material.PAPER);
        ItemMeta membersMeta = members.getItemMeta();
        membersMeta.setDisplayName("§dČlenové");
        members.setItemMeta(membersMeta);
        inv.setItem(22, members);

        p.openInventory(inv);
    }
}
