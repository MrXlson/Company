package me.plugin.firma.command;

import me.plugin.firma.manager.FirmaManager;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class FirmaCommand implements CommandExecutor {

    private final FirmaManager manager;

    public FirmaCommand(FirmaManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player p)) return true;

        if (args.length == 0) {
            p.sendMessage("§6/firma create <name>");
            p.sendMessage("§6/firma invite <hráč>");
            p.sendMessage("§6/firma accept");
            return true;
        }

        switch (args[0].toLowerCase()) {

            case "create":
                manager.createFirma(p.getUniqueId(), args[1]);
                p.sendMessage("§aFirma vytvořena!");
                break;

            case "invite":
                manager.invite(p.getUniqueId(), args[1]);
                p.sendMessage("§aPozvánka odeslána!");
                break;

            case "accept":
                manager.accept(p.getUniqueId());
                p.sendMessage("§aPřipojen do firmy!");
                break;

            case "kick":
                manager.kick(p.getUniqueId(), args[1]);
                p.sendMessage("§cHráč vyhozen!");
                break;
        }

        return true;
    }
}
