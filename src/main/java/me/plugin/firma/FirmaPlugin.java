package me.plugin.firma;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.configuration.file.*;

import java.io.File;
import java.util.*;

public class FirmaPlugin extends JavaPlugin implements CommandExecutor, Listener {

    private static Economy econ;

    private final Map<String, Company> companies = new HashMap<>();
    private final Map<UUID, String> invites = new HashMap<>();
    private final Map<Player, UUID> managingPlayer = new HashMap<>();

    private File file;
    private FileConfiguration data;

    @Override
    public void onEnable() {

        setupFile();
        loadCompanies();
        setupEconomy();

        if (getCommand("firma") != null)
            getCommand("firma").setExecutor(this);

        Bukkit.getPluginManager().registerEvents(this, this);

        startSalaryTask();
        startAutoSave();

        getLogger().info("FirmaPlugin enabled");
    }

    // ================= FILE =================
    private void setupFile() {

        file = new File(getDataFolder(), "data.yml");

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        data = YamlConfiguration.loadConfiguration(file);
    }

    private void saveCompanies() {

        data.set("companies", null);

        for (Company c : companies.values()) {

            String path = "companies." + c.name;

            data.set(path + ".owner", c.owner.toString());
            data.set(path + ".balance", c.balance);
            data.set(path + ".level", c.level);
            data.set(path + ".xp", c.xp);

            for (UUID u : c.members.keySet()) {
                data.set(path + ".members." + u, c.members.get(u));
                data.set(path + ".jobs." + u, c.jobs.getOrDefault(u, "NONE"));
            }
        }

        try {
            data.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCompanies() {

        if (!data.contains("companies")) return;

        var section = data.getConfigurationSection("companies");
        if (section == null) return;

        for (String name : section.getKeys(false)) {

            String path = "companies." + name;

            String ownerStr = data.getString(path + ".owner");
            if (ownerStr == null) continue;

            UUID owner = UUID.fromString(ownerStr);

            Company c = new Company(name, owner);

            c.balance = data.getDouble(path + ".balance");
            c.level = data.getInt(path + ".level");
            c.xp = data.getInt(path + ".xp");

            if (data.contains(path + ".members")) {
                var members = data.getConfigurationSection(path + ".members");
                if (members != null) {
                    for (String u : members.getKeys(false)) {
                        c.members.put(UUID.fromString(u), members.getString(u));
                    }
                }
            }

            if (data.contains(path + ".jobs")) {
                var jobs = data.getConfigurationSection(path + ".jobs");
                if (jobs != null) {
                    for (String u : jobs.getKeys(false)) {
                        c.jobs.put(UUID.fromString(u), jobs.getString(u));
                    }
                }
            }

            companies.put(name, c);
        }
    }

    private void startAutoSave() {
        new BukkitRunnable() {
            @Override
            public void run() {
                saveCompanies();
            }
        }.runTaskTimer(this, 20 * 300, 20 * 300);
    }

    // ================= COMMAND =================
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player p)) return true;

        Company c = getCompany(p);

        if (args.length == 0) {
            openMainGUI(p, c);
            return true;
        }

        switch (args[0].toLowerCase()) {

            case "create" -> {
                if (c != null) {
                    p.sendMessage("§cUž jsi ve firmě!");
                    return true;
                }

                if (args.length < 2) return true;

                Company nc = new Company(args[1], p.getUniqueId());
                companies.put(args[1], nc);

                saveCompanies();
                p.sendMessage("§aFirma vytvořena!");
            }

            case "leave" -> {
                if (c == null) return true;
                if (c.isOwner(p)) {
                    p.sendMessage("§cOwner nemůže odejít!");
                    return true;
                }

                c.members.remove(p.getUniqueId());
                c.jobs.remove(p.getUniqueId());

                saveCompanies();
                p.sendMessage("§cOdešel jsi z firmy");
            }

            case "deposit" -> {
                if (c == null || args.length < 2) return true;

                double amount = Double.parseDouble(args[1]);

                if (econ != null) {
                    econ.withdrawPlayer(p, amount);
                    c.balance += amount;
                    p.sendMessage("§aVloženo: " + amount);
                }
            }

            case "withdraw" -> {
                if (c == null || args.length < 2) return true;
                if (!c.isOwner(p)) return true;

                double amount = Double.parseDouble(args[1]);

                if (c.balance < amount) {
                    p.sendMessage("§cMálo peněz!");
                    return true;
                }

                c.balance -= amount;

                if (econ != null)
                    econ.depositPlayer(p, amount);

                p.sendMessage("§aVybráno: " + amount);
            }

            case "top" -> openTopGUI(p);
        }

        return true;
    }

    // ================= GUI =================
    private void openMainGUI(Player p, Company c) {

        Inventory inv = Bukkit.createInventory(null, 45, "§6Firma Menu");

        inv.setItem(10, item(Material.GOLD_INGOT, "§eBanka", "Klikni"));
        inv.setItem(12, item(Material.PLAYER_HEAD, "§bČlenové", "Správa"));
        inv.setItem(18, item(Material.EMERALD, "§aTOP", ""));
        inv.setItem(40, item(Material.BARRIER, "§cZavřít", ""));

        p.openInventory(inv);
    }

    private void openMembersGUI(Player p, Company c) {

        Inventory inv = Bukkit.createInventory(null, 27, "§bČlenové");

        int i = 0;

        for (UUID u : c.members.keySet()) {

            OfflinePlayer op = Bukkit.getOfflinePlayer(u);

            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            ItemMeta meta = head.getItemMeta();

            meta.setDisplayName("§e" + op.getName());
            meta.setLore(List.of(
                    "§7Role: " + c.members.get(u),
                    "§7Job: " + c.jobs.getOrDefault(u, "NONE"),
                    "§aKlikni"
            ));

            head.setItemMeta(meta);
            inv.setItem(i++, head);
        }

        p.openInventory(inv);
    }

    private void openManagePlayerGUI(Player p, Player target) {

        Inventory inv = Bukkit.createInventory(null, 27, "§eSpráva hráče");

        inv.setItem(11, item(Material.BARRIER, "§cKick", ""));
        inv.setItem(15, item(Material.DIAMOND_PICKAXE, "§dJob", ""));

        p.openInventory(inv);
    }

    private void openJobGUI(Player p) {

        Inventory inv = Bukkit.createInventory(null, 27, "§dVyber job");

        String[] jobs = {"MINER", "BUILDER", "FARMER"};

        for (int i = 0; i < jobs.length; i++) {
            inv.setItem(i, item(Material.DIAMOND_PICKAXE, "§e" + jobs[i], ""));
        }

        p.openInventory(inv);
    }

    private void openTopGUI(Player p) {

        Inventory inv = Bukkit.createInventory(null, 27, "§6TOP Firmy");

        List<Company> sorted = new ArrayList<>(companies.values());
        sorted.sort((a, b) -> Double.compare(b.balance, a.balance));

        int i = 0;

        for (Company c : sorted) {

            ItemStack it = new ItemStack(Material.GOLD_BLOCK);
            ItemMeta meta = it.getItemMeta();

            meta.setDisplayName("§e#" + (i + 1) + " " + c.name);
            meta.setLore(List.of("§aBalance: " + c.balance));

            it.setItemMeta(meta);
            inv.setItem(i++, it);

            if (i >= 27) break;
        }

        p.openInventory(inv);
    }

    // ================= CLICK =================
    @EventHandler
    public void click(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player p)) return;

        String t = e.getView().getTitle();
        ItemStack it = e.getCurrentItem();

        if (it == null || it.getItemMeta() == null) return;

        Company c = getCompany(p);

        if (t.equals("§6Firma Menu")) {

            e.setCancelled(true);

            switch (it.getType()) {

                case GOLD_INGOT -> {
                    if (c != null) {
                        p.sendMessage("§aBalance: " + c.balance);
                        p.sendMessage("/firma deposit <částka>");
                        p.sendMessage("/firma withdraw <částka>");
                    }
                }

                case PLAYER_HEAD -> {
                    if (c != null)
                        openMembersGUI(p, c);
                }

                case EMERALD -> openTopGUI(p);

                case BARRIER -> p.closeInventory();
            }
        }

        if (t.equals("§bČlenové")) {

            e.setCancelled(true);

            String name = it.getItemMeta().getDisplayName().replace("§e", "");
            Player target = Bukkit.getPlayer(name);

            if (target == null || c == null) return;

            if (!c.isOwner(p)) return;

            managingPlayer.put(p, target.getUniqueId());
            openManagePlayerGUI(p, target);
        }

        if (t.equals("§eSpráva hráče")) {

            e.setCancelled(true);

            UUID targetId = managingPlayer.get(p);
            if (targetId == null || c == null) return;

            if (it.getType() == Material.BARRIER) {

                c.members.remove(targetId);
                c.jobs.remove(targetId);

                p.sendMessage("§cHráč vyhozen");
            }

            if (it.getType() == Material.DIAMOND_PICKAXE) {
                openJobGUI(p);
            }
        }

        if (t.equals("§dVyber job")) {

            e.setCancelled(true);

            UUID targetId = managingPlayer.get(p);
            if (targetId == null || c == null) return;

            String job = it.getItemMeta().getDisplayName().replace("§e", "");

            c.jobs.put(targetId, job);

            p.sendMessage("§aJob nastaven: " + job);
        }

        if (t.equals("§6TOP Firmy")) {
            e.setCancelled(true);
        }
    }

    // ================= COMPANY =================
    static class Company {

        String name;
        UUID owner;

        Map<UUID, String> members = new HashMap<>();
        Map<UUID, String> jobs = new HashMap<>();

        double balance = 0;
        int level = 1;
        int xp = 0;

        Company(String name, UUID owner) {
            this.name = name;
            this.owner = owner;

            members.put(owner, "OWNER");
            jobs.put(owner, "OWNER");
        }

        boolean isOwner(Player p) {
            return owner.equals(p.getUniqueId());
        }
    }

    // ================= HELPERS =================
    private Company getCompany(Player p) {
        for (Company c : companies.values()) {
            if (c.members.containsKey(p.getUniqueId())) return c;
        }
        return null;
    }

    private ItemStack item(Material m, String name, String lore) {
        ItemStack i = new ItemStack(m);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(List.of(lore));
        i.setItemMeta(meta);
        return i;
    }

    private void setupEconomy() {
        var rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp != null) econ = rsp.getProvider();
    }

    private void startSalaryTask() {
        new BukkitRunnable() {
            @Override
            public void run() {

                for (Company c : companies.values()) {

                    for (UUID u : c.members.keySet()) {

                        Player p = Bukkit.getPlayer(u);
                        if (p == null) continue;

                        double pay = 50 + (c.level * 10);

                        if (econ != null)
                            econ.depositPlayer(p, pay);

                        c.xp += 10;

                        if (c.xp >= c.level * 100) {
                            c.xp = 0;
                            c.level++;
                        }
                    }
                }
            }
        }.runTaskTimer(this, 0, 20 * 60);
    }
            }
