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

    @Override
    public void onEnable() {
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

            if (!econ.has(p, 1000)) {
                p.sendMessage("§cNemáš peníze!");
                return true;
            }

            econ.withdrawPlayer(p, 1000);

            companies.put(name, new Company(name, p.getUniqueId()));
            p.sendMessage("§aFirma vytvořena!");
        }

        // INVITE
        if (args[0].equalsIgnoreCase("invite")) {
            if (args.length < 2) return true;

            Company c = getCompany(p);
            if (c == null) return true;

            if (!c.isManager(p.getUniqueId())) {
                p.sendMessage("§cNemáš práva!");
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) return true;

            invites.put(target.getUniqueId(), c.name);
            openInviteGUI(target);
        }

        return true;
    }

    // ===== GUI =====
    private void openMainGUI(Player p) {

        Company c = getCompany(p);
        if (c == null) {
            p.sendMessage("§cNemáš firmu!");
            return;
        }

        Inventory inv = Bukkit.createInventory(null, 27, "§8Firma");

        inv.setItem(13, createItem(Material.GOLD_INGOT, "§6Balance",
                "§7" + c.balance + "$"));

        inv.setItem(22, createItem(Material.PLAYER_HEAD, "§bZaměstnanci"));

        p.openInventory(inv);
    }

    private void openMembersGUI(Player p) {

        Company c = getCompany(p);
        Inventory inv = Bukkit.createInventory(null, 54, "§8Zaměstnanci");

        int i = 0;

        for (UUID uuid : c.members.keySet()) {
            Player pl = Bukkit.getPlayer(uuid);

            inv.setItem(i++, createItem(Material.PLAYER_HEAD,
                    "§f" + (pl != null ? pl.getName() : "Offline"),
                    "§7Role: " + c.members.get(uuid),
                    "§cKlikni pro vyhození"));
        }

        p.openInventory(inv);
    }

    private void openInviteGUI(Player p) {
        Inventory inv = Bukkit.createInventory(null, 27, "§8Pozvánka");

        inv.setItem(11, createItem(Material.LIME_WOOL, "§aPřijmout"));
        inv.setItem(15, createItem(Material.RED_WOOL, "§cOdmítnout"));

        p.openInventory(inv);
    }

    // ===== CLICK =====
    @EventHandler
    public void click(InventoryClickEvent e) {

        Player p = (Player) e.getWhoClicked();

        // MAIN GUI
        if (e.getView().getTitle().equals("§8Firma")) {
            e.setCancelled(true);

            if (e.getSlot() == 22) {
                openMembersGUI(p);
            }
        }

        // MEMBERS
        if (e.getView().getTitle().equals("§8Zaměstnanci")) {
            e.setCancelled(true);

            Company c = getCompany(p);

            if (!c.isManager(p.getUniqueId())) return;

            ItemStack item = e.getCurrentItem();
            if (item == null) return;

            String name = item.getItemMeta().getDisplayName().replace("§f", "");
            Player target = Bukkit.getPlayer(name);

            if (target != null) {
                c.members.remove(target.getUniqueId());
                p.sendMessage("§cVyhozen!");
                openMembersGUI(p);
            }
        }

        // INVITE
        if (e.getView().getTitle().equals("§8Pozvánka")) {
            e.setCancelled(true);

            if (!invites.containsKey(p.getUniqueId())) return;

            String company = invites.get(p.getUniqueId());

            if (e.getSlot() == 11) {
                Company c = companies.get(company);
                c.members.put(p.getUniqueId(), "EMPLOYEE");
                p.sendMessage("§aPřipojen do firmy!");
            }

            invites.remove(p.getUniqueId());
            p.closeInventory();
        }
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

        Company(String name, UUID owner) {
            this.name = name;
            members.put(owner, "OWNER");
        }

        boolean isManager(UUID u) {
            String role = members.get(u);
            return role.equals("OWNER") || role.equals("MANAGER");
        }
    }
}
