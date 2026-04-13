package me.plugin.firma;

import me.plugin.firma.command.*;
import me.plugin.firma.listener.*;
import me.plugin.firma.manager.FirmaManager;
import me.plugin.firma.tasks.IncomeTask;
import me.plugin.firma.economy.EconomySetup;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class FirmaPlugin extends JavaPlugin {

    private FirmaManager manager;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        if (!EconomySetup.setup(this)) {
            getLogger().severe("Vault nenalezen!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        manager = new FirmaManager(this);

        getCommand("firma").setExecutor(new FirmaCommand(manager));
        getCommand("firmatop").setExecutor(new FirmaTopCommand(manager));

        getServer().getPluginManager().registerEvents(new FirmaListener(manager), this);
        getServer().getPluginManager().registerEvents(new ChatListener(manager), this);
        getServer().getPluginManager().registerEvents(new DamageListener(manager), this);
        getServer().getPluginManager().registerEvents(new QuestListener(manager), this);

        Bukkit.getScheduler().runTaskTimer(this,
                new IncomeTask(this, manager),
                20 * 60,
                20 * 60);
    }
}
