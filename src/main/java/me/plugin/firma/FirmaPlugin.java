package me.plugin.firma;

import me.plugin.firma.listener.ChatListener;
import me.plugin.firma.listener.FirmaListener;
import me.plugin.firma.manager.FirmaManager;
import org.bukkit.plugin.java.JavaPlugin;

public class FirmaPlugin extends JavaPlugin {

    private FirmaManager manager;

    @Override
    public void onEnable() {

        manager = new FirmaManager();

        getServer().getPluginManager().registerEvents(new FirmaListener(manager), this);
        getServer().getPluginManager().registerEvents(new ChatListener(manager), this);
    }

    public FirmaManager getManager() {
        return manager;
    }
}
