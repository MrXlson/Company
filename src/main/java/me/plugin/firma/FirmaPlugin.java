package me.plugin.firma;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class FirmaPlugin extends JavaPlugin implements CommandExecutor, Listener {

    private static Economy econ;

    private final Map<String, Company> companies = new HashMap<>();
    private final Map<UUID, String> invites = new HashMap<>();

    @Override
    public void onEnable() {

        saveDefaultConfig();
        setupEconomy();

        getCommand("firma").setExecutor(this);
        Bukkit.getPluginManager().registerEvents(this, this);

        startSalaryTask();
        startMonthlyTax();

        getLogger().info("FirmaPlugin enabled");
    }

    // ================= COMMAND =================
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player p)) return true;

        Company c = getPlayerCompanyObj(p.getUniqueId());

        if (args.length == 0) {
            openMainGUI(p, c);
            return true;
        }

        switch (args[0].toLowerCase()) {

            case "create" -> {
                if (args.length < 2) return true;

                String name = args[1].toLowerCase();

                if (companies.containsKey(name)) {
                    p.sendMessage("§cFirma existuje!");
                    return true;
                }

                Company newC = new Company(name, p.getUniqueId());
                companies.put(name, newC);

                p.sendMessage("§aFirma vytvořena!");
            }

            case "invite" -> {
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) return true;

                String comp = getPlayerCompany(p.getUniqueId());
                invites.put(target.getUniqueId(), comp);

                target.sendMessage("§aPozvánka do firmy!");
            }

            case "accept" -> {
                String compName = invites.remove(p.getUniqueId());
                if (compName == null) return true;

                Company co = companies.get(compName);
                if (co == null) return true;

                co.members.put(p.getUniqueId(), "MEMBER");
                p.sendMessage("§aPřijat do firmy!");
            }
        }

        return true;
    }

    // ================= MAIN GUI =================
    private void openMainGUI(Player p, Company c) {

        Inventory inv = Bukkit.createInventory(null, 45, "§6Firma Menu");

        inv.setItem(10, item(Material.GOLD_INGOT, "§eZůstatek", "§a" + (c == null ? 0 : c.balance)));
        inv.setItem(12, item(Material.PLAYER_HEAD, "§bČlenové", "Klikni"));
        inv.setItem(14, item(Material.PAPER, "§aInvite", "Klikni"));
        inv.setItem(16, item(Material.EXPERIENCE_BOTTLE, "§6Level", "§e" + (c == null ? 1 : c.level)));
        inv.setItem(20, item(Material.COMPARATOR, "§c% Nastavení", "Klikni"));
        inv.setItem(22, item(Material.DIAMOND_PICKAXE, "§dPráce", c == null ? "NONE" : c.job));
        inv.setItem(40, item(Material.BARRIER, "§cZavřít", ""));

        p.openInventory(inv);
    }

    // ================= CLICK =================
    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player p)) return;

        String title = e.getView().getTitle();

        // ===== MAIN MENU =====
        if (title.equals("§6Firma Menu")) {

            e.setCancelled(true);

            ItemStack item = e.getCurrentItem();
            if (item == null || item.getType() == Material.AIR) return;

            Company c = getPlayerCompanyObj(p.getUniqueId());

            switch (item.getType()) {

                case PAPER -> openInviteGUI(p);

                case PLAYER_HEAD -> {
                    if (c != null) showMembers(p, c);
                    else p.sendMessage("§cNemáš firmu!");
                }

                case GOLD_INGOT -> {
                    if (c != null) p.sendMessage("§aBalance: " + c.balance);
                }

                case EXPERIENCE_BOTTLE -> {
                    if (c != null) p.sendMessage("§6Level: " + c.level);
                }

                case COMPARATOR -> {
                    if (c != null) openPercentGUI(p, c);
                }

                case DIAMOND_PICKAXE -> {
                    if (c != null) p.sendMessage("§dJob: " + c.job);
                }

                case BARRIER -> p.closeInventory();
            }
        }

        // ===== INVITE MENU =====
        if (title.equals("§aInvite menu")) {

            e.setCancelled(true);

            ItemStack item = e.getCurrentItem();
            if (item == null || item.getType() != Material.PLAYER_HEAD) return;

            String targetName = item.getItemMeta().getDisplayName();
            Player target = Bukkit.getPlayer(targetName);

            if (target == null) {
                p.sendMessage("§cHráč není online!");
                return;
            }

            String comp = getPlayerCompany(p.getUniqueId());
            if (comp == null) {
                p.sendMessage("§cNemáš firmu!");
                return;
            }

            invites.put(target.getUniqueId(), comp);
            target.sendMessage("§aPozvánka do firmy!");
            p.sendMessage("§aPozvánka odeslána!");
        }

        // ===== PERCENT MENU =====
        if (title.equals("§c% nastavení")) {

            e.setCancelled(true);

            ItemStack item = e.getCurrentItem();
            if (item == null || item.getType() != Material.COMPARATOR) return;

            String percent = item.getItemMeta().getDisplayName();

            p.sendMessage("§aNastaveno: " + percent);
        }
    }

    // ================= INVITE GUI =================
    private void openInviteGUI(Player p) {

        Inventory inv = Bukkit.createInventory(null, 27, "§aInvite menu");

        int i = 0;
        for (Player pl : Bukkit.getOnlinePlayers()) {
            inv.setItem(i++, item(Material.PLAYER_HEAD, pl.getName(), "Klikni pro invite"));
        }

        p.openInventory(inv);
    }

    // ================= PERCENT GUI =================
    private void openPercentGUI(Player p, Company c) {

        Inventory inv = Bukkit.createInventory(null, 9, "§c% nastavení");

        int[] vals = {10, 25, 50, 75, 90};

        for (int i = 0; i < vals.length; i++) {
            int v = vals[i];
            inv.setItem(i, item(Material.COMPARATOR, v + "%", "Firma cut"));
        }

        p.openInventory(inv);
    }

    // ================= MEMBERS =================
    private void showMembers(Player p, Company c) {

        p.sendMessage("§bČlenové:");

        c.members.forEach((u, r) -> {
            p.sendMessage(" - " + Bukkit.getOfflinePlayer(u).getName() + " §7(" + r + ")");
        });
    }

    // ================= ITEM =================
    private ItemStack item(Material m, String name, String lore) {
        ItemStack i = new ItemStack(m);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(List.of(lore));
        i.setItemMeta(meta);
        return i;
    }

    // ================= COMPANY =================
    static class Company {

        String name;
        UUID owner;

        Map<UUID, String> members = new HashMap<>();

        double balance = 0;

        int level = 1;
        String job = "NONE";

        Company(String name, UUID owner) {
            this.name = name;
            this.owner = owner;
            members.put(owner, "OWNER");
        }

        boolean isOwner(UUID u) {
            return owner.equals(u);
        }
    }

    // ================= HELPERS =================
    private String getPlayerCompany(UUID u) {
        for (Company c : companies.values()) {
            if (c.members.containsKey(u)) return c.name;
        }
        return null;
    }

    private Company getPlayerCompanyObj(UUID u) {
        for (Company c : companies.values()) {
            if (c.members.containsKey(u)) return c;
        }
        return null;
    }

    private void setupEconomy() {
        var rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp != null) econ = rsp.getProvider();
    }

    // ================= TASKS =================
    private void startSalaryTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                // future job system
            }
        }.runTaskTimer(this, 0, 20 * 60);
    }

    private void startMonthlyTax() {
        new BukkitRunnable() {
            @Override
            public void run() {
                // tax system later
            }
        }.runTaskTimer(this, 0, 20L * 60 * 60 * 24 * 30);
    }
}
