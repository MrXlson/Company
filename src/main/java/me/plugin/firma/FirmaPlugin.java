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

    private final Map<String, UUID> owners = new HashMap<>();
    private final Map<UUID, String> playerCompany = new HashMap<>();
    private final Map<String, Double> companyMoney = new HashMap<>();

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
            openGUI(p);
            return true;
        }

        if (args[0].equalsIgnoreCase("create")) {
            if (args.length < 2) return true;

            String name = args[1];

            if (playerCompany.containsKey(p.getUniqueId())) {
                p.sendMessage("§cUž máš firmu!");
                return true;
            }

            if (owners.containsKey(name)) {
                p.sendMessage("§cFirma existuje!");
                return true;
            }

            if (!econ.has(p, 1000)) {
                p.sendMessage("§cNemáš peníze!");
                return true;
            }

            econ.withdrawPlayer(p, 1000);

            owners.put(name, p.getUniqueId());
            playerCompany.put(p.getUniqueId(), name);
            companyMoney.put(name, 0.0);

            p.sendMessage("§aFirma vytvořena!");
        }

        return true;
    }

    // ===== GUI =====
    private void openGUI(Player p) {

        if (!playerCompany.containsKey(p.getUniqueId())) {
            p.sendMessage("§cNemáš firmu!");
            return;
        }

        String name = playerCompany.get(p.getUniqueId());
        double money = companyMoney.getOrDefault(name, 0.0);

        Inventory inv = Bukkit.createInventory(null, 27, "§8Firma");

        inv.setItem(11, createItem(Material.PAPER, "§eInfo",
                "§7Firma: " + name,
                "§7Peníze: " + money));

        inv.setItem(13, createItem(Material.GOLD_INGOT, "§6Balance",
                "§7" + money + "$"));

        inv.setItem(15, createItem(Material.EMERALD, "§aVybrat 100$"));

        p.openInventory(inv);
    }

    // ===== CLICK =====
    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player)) return;

        Player p = (Player) e.getWhoClicked();

        if (!e.getView().getTitle().equals("§8Firma")) return;

        e.setCancelled(true);

        if (!playerCompany.containsKey(p.getUniqueId())) return;

        String name = playerCompany.get(p.getUniqueId());

        if (e.getSlot() == 15) {
            double balance = companyMoney.getOrDefault(name, 0.0);

            if (balance < 100) {
                p.sendMessage("§cFirma nemá peníze!");
                return;
            }

            companyMoney.put(name, balance - 100);
            econ.depositPlayer(p, 100);

            p.sendMessage("§aVybral jsi 100$!");
            openGUI(p);
        }
    }

    // ===== ITEM =====
    private ItemStack createItem(Material mat, String name, String... lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);
        return item;
    }
}
