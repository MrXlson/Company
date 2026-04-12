package me.plugin.firma;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;

import net.milkbowl.vault.economy.Economy;

import java.util.*;

public class FirmaPlugin extends JavaPlugin implements CommandExecutor, Listener {

    private static Economy econ = null;

    private final Map<String, Company> companies = new HashMap<>();
    private final Map<UUID, String> invites = new HashMap<>();

    private double createPrice;

    @Override
    public void onEnable() {

        saveDefaultConfig();
        createPrice = getConfig().getDouble("company-create-price");

        if (!setupEconomy()) {
            getLogger().severe("Vault nenalezen!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getCommand("firma").setExecutor(this);
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) return false;

        RegisteredServiceProvider<Economy> rsp =
                getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null) return false;

        econ = rsp.getProvider();
        return econ != null;
    }

    // ===== COMMAND =====
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;

        if (args.length == 0) {
            openMainGUI(p);
            return true;
        }

        // CREATE
        if (args[0].equalsIgnoreCase("create")) {
            if (args.length < 2) return true;

            String name = args[1];

            if (companies.containsKey(name)) {
                p.sendMessage("§cFirma existuje!");
                return true;
            }

            if (!econ.has(p, createPrice)) {
                p.sendMessage("§cNemáš peníze!");
                return true;
            }

            econ.withdrawPlayer(p, createPrice);

            companies.put(name, new Company(name, p.getUniqueId()));
            p.sendMessage("§aFirma vytvořena!");
        }

        // TOP
        if (args[0].equalsIgnoreCase("top")) {
            sendTop(p);
        }

        return true;
    }

    // ===== LEADERBOARD =====
    private void sendTop(Player p) {

        List<Company> list = new ArrayList<>(companies.values());

        list.sort((a, b) -> Double.compare(b.balance, a.balance));

        p.sendMessage("§6=== TOP FIREM ===");

        int i = 1;
        for (Company c : list) {
            p.sendMessage("§e" + i + ". §f" + c.name + " §7- " + c.balance + "$");

            if (i++ >= 10) break;
        }
    }

    // ===== GUI =====
    private void openMainGUI(Player p) {

        Company c = getCompany(p);
        if (c == null) {
            p.sendMessage("§cNemáš firmu!");
            return;
        }

        Inventory inv = Bukkit.createInventory(null, 27, "§8Firma");

        inv.setItem(11, createItem(Material.PAPER, "§eInfo",
                "§7Level: " + c.level,
                "§7XP: " + c.xp + "/" + (c.level * 100)));

        inv.setItem(13, createItem(Material.GOLD_INGOT, "§6Balance",
                "§7" + c.balance + "$"));

        p.openInventory(inv);
    }

    // ===== UTILS =====
    private Company getCompany(Player p) {
        for (Company c : companies.values()) {
            if (c.members.containsKey(p.getUniqueId())) return c;
        }
        return null;
    }

    private ItemStack createItem(Material m, String name, String... lore) {
        ItemStack i = new ItemStack(m);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(name);
        im.setLore(Arrays.asList(lore));
        i.setItemMeta(im);
        return i;
    }

    // ===== COMPANY =====
    static class Company {
        String name;
        Map<UUID, String> members = new HashMap<>();
        double balance = 0;

        int level = 1;
        int xp = 0;

        Company(String name, UUID owner) {
            this.name = name;
            members.put(owner, "OWNER");
        }
    }
}
