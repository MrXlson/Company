package me.plugin.firma.listener;

import me.plugin.firma.FirmaPlugin;
import me.plugin.firma.manager.FirmaManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.BlockBreakEvent;

public class JobListener implements Listener {

    private final FirmaManager manager;

    public JobListener() {
        this.manager = FirmaPlugin.getInstance().getManager();
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {

        Player p = e.getPlayer();

        if (!manager.hasFirma(p.getUniqueId())) return;

        if (e.getBlock().getType() == Material.STONE) {

            manager.getFirma(p.getUniqueId()).addBalance(5);
            manager.getFirma(p.getUniqueId()).addXP(2);

            p.sendMessage("§a+5$ | +2 XP");
        }
    }
}
