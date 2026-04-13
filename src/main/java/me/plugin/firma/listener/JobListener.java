package me.plugin.firma.listener;

import me.plugin.firma.manager.FirmaManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.BlockBreakEvent;

public class JobListener implements Listener {

    private final FirmaManager manager;

    public JobListener(FirmaManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onMine(BlockBreakEvent e) {

        Player p = e.getPlayer();
        String firma = manager.getCompany(p.getUniqueId());

        if (firma == null) return;

        if (e.getBlock().getType() == Material.STONE) {

            double reward = 50 * manager.getMultiplier(firma);

            manager.addBalance(firma, reward);
            manager.addXP(firma, 10);
        }
    }
}
