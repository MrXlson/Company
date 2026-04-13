package me.plugin.firma;

import me.plugin.firma.listener.InventoryListener;
import me.plugin.firma.manager.FirmaManager;
import org.bukkit.plugin.java.JavaPlugin;

public class FirmaPlugin extends JavaPlugin {

    private static FirmaPlugin instance;
    private FirmaManager manager;

    @Override
    public void onEnable() {

        instance = this;

        // 📦 inicializace manageru
        manager = new FirmaManager();

        // 📡 registrace listeneru
        getServer().getPluginManager().registerEvents(new InventoryListener(manager), this);

        getLogger().info("FirmaPlugin byl zapnut!");
    }

    @Override
    public void onDisable() {
        getLogger().info("FirmaPlugin byl vypnut!");
    }

    // 🔹 přístup k instanci pluginu
    public static FirmaPlugin getInstance() {
        return instance;
    }

    // 🔹 přístup k manageru
    public FirmaManager getManager() {
        return manager;
    }
}
