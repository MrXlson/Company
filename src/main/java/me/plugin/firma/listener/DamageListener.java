package me.plugin.firma.listener;

import me.plugin.firma.manager.*;

import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.Player;

public class DamageListener implements Listener {

    private final FirmaManager manager;

    public DamageListener(FirmaManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {

        if (!(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player)) return;

        Player d = (Player) e.getDamager();
        Player v = (Player) e.getEntity();

        String f1 = manager.getCompany(d.getUniqueId());
        String f2 = manager.getCompany(v.getUniqueId());

        if (f1 == null || f2 == null) return;

        if (WarManager.isInWar(f1) && WarManager.getEnemy(f1).equals(f2)) {
            e.setDamage(e.getDamage() * 1.5);
        }
    }
}
