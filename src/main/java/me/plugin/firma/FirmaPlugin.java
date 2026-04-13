package me.plugin.firma;

import me.plugin.firma.listener.FirmaListener;
import me.plugin.firma.listener.ChatListener;
import me.plugin.firma.manager.FirmaManager;
import org.bukkit.plugin.java.JavaPlugin;

public class FirmaPlugin extends JavaPlugin {

    private static FirmaPlugin instance;
    private FirmaManager manager;

    @Override
    public void onEnable() {
        instance = this;

        manager = new FirmaManager(this);

        getServer().getPluginManager().registerEvents(new FirmaListener(this, manager), this);
        getServer().getPluginManager().registerEvents(new ChatListener(manager), this);

        getLogger().info("BizCore zapnut!");
    }

    public static FirmaPlugin getInstance() {
        return instance;
    }

    public FirmaManager getManager() {
        return manager;
    }
}
