package me.plugin.firma.gui;

import me.plugin.firma.company.Company;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import java.util.List;

public class MainGUI {

    public static void open(Player p, Company c) {

        if (c == null) return;

        Inventory inv = Bukkit.createInventory(null, 45, "§6BizCore");

        fill(inv);

        inv.setItem(20, item(Material.GOLD_INGOT, "§eBanka", "§7Balance: §a" + c.balance));
        inv.setItem(22, item(Material.EMERALD, "§aInfo", "§7Level: §e" + c.level, "§7XP: §b" + c.xp));
        inv.setItem(24, item(Material.PLAYER_HEAD, "§bČlenové", "§7Počet: §e" + c.members.size()));

        inv.setItem(40, item(Material.BARRIER, "§cZavřít"));

        p.openInventory(inv);
    }

    private static void fill(Inventory inv) {
        ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        var meta = glass.getItemMeta();
        meta.setDisplayName(" ");
        glass.setItemMeta(meta);

        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, glass);
        }
    }

    private static ItemStack item(Material m, String name, String... lore) {
        ItemStack i = new ItemStack(m);
        var meta = i.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(List.of(lore));
        i.setItemMeta(meta);
        return i;
    }
}
