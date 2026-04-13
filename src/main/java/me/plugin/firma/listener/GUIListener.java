package me.plugin.firma.listener;

import me.plugin.firma.*;
import me.plugin.firma.company.*;
import me.plugin.firma.gui.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIListener implements Listener {

    private final CompanyManager manager;
    private final FirmaPlugin plugin;

    public GUIListener(CompanyManager manager, FirmaPlugin plugin) {
        this.manager = manager;
        this.plugin = plugin;
    }

    @EventHandler
    public void click(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player p)) return;

        String t = e.getView().getTitle();

        if (!t.equals("§6BizCore") && !t.equals("§bBanka")) return;

        e.setCancelled(true);

        if (e.getCurrentItem() == null) return;

        Company c = manager.getCompany(p);

        if (t.equals("§6BizCore")) {

            switch (e.getCurrentItem().getType()) {
                case GOLD_INGOT -> BankGUI.open(p, c);
            }
        }

        if (t.equals("§bBanka")) {

            double deposit = plugin.getConfig().getDouble("economy.deposit-amount");
            double withdraw = plugin.getConfig().getDouble("economy.withdraw-amount");

            switch (e.getCurrentItem().getType()) {

                case GREEN_WOOL -> {
                    if (plugin.getEconomy().getBalance(p) >= deposit) {
                        plugin.getEconomy().withdrawPlayer(p, deposit);
                        c.balance += deposit;
                    }
                    BankGUI.open(p, c);
                }

                case RED_WOOL -> {
                    if (c.balance >= withdraw) {
                        c.balance -= withdraw;
                        plugin.getEconomy().depositPlayer(p, withdraw);
                    }
                    BankGUI.open(p, c);
                }
            }
        }
    }
}
