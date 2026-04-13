package me.plugin.firma;

import me.plugin.firma.command.FirmaCommand;
import me.plugin.firma.manager.FirmaManager;
import org.bukkit.plugin.java.JavaPlugin;

public class FirmaPlugin extends JavaPlugin {

    private static FirmaPlugin instance;
    private FirmaManager manager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        manager = new FirmaManager(this);
        manager.load();

        getCommand("firma").setExecutor(new FirmaCommand(manager));

        getLogger().info("BizCore PRO zapnut!");
    }

    @Override
    public void onDisable() {
        manager.save();
    }

    public static FirmaPlugin getInstance() {
        return instance;
    }

    public FirmaManager getManager() {
        return manager;
    }
}
