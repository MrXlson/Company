package me.plugin.firma.gui;

import me.plugin.firma.manager.QuestManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class QuestGUI {

    public static void open(Player p, me.plugin.firma.manager.FirmaManager manager) {

        Inventory inv = Bukkit.createInventory(null, 27, "§dQuesty");

        ItemStack quest = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta m = quest.getItemMeta();

        int kills = QuestManager.getKills(p.getUniqueId());

        m.setDisplayName("§cKill Quest");
        m.setLore(Arrays.asList(
                "§7Zabij 10 hráčů",
                "§7Progress: " + kills + "/10",
                "§7Reward: 1000$ + XP"
        ));

        quest.setItemMeta(m);

        inv.setItem(13, quest);

        p.openInventory(inv);
    }
}
