package me.plugin.firma.listener;

import me.plugin.firma.company.CompanyManager;
import me.plugin.firma.gui.BankGUI;
import me.plugin.firma.gui.MainGUI;
import me.plugin.firma.gui.MembersGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIListener implements Listener {

    private final CompanyManager companyManager;

    public GUIListener(CompanyManager companyManager) {
        this.companyManager = companyManager;
    }

    @EventHandler
    public void click(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player p)) return;
        if (e.getCurrentItem() == null) return;

        String title = e.getView().getTitle();

        if (!title.contains("Firma") && !title.contains("Banka") && !title.contains("Členové")) return;

        e.setCancelled(true);

        switch (title) {

            case "§6Firma Menu" -> {
                switch (e.getCurrentItem().getType()) {
                    case GOLD_INGOT -> BankGUI.open(p);
                    case PLAYER_HEAD -> MembersGUI.open(p, companyManager.getCompany(p));
                }
            }

            case "§bBanka" -> MainGUI.open(p);
        }
    }
