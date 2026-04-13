package me.plugin.firma;

import me.plugin.firma.command.FirmaCommand;
import me.plugin.firma.listener.ChatListener;
import me.plugin.firma.listener.InventoryClickListener;
import me.plugin.firma.manager.FirmaManager;
import org.bukkit.plugin.java.JavaPlugin;

public class FirmaPlugin extends JavaPlugin {

    private FirmaManager firmaManager;

    @Override
    public void onEnable() {

        // ✅ Manager
        firmaManager = new FirmaManager(this);

        // ✅ Command
        getCommand("firma").setExecutor(new FirmaCommand(firmaManager));

        // ✅ Listenery
        getServer().getPluginManager().registerEvents(new ChatListener(firmaManager), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(firmaManager), this);

        getLogger().info("BizCore plugin zapnut!");
    }

    @Override
    public void onDisable() {
        getLogger().info("BizCore plugin vypnut!");
    }

    public FirmaManager getFirmaManager() {
        return firmaManager;
    }
}
