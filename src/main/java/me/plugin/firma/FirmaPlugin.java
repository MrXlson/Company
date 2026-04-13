package me.plugin.firma;

import me.plugin.firma.command.FirmaCommand;
import me.plugin.firma.listener.*;
import me.plugin.firma.manager.FirmaManager;

import org.bukkit.plugin.java.JavaPlugin;

public class FirmaPlugin extends JavaPlugin {

    private FirmaManager manager;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        manager = new FirmaManager(this);

        getCommand("firma").setExecutor(new FirmaCommand(manager));

        getServer().getPluginManager().registerEvents(new FirmaListener(manager), this);
        getServer().getPluginManager().registerEvents(new ChatListener(manager), this);
        getServer().getPluginManager().registerEvents(new QuestListener(manager), this);

        getLogger().info("BizCore V16 loaded!");
    }
}
