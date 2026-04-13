package me.plugin.firma;

import me.plugin.firma.listener.InventoryListener;
import me.plugin.firma.manager.FirmaManager;
import org.bukkit.plugin.java.JavaPlugin;

public class FirmaPlugin extends JavaPlugin {

    private FirmaManager manager;

    @Override
    public void onEnable() {

        manager = new FirmaManager(this);

        getServer().getPluginManager().registerEvents(
                new InventoryListener(manager),
                this
        );

        getLogger().info("Plugin běží");
    }

    public FirmaManager getManager() {
        return manager;
    }
}
