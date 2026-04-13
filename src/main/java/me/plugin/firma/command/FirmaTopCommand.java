package me.plugin.firma.command;

import me.plugin.firma.manager.FirmaManager;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.*;

public class FirmaTopCommand implements CommandExecutor {

    private final FirmaManager manager;

    public FirmaTopCommand(FirmaManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        Map<String, Double> map = new HashMap<>();

        for (String f : manager.getPlugin().getConfig().getConfigurationSection("firma").getKeys(false)) {
            map.put(f, manager.getBalance(f));
        }

        map.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(10)
                .forEach(e ->
                        player.sendMessage("§e" + e.getKey() + " §7- §6" + e.getValue())
                );

        return true;
    }
}
