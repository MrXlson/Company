package me.plugin.firma.gui;

import me.plugin.firma.manager.FirmaManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

import java.util.*;

public class MembersGUI {

    public static void open(Player p, FirmaManager manager) {

        Inventory inv = Bukkit.createInventory(null, 27, "§aČlenové");

        String firma = manager.getFirma(p);
        if (firma == null) return;

        Map<UUID, String> members = manager.getMembers(firma);

        int slot = 10;

        for (UUID uuid : members.keySet()) {

            OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);

            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();

            meta.setOwningPlayer(op);
            meta.setDisplayName("§e" + op.getName());

            String role = manager.getRole(firma, uuid);

            meta.setLore(Arrays.asList(
                    "§7Role: §e" + role
            ));

            head.setItemMeta(meta);

            inv.setItem(slot, head);

            slot++;
            if (slot == 17) slot = 19;
        }

        // ➕ Přidat
        ItemStack add = new ItemStack(Material.LIME_WOOL);
        ItemMeta addM = add.getItemMeta();
        addM.setDisplayName("§aPřidat člena");
        add.setItemMeta(addM);
        inv.setItem(26, add);

        // ❌ Odebrat
        ItemStack remove = new ItemStack(Material.RED_WOOL);
        ItemMeta remM = remove.getItemMeta();
        remM.setDisplayName("§cOdebrat člena");
        remove.setItemMeta(remM);
        inv.setItem(25, remove);

        p.openInventory(inv);
    }
}
