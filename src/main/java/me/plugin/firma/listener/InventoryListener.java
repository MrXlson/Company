package me.plugin.firma.listener;

import me.plugin.firma.gui.*;
import me.plugin.firma.manager.FirmaManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {

    private final FirmaManager manager;

    public InventoryListener(FirmaManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player)) return;

        Player p = (Player) e.getWhoClicked();
        String title = e.getView().getTitle();

        if (title == null) return;

        // ❌ zákaz brání itemů z GUI
        if (title.contains("BizCore") || title.contains("Práce") || title.contains("Členové")) {
            e.setCancelled(true);
        }

        if (e.getCurrentItem() == null) return;

        // =========================
        // 🏠 MAIN GUI
        // =========================
        if (title.equals("§6BizCore")) {
