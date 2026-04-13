package me.plugin.firma;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class FirmaPlugin extends JavaPlugin implements Listener, CommandExecutor {

    // ================= ECONOMY =================
    private static Economy econ;

    // ================= DATA =================
    private final Map<String, Company> companies = new HashMap<>();
    private final Map<UUID, String> invites = new HashMap<>();
    private final Map<UUID, UUID> selectedTarget = new HashMap<>();
    private final Map<UUID, String> renameBuffer = new HashMap<>();
    private final Map<UUID, String> transferBuffer = new HashMap<>();

    // ================= ENABLE =================
    @Override
    public void onEnable() {

        setupEconomy();

        if (getCommand("firma") != null)
            getCommand("firma").setExecutor(this);

        Bukkit.getPluginManager().registerEvents(this, this);

        getLogger().info("FirmaPlugin FINAL enabled");
    }

    // ================= COMPANY =================
    static class Company {
        String name;
        UUID owner;
        double balance = 0;

        Map<UUID, Role> members = new HashMap<>();
        Map<UUID, String> jobs = new HashMap<>();

        Company(String name, UUID owner) {
            this.name = name;
            this.owner = owner;

            members.put(owner, Role.OWNER);
        }
    }

    enum Role {
        OWNER, MANAGER, WORKER
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

        if (args[0].equalsIgnoreCase("create")) {
            if (c != null) return true;
            if (args.length < 2) return true;

            Company nc = new Company(args[1], p.getUniqueId());
            companies.put(args[1], nc);

            p.sendMessage("§aFirma vytvořena");
        }

        return true;
    }

    // ================= MAIN GUI =================
    private void openMainGUI(Player p, Company c) {

        Inventory inv = Bukkit.createInventory(null, 45, "§6Firma Menu");

        inv.setItem(10, item(Material.GOLD_INGOT, "§eBanka"));
        inv.setItem(12, item(Material.PLAYER_HEAD, "§bČlenové"));
        inv.setItem(14, item(Material.PAPER, "§dInvite"));
        inv.setItem(16, item(Material.ANVIL, "§cNastavení"));
        inv.setItem(22, item(Material.EMERALD, "§aTOP"));
        inv.setItem(40, item(Material.BARRIER, "§cZavřít"));

        p.openInventory(inv);
    }

    // ================= BANK =================
    private void openBank(Player p, Company c) {

        Inventory inv = Bukkit.createInventory(null, 27, "§bBanka");

        inv.setItem(11, item(Material.GREEN_WOOL, "§aVložit +100"));
        inv.setItem(13, item(Material.RED_WOOL, "§cVybrat -100"));
        inv.setItem(15, item(Material.BARRIER, "§cZpět"));

        p.openInventory(inv);
    }

    // ================= MEMBERS =================
    private void openMembers(Player p, Company c) {

        Inventory inv = Bukkit.createInventory(null, 27, "§bČlenové");

        int i = 0;

        for (UUID u : c.members.keySet()) {

            OfflinePlayer op = Bukkit.getOfflinePlayer(u);

            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();

            meta.setDisplayName("§e" + op.getName());
            meta.setOwningPlayer(op);
            meta.setLore(List.of("§7Klik pro správu"));

            head.setItemMeta(meta);

            inv.setItem(i++, head);
        }

        p.openInventory(inv);
    }

    // ================= MEMBER MANAGE =================
    private void openMemberManage(Player p, Company c, UUID target) {

        Inventory inv = Bukkit.createInventory(null, 27, "§eSpráva člena");

        inv.setItem(10, item(Material.BARRIER, "§cKick"));
        inv.setItem(12, item(Material.NAME_TAG, "§eNastavit job"));
        inv.setItem(14, item(Material.CYAN_WOOL, "§bZměnit roli"));

        p.openInventory(inv);
    }

    // ================= SETTINGS =================
    private void openSettings(Player p, Company c) {

        Inventory inv = Bukkit.createInventory(null, 27, "§cNastavení");

        inv.setItem(10, item(Material.NAME_TAG, "§ePřejmenovat firmu"));
        inv.setItem(12, item(Material.PLAYER_HEAD, "§bPřevést firmu"));
        inv.setItem(14, item(Material.BARRIER, "§cZpět"));

        p.openInventory(inv);
    }

    // ================= INVITE =================
    private void openInvite(Player p) {

        Inventory inv = Bukkit.createInventory(null, 27, "§dInvite menu");

        int i = 0;

        for (Player t : Bukkit.getOnlinePlayers()) {
            if (t.equals(p)) continue;

            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();

            meta.setDisplayName("§e" + t.getName());
            meta.setOwningPlayer(t);

            head.setItemMeta(meta);

            inv.setItem(i++, head);
        }

        p.openInventory(inv);
    }

    // ================= CLICK =================
    @EventHandler
    public void click(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player p)) return;

        ItemStack it = e.getCurrentItem();
        if (it == null || !it.hasItemMeta()) return;

        String t = e.getView().getTitle();
        Company c = getCompany(p);

        e.setCancelled(true);

        // ================= MAIN =================
        if (t.equals("§6Firma Menu")) {

            switch (it.getType()) {

                case GOLD_INGOT -> openBank(p, c);
                case PLAYER_HEAD -> openMembers(p, c);
                case PAPER -> openInvite(p);
                case ANVIL -> openSettings(p, c);
                case BARRIER -> p.closeInventory();
            }
        }

        // ================= BANK =================
        if (t.equals("§bBanka")) {

            if (it.getType() == Material.GREEN_WOOL) {
                c.balance += 100;
                econ.withdrawPlayer(p, 100);
                openBank(p, c);
            }

            if (it.getType() == Material.RED_WOOL) {
                c.balance -= 100;
                econ.depositPlayer(p, 100);
                openBank(p, c);
            }

            if (it.getType() == Material.BARRIER) {
                openMainGUI(p, c);
            }
        }

        // ================= MEMBERS =================
        if (t.equals("§bČlenové")) {

            OfflinePlayer op = Bukkit.getOfflinePlayer(pickUUIDFromHead(it));

            selectedTarget.put(p.getUniqueId(), op.getUniqueId());

            openMemberManage(p, c, op.getUniqueId());
        }

        // ================= MEMBER MANAGE =================
        if (t.equals("§eSpráva člena")) {

            UUID target = selectedTarget.get(p.getUniqueId());

            if (target == null) return;

            if (it.getType() == Material.BARRIER) {
                c.members.remove(target);
                p.sendMessage("§cHráč vyhozen");
            }

            if (it.getType() == Material.NAME_TAG) {
                c.jobs.put(target, "MINER");
                p.sendMessage("§aJob nastaven");
            }

            if (it.getType() == Material.CYAN_WOOL) {
                c.members.put(target, Role.MANAGER);
                p.sendMessage("§bRole změněna");
            }
        }

        // ================= SETTINGS =================
        if (t.equals("§cNastavení")) {

            if (it.getType() == Material.NAME_TAG) {
                renameBuffer.put(p.getUniqueId(), c.name);
                p.sendMessage("§ePoužij /firma rename <nový název>");
            }

            if (it.getType() == Material.PLAYER_HEAD) {
                transferBuffer.put(p.getUniqueId(), c.name);
                p.sendMessage("§ePoužij /firma transfer <hráč>");
            }

            if (it.getType() == Material.BARRIER) {
                openMainGUI(p, c);
            }
        }

        // ================= INVITE =================
        if (t.equals("§dInvite menu")) {

            Player target = Bukkit.getPlayer(it.getItemMeta().getDisplayName().replace("§e", ""));
            if (target == null) return;

            invites.put(target.getUniqueId(), c.name);

            p.sendMessage("§aPozvánka odeslána");
            target.sendMessage("§eDostal jsi invite do firmy /firma accept");
        }
    }

    // ================= HELPERS =================
    private Company getCompany(Player p) {
        for (Company c : companies.values()) {
            if (c.members.containsKey(p.getUniqueId())) return c;
        }
        return null;
    }

    private ItemStack item(Material m, String name) {
        ItemStack i = new ItemStack(m);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(name);
        i.setItemMeta(meta);
        return i;
    }

    private UUID pickUUIDFromHead(ItemStack it) {
        return ((SkullMeta) it.getItemMeta()).getOwningPlayer().getUniqueId();
    }

    private void setupEconomy() {
        RegisteredServiceProvider<Economy> rsp =
                getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp != null) econ = rsp.getProvider();
    }
            }
