package me.plugin.firma.gui;

import me.plugin.firma.manager.FirmaManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class MembersGUI {

    public static void open(Player p, FirmaManager manager) {
        Inventory inv = Bukkit.createInventory(null, 27, "§aČlenové");

        // ➕ Přidat člena
        ItemStack add = new ItemStack(Material.LIME_WOOL);
        ItemMeta addMeta = add.getItemMeta();
        addMeta.setDisplayName("§aPřidat člena");
        add.setItemMeta(addMeta);
        inv.setItem(11, add);

        // ➖ Odebrat člena
        ItemStack remove = new ItemStack(Material.RED_WOOL);
        ItemMeta removeMeta = remove.getItemMeta();
        removeMeta.setDisplayName("§cOdebrat člena");
        remove.setItemMeta(removeMeta);
        inv.setItem(15, remove);

        // 👤 Seznam členů
        int slot = 18;

        for (UUID uuid : manager.getMembers(p.getUniqueId())) {
            Player target = Bukkit.getPlayer(uuid);
            if (target == null) continue;

            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            ItemMeta meta = head.getItemMeta();

            meta.setDisplayName("§f" + target.getName());
            head.setItemMeta(meta);

            inv.setItem(slot++, head);
        }

        p.openInventory(inv);
    }
}
