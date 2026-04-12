package me.plugin.firma;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class FirmaPlugin extends JavaPlugin implements CommandExecutor {

    private Map<String, Company> companies = new HashMap<>();
    private Map<UUID, String> playerCompany = new HashMap<>();
    private Map<UUID, UUID> invites = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadCompanies();
        getCommand("firma").setExecutor(this);
    }

    @Override
    public void onDisable() {
        saveCompanies();
    }

    // ---------- COMMAND ----------
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;

        if (args.length == 0) {
            p.sendMessage("§e/firma create <název>");
            p.sendMessage("§e/firma invite <hráč>");
            p.sendMessage("§e/firma join");
            p.sendMessage("§e/firma kick <hráč>");
            return true;
        }

        // CREATE
        if (args[0].equalsIgnoreCase("create")) {
            if (args.length < 2) return true;

            String name = args[1].toLowerCase();

            if (companies.containsKey(name)) {
                p.sendMessage("§cFirma už existuje!");
                return true;
            }

            Company c = new Company(name, p.getUniqueId());
            companies.put(name, c);
            playerCompany.put(p.getUniqueId(), name);

            p.sendMessage("§aFirma vytvořena!");
            return true;
        }

        // INVITE
        if (args[0].equalsIgnoreCase("invite")) {
            if (args.length < 2) return true;

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                p.sendMessage("§cHráč není online!");
                return true;
            }

            invites.put(target.getUniqueId(), p.getUniqueId());
            target.sendMessage("§aByl jsi pozván do firmy! Použij /firma join");
            return true;
        }

        // JOIN
        if (args[0].equalsIgnoreCase("join")) {

            if (!invites.containsKey(p.getUniqueId())) {
                p.sendMessage("§cNemáš pozvánku!");
                return true;
            }

            UUID inviter = invites.get(p.getUniqueId());
            String companyName = playerCompany.get(inviter);

            Company c = companies.get(companyName);
            c.members.add(p.getUniqueId());
            playerCompany.put(p.getUniqueId(), companyName);

            p.sendMessage("§aPřipojil ses do firmy!");
            return true;
        }

        // KICK
        if (args[0].equalsIgnoreCase("kick")) {
            if (args.length < 2) return true;

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                p.sendMessage("§cHráč není online!");
                return true;
            }

            String companyName = playerCompany.get(p.getUniqueId());
            Company c = companies.get(companyName);

            if (!c.owner.equals(p.getUniqueId())) {
                p.sendMessage("§cNejsi owner!");
                return true;
            }

            c.members.remove(target.getUniqueId());
            playerCompany.remove(target.getUniqueId());

            p.sendMessage("§cHráč vyhozen!");
            return true;
        }

        return true;
    }

    // ---------- SAVE / LOAD ----------
    private void saveCompanies() {
        FileConfiguration config = getConfig();

        config.set("companies", null);

        for (String name : companies.keySet()) {
            Company c = companies.get(name);

            config.set("companies." + name + ".owner", c.owner.toString());

            List<String> members = new ArrayList<>();
            for (UUID uuid : c.members) {
                members.add(uuid.toString());
            }
            config.set("companies." + name + ".members", members);
        }

        saveConfig();
    }

    private void loadCompanies() {
        FileConfiguration config = getConfig();

        if (!config.contains("companies")) return;

        for (String name : config.getConfigurationSection("companies").getKeys(false)) {

            UUID owner = UUID.fromString(config.getString("companies." + name + ".owner"));

            Company c = new Company(name, owner);

            List<String> members = config.getStringList("companies." + name + ".members");
            for (String m : members) {
                c.members.add(UUID.fromString(m));
                playerCompany.put(UUID.fromString(m), name);
            }

            playerCompany.put(owner, name);
            companies.put(name, c);
        }
    }

    // ---------- CLASS ----------
    static class Company {
        String name;
        UUID owner;
        Set<UUID> members = new HashSet<>();

        public Company(String name, UUID owner) {
            this.name = name;
            this.owner = owner;
        }
    }
              }
