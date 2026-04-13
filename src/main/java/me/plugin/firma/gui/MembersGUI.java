package me.plugin.firma.gui;

import me.plugin.firma.manager.FirmaManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.UUID;

public class MembersGUI {

    public static void open(Player p, FirmaManager manager) {

        Inventory inv = Bukkit.createInventory(null, 27, "§aČlenové");

        String firma = manager.getFirma(p);
        if (firma == null) {
            p.sendMessage("§cNemáš firmu!");
            return;
        }

        List<UUID> members = manager.getMembers(firma);

        int slot = 10;

        for (UUID uuid : members) {

            OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);

            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();

            meta.setOwningPlayer(op);
            meta.setDisplayName("§e" + op.getName());

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
