package me.plugin.firma;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.configuration.file.FileConfiguration;

import net.milkbowl.vault.economy.Economy;

import java.util.*;

public class FirmaPlugin extends JavaPlugin implements CommandExecutor, Listener {

    private static Economy econ = null;
    private Map<String, Company> companies = new HashMap<>();

    private int createPrice;
    private int withdrawAmount;
    private int salary;
    private int salaryInterval;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadSettings();
        loadCompanies();

        if (!setupEconomy()) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getCommand("firma").setExecutor(this);
        Bukkit.getPluginManager().registerEvents(this, this);

        // 💰 PLATY
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Company c : companies.values()) {
                for (UUID uuid : c.members.keySet()) {
                    Player p = Bukkit.getPlayer(uuid);
                    if (p != null) econ.depositPlayer(p, salary);
                }
            }
        }, 0, 20L * salaryInterval);
    }

    @Override
    public void onDisable() {
        saveCompanies();
    }

    private void loadSettings() {
        FileConfiguration c = getConfig();
        createPrice = c.getInt("firma.create-price");
        withdrawAmount = c.getInt("firma.withdraw-amount");
        salary = c.getInt("firma.salary");
        salaryInterval = c.getInt("firma.salary-interval");
    }

    // ===== COMMAND =====
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;

        if (args.length == 0) {
            openGUI(p);
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!p.hasPermission("firma.admin")) return true;

            reloadConfig();
            loadSettings();
            p.sendMessage("§aConfig reloadnut!");
            return true;
        }

        if (args[0].equalsIgnoreCase("create")) {
            if (args.length < 2) return true;

            if (!econ.has(p, createPrice)) {
                p.sendMessage("§cNemáš peníze!");
                return true;
            }

            econ.withdrawPlayer(p, createPrice);
            companies.put(args[1], new Company(args[1], p.getUniqueId()));

            p.sendMessage("§aFirma vytvořena!");
        }

        return true;
    }

    // ===== GUI =====
    private void openGUI(Player p) {
        Company c = getCompany(p);
        if (c == null) {
            p.sendMessage("§cNemáš firmu!");
            return;
        }

        Inventory inv = Bukkit.createInventory(null, 27, "§8Firma");

        inv.setItem(13, createItem(Material.GOLD_INGOT, "§6Balance",
                "§7" + c.balance + "$"));

        inv.setItem(15, createItem(Material.EMERALD,
                "§aVybrat " + withdrawAmount + "$"));

        p.openInventory(inv);
    }

    @EventHandler
    public void click(InventoryClickEvent e) {
        if (!e.getView().getTitle().equals("§8Firma")) return;

        e.setCancelled(true);

        Player p = (Player) e.getWhoClicked();
        Company c = getCompany(p);

        if (e.getSlot() == 15) {
            if (c.balance < withdrawAmount) return;

            c.balance -= withdrawAmount;
            econ.depositPlayer(p, withdrawAmount);

            openGUI(p);
        }
    }

    // ===== SAVE =====
    private void saveCompanies() {
        FileConfiguration c = getConfig();
        c.set("companies", null);

        for (String name : companies.keySet()) {
            Company comp = companies.get(name);

            c.set("companies." + name + ".balance", comp.balance);

            for (UUID u : comp.members.keySet()) {
                c.set("companies." + name + ".members." + u, comp.members.get(u));
            }
        }

        saveConfig();
    }

    private void loadCompanies() {
        FileConfiguration c = getConfig();

        if (!c.contains("companies")) return;

        for (String name : c.getConfigurationSection("companies").getKeys(false)) {
            Company comp = new Company(name, UUID.randomUUID());

            comp.balance = c.getDouble("companies." + name + ".balance");

            for (String u : c.getConfigurationSection("companies." + name + ".members").getKeys(false)) {
                comp.members.put(UUID.fromString(u),
                        c.getString("companies." + name + ".members." + u));
            }

            companies.put(name, comp);
        }
    }

    // ===== UTIL =====
    private ItemStack createItem(Material m, String name, String... lore) {
        ItemStack i = new ItemStack(m);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(name);
        im.setLore(Arrays.asList(lore));
        i.setItemMeta(im);
        return i;
    }

    private Company getCompany(Player p) {
        for (Company c : companies.values()) {
            if (c.members.containsKey(p.getUniqueId())) return c;
        }
        return null;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) return false;

        RegisteredServiceProvider<Economy> rsp =
                getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null) return false;

        econ = rsp.getProvider();
        return econ != null;
    }

    // ===== COMPANY =====
    static class Company {
        String name;
        Map<UUID, String> members = new HashMap<>();
        double balance = 500;

        Company(String name, UUID owner) {
            this.name = name;
            members.put(owner, "OWNER");
        }
    }
    }
