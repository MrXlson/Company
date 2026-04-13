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

            // INFO
            ItemStack info = new ItemStack(Material.PAPER);
            ItemMeta im = info.getItemMeta();
            im.setDisplayName("§e" + name);
            im.setLore(Arrays.asList(
                    "§7Balance: §a" + manager.getBalance(name),
                    "§7Level: §b" + manager.getLevel(name),
                    "§7XP: §e" + manager.getXP(name)
            ));
            info.setItemMeta(im);

            // MEMBERS
            ItemStack members = new ItemStack(Material.CHEST);
            ItemMeta mm = members.getItemMeta();
            mm.setDisplayName("§6Členové");
            members.setItemMeta(mm);

            // SHOP
            ItemStack shop = new ItemStack(Material.EMERALD);
            ItemMeta sm = shop.getItemMeta();
            sm.setDisplayName("§aUpgrady");
            shop.setItemMeta(sm);

            // JOBS
            ItemStack jobs = new ItemStack(Material.IRON_PICKAXE);
            ItemMeta jm = jobs.getItemMeta();
            jm.setDisplayName("§aPráce");
            jobs.setItemMeta(jm);

            // QUESTS
            ItemStack quests = new ItemStack(Material.BOOK);
            ItemMeta qm = quests.getItemMeta();
            qm.setDisplayName("§bÚkoly");
            quests.setItemMeta(qm);

            // TOP
            ItemStack top = new ItemStack(Material.DIAMOND);
            ItemMeta tm = top.getItemMeta();
            tm.setDisplayName("§eTOP firmy");
            top.setItemMeta(tm);

            inv.setItem(10, info);
            inv.setItem(11, members);
            inv.setItem(12, shop);
            inv.setItem(14, jobs);
            inv.setItem(15, quests);
            inv.setItem(16, top);
        }

        p.openInventory(inv);
    }
}
