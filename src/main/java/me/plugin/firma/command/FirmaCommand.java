package me.plugin.firma.command;

import me.plugin.firma.gui.*;
import me.plugin.firma.manager.*;

import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class FirmaCommand implements CommandExecutor {

    private final FirmaManager manager;

    public FirmaCommand(FirmaManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (args.length == 0) {
            boolean has = manager.hasCompany(player.getUniqueId());
            String name = has ? manager.getCompany(player.getUniqueId()) : null;
            FirmaGUI.openMainGUI(player, has, name, manager);
            return true;
        }

        if (args[0].equalsIgnoreCase("top")) {
            TopGUI.open(player, manager);
            return true;
        }

        if (args[0].equalsIgnoreCase("shop")) {
            UpgradeGUI.open(player, manager);
            return true;
        }

        if (args[0].equalsIgnoreCase("war") && args.length > 1) {
            String my = manager.getCompany(player.getUniqueId());
            String target = args[1];

            WarManager.startWar(my, target);
            player.sendMessage("§cVyhlásil jsi válku!");
            return true;
        }

        return true;
    }
}
