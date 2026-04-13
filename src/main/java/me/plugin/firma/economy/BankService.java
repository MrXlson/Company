package me.plugin.firma.economy;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

public class BankService {

    private final Economy econ;

    public BankService(Economy econ) {
        this.econ = econ;
    }

    public boolean deposit(Player p, double amount) {
        if (econ == null) return false;

        if (econ.getBalance(p) < amount) {
            p.sendMessage("§cNemáš dost peněz!");
            return false;
        }

        econ.withdrawPlayer(p, amount);
        return true;
    }

    public void withdraw(Player p, double amount) {
        if (econ == null) return;

        econ.depositPlayer(p, amount);
    }
}
