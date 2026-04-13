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

    private static Economy econ;

    private final Map<String, Company> companies = new HashMap<>();
    private final Map<UUID, String> invites = new HashMap<>();
    private final Map<UUID, UUID> selectedTarget = new HashMap<>();

    // ================= ENABLE =================
    @Override
    public void onEnable() {

        setupEconomy();

        if (getCommand("firma") != null)
            getCommand("firma").setExecutor(this);

        Bukkit.getPluginManager().registerEvents(this, this);

        getLogger().info("FirmaPlugin FIX enabled");
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

    // ================= CLICK =================
    @EventHandler
    public void click(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player p)) return;

        ItemStack it = e.getCurrentItem();
        if (it == null || !it.hasItemMeta()) return;

        String t = e.getView().getTitle();
        Company c = getCompany(p);

        // ❗ BLOKUJ JEN FIRMA GUI
        if (isFirmaGUI(t)) {
            e.setCancelled(true);
        } else {
            return; // ❗ survival + truhly fungují normálně
        }

        // ================= MAIN =================
        if (t.equals("§6Firma Menu")) {

            switch (it.getType()) {

                case GOLD_INGOT -> openBank(p, c);
                case PLAYER_HEAD -> openMembers(p, c);
                case PAPER -> p.sendMessage("§eInvite systém bude doplněn");
                case ANVIL -> p.sendMessage("§cSettings budou doplněny");
                case BARRIER -> p.closeInventory();
            }
        }

        // ================= BANK =================
        if (t.equals("§bBanka")) {

            if (it.getType() == Material.GREEN_WOOL) {
                c.balance += 100;

                if (econ != null)
                    econ.withdrawPlayer(p, 100);

                openBank(p, c);
            }

            if (it.getType() == Material.RED_WOOL) {
                c.balance -= 100;

                if (econ != null)
                    econ.depositPlayer(p, 100);

                openBank(p, c);
            }

            if (it.getType() == Material.BARRIER) {
                openMainGUI(p, c);
            }
        }

        // ================= MEMBERS =================
        if (t.equals("§bČlenové")) {

            UUID uuid = getUUIDFromHead(it);
            if (uuid == null) return;

            selectedTarget.put(p.getUniqueId(), uuid);

            p.sendMessage("§aVybrán hráč");
        }
    }

    // ================= HELPERS =================
    private boolean isFirmaGUI(String t) {
        return t.equals("§6Firma Menu")
                || t.equals("§bBanka")
                || t.equals("§bČlenové");
    }

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

    private UUID getUUIDFromHead(ItemStack it) {
        if (!(it.getItemMeta() instanceof SkullMeta meta)) return null;
        if (meta.getOwningPlayer() == null) return null;
        return meta.getOwningPlayer().getUniqueId();
    }

    private void setupEconomy() {
        RegisteredServiceProvider<Economy> rsp =
                getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp != null) econ = rsp.getProvider();
    }
            }
