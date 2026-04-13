package me.plugin.firma.listener;

import me.plugin.firma.manager.FirmaManager;
import me.plugin.firma.gui.FirmaGUI;
import me.plugin.firma.gui.JobsGUI;
import me.plugin.firma.gui.UpgradeGUI;
import me.plugin.firma.gui.QuestGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class FirmaListener implements Listener {

    private final FirmaManager manager;

    public FirmaListener(FirmaManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player)) return;

        Player p = (Player) e.getWhoClicked();

        // 🔥 NÁZEV HLAVNÍHO GUI
        if (e.getView().getTitle().equals("§6BizCore")) {

            e.setCancelled(true); // ❌ zakáže brání itemů

            if (e.getCurrentItem() == null) return;

            switch (e.getSlot()) {

                case 11:
                    JobsGUI.open(p, manager);
                    break;

                case 13:
                    UpgradeGUI.open(p, manager);
                    break;

                case 15:
                    QuestGUI.open(p, manager);
                    break;

                case 22:
                    p.sendMessage("§eČlenové zatím nejsou hotové");
                    break;
            }
        }

        // 🔥 JOBS GUI
        if (e.getView().getTitle().equals("§bPráce")) {

            e.setCancelled(true);

            if (e.getCurrentItem() == null) return;

            p.sendMessage("§aVybral jsi práci!");
        }

        // 🔥 QUEST GUI
        if (e.getView().getTitle().equals("§dQuesty")) {

            e.setCancelled(true);

            if (e.getCurrentItem() == null) return;

            p.sendMessage("§dQuest kliknut!");
        }

        // 🔥 UPGRADE GUI
        if (e.getView().getTitle().equals("§eUpgrade")) {

            e.setCancelled(true);

            if (e.getCurrentItem() == null) return;

            p.sendMessage("§eUpgrade kliknut!");
        }
    }
}
