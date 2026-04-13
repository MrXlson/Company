package me.plugin.firma.tasks;

import me.plugin.firma.manager.FirmaManager;

import org.bukkit.plugin.java.JavaPlugin;

public class IncomeTask implements Runnable {

    private final JavaPlugin plugin;
    private final FirmaManager manager;

    public IncomeTask(JavaPlugin plugin, FirmaManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @Override
    public void run() {

        if (!plugin.getConfig().contains("firma")) return;

        for (String firma : plugin.getConfig().getConfigurationSection("firma").getKeys(false)) {

            int level = manager.getLevel(firma);
            double income = (level * 10) * manager.getMultiplier(firma);

            if (manager.getBalance(firma) < manager.getLimit(firma)) {
                manager.addBalance(firma, income);
                manager.addXP(firma, 10);
            }
        }
    }
}
