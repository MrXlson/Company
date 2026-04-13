package me.plugin.firma.command;

import me.plugin.firma.gui.FirmaGUI;
import me.plugin.firma.manager.*;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class FirmaCommand implements CommandExecutor {

    private final FirmaManager manager;

    public FirmaCommand(FirmaManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;

        if (args.length == 0) {
            FirmaGUI.openMainGUI(p, manager.hasCompany(p.getUniqueId()), manager.getCompany(p.getUniqueId()), manager);
            return true;
        }

        if (args[0].equalsIgnoreCase("invite") && args.length > 1) {
            Player target = Bukkit.getPlayer(args[1]);

            if (target == null) return true;

            String f = manager.getCompany(p.getUniqueId());
            InviteManager.sendInvite(target.getUniqueId(), f);

            target.sendMessage("§ePozvánka do firmy: " + f);
        }

        if (args[0].equalsIgnoreCase("accept")) {
            String f = InviteManager.getInvite(p.getUniqueId());

            if (f == null) return true;

            manager.addMember(f, p.getUniqueId());
            InviteManager.remove(p.getUniqueId());

            p.sendMessage("§aPřipojen!");
        }

        return true;
    }
}
