package me.plugin.firma;

import me.plugin.firma.command.FirmaCommand;
import me.plugin.firma.listener.InventoryListener;
import me.plugin.firma.listener.JobListener;
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

        // COMMAND
        getCommand("firma").setExecutor(new FirmaCommand(manager));

        // LISTENERS
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new JobListener(), this);

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
