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

import org.bukkit.configuration.file.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FirmaPlugin extends JavaPlugin implements CommandExecutor, Listener {

    private static Economy econ = null;

    private final Map<String, Company> companies = new HashMap<>();
    private final Map<UUID, String> invites = new HashMap<>();

    private File file;
    private FileConfiguration data;

    private double createPrice;

    @Override
    public void onEnable() {

        saveDefaultConfig();
        createPrice = getConfig().getDouble("company-create-price");

        setupFile();
        loadCompanies();

        if (!setupEconomy()) {
            getLogger().severe("Vault nenalezen!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getCommand("firma").setExecutor(this);
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        saveCompanies();
    }

    // ===== FILE =====
    private void setupFile() {
        file = new File(getDataFolder(), "companies.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            saveResource("companies.yml", false);
        }
        data = YamlConfiguration.loadConfiguration(file);
    }

    // ===== SAVE =====
    private void saveCompanies() {
        for (String name : companies.keySet()) {
            Company c = companies.get(name);

            data.set(name + ".balance", c.balance);
            data.set(name + ".level", c.level);
            data.set(name + ".xp", c.xp);

            List<String> members = new ArrayList<>();
            for (UUID uuid : c.members.keySet()) {
                members.add(uuid.toString() + ":" + c.members.get(uuid));
            }

            data.set(name + ".members", members);
        }

        try {
            data.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ===== LOAD =====
    private void loadCompanies() {
        if (data.getKeys(false) == null) return;

        for (String name : data.getKeys(false)) {

            Company c = new Company(name, null);

            c.balance = data.getDouble(name + ".balance");
            c.level = data.getInt(name + ".level");
            c.xp = data.getInt(name + ".xp");

            List<String> members = data.getStringList(name + ".members");

            for (String s : members) {
                String[] split = s.split(":");
                UUID uuid = UUID.fromString(split[0]);
                String role = split[1];

                c.members.put(uuid, role);
            }

            companies.put(name, c);
        }
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
            saveCompanies();

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

    private Company getCompany(Player p) {
        for (Company c : companies.values()) {
            if (c.members.containsKey(p.getUniqueId())) return c;
        }
        return null;
    }

    private ItemStack createItem(Material m, String name, String... lore) {
        ItemStack i = new ItemStack(m);
        ItemMeta im = i.getItemMeta();
        im.setDisplay
            }
}
