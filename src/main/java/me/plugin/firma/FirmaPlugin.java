package me.plugin.firma;

import me.plugin.firma.command.FirmaCommand;
import me.plugin.firma.listener.ChatListener;
import me.plugin.firma.listener.FirmaListener;
import me.plugin.firma.listener.JobListener;
import me.plugin.firma.listener.QuestListener;
import me.plugin.firma.manager.FirmaManager;

import org.bukkit.plugin.java.JavaPlugin;

public class FirmaPlugin extends JavaPlugin {

    private FirmaManager manager;

    @Override
    public void onEnable() {

        // 📦 config
        saveDefaultConfig();

        // 🧠 manager
        manager = new FirmaManager(this);

        // ⚙️ command
        getCommand("firma").setExecutor(new FirmaCommand(manager));

        // 🎧 listenery
        getServer().getPluginManager().registerEvents(new FirmaListener(manager), this);
        getServer().getPluginManager().registerEvents(new ChatListener(manager), this);
        getServer().getPluginManager().registerEvents(new QuestListener(manager), this);
        getServer().getPluginManager().registerEvents(new JobListener(manager), this);

        getLogger().info("§aBizCore plně načten!");
    }

    @Override
    public void onDisable() {
        saveConfig();
        getLogger().info("§cBizCore vypnut!");
    }

    public FirmaManager getManager() {
        return manager;
    }
}
