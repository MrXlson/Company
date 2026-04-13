package me.plugin.firma.listener;

import me.plugin.firma.*;
import me.plugin.firma.company.*;
import me.plugin.firma.gui.*;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIListener implements Listener {

    private final CompanyManager m;
    private final FirmaPlugin plugin;

    public GUIListener(CompanyManager m, FirmaPlugin plugin) {
        this.m = m;
        this.plugin = plugin;
    }

    @EventHandler
    public void click(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player p)) return;

        String t = e.getView().getTitle();

        if (!t.equals("§6BizCore") && !t.equals("§bBanka")) return;

        e.setCancelled(true);

        Company c = m.getCompany(p);

        switch (e.getCurrentItem().getType()) {
            case GOLD_INGOT -> BankGUI.open(p, c);
            case BARRIER -> p.closeInventory();
        }
    }
}
