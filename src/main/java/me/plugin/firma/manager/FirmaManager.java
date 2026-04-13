package me.plugin.firma.manager;

import me.plugin.firma.FirmaPlugin;
import me.plugin.firma.model.Firma;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class FirmaManager {

    private final FirmaPlugin plugin;

    private final Map<String, Firma> firmy = new HashMap<>();
    private final Map<UUID, String> playerFirma = new HashMap<>();

    private final Map<UUID, String> invites = new HashMap<>();

    public FirmaManager(FirmaPlugin plugin) {
        this.plugin = plugin;
    }

    // ================= FIRMA =================

    public boolean hasFirma(UUID uuid) {
        return playerFirma.containsKey(uuid);
    }

    public Firma getFirma(UUID uuid) {
        String name = playerFirma.get(uuid);
        return name == null ? null : firmy.get(name);
    }

    public void createFirma(UUID owner, String name) {
        Firma f = new Firma(name, owner);

        firmy.put(name.toLowerCase(), f);
        playerFirma.put(owner, name.toLowerCase());
    }

    // ================= MEMBERS =================

    public void invite(UUID owner, String targetName) {
        UUID target = Bukkit.getOfflinePlayer(targetName).getUniqueId();
        invites.put(target, getFirma(owner).getName());
    }

    public void accept(UUID player) {
        if (!invites.containsKey(player)) return;

        String firmaName = invites.get(player);
        Firma f = firmy.get(firmaName.toLowerCase());

        f.addMember(player);
        playerFirma.put(player, firmaName.toLowerCase());

        invites.remove(player);
    }

    public void kick(UUID owner, String targetName) {
        UUID target = Bukkit.getOfflinePlayer(targetName).getUniqueId();

        Firma f = getFirma(owner);
        if (f == null) return;

        f.removeMember(target);
        playerFirma.remove(target);
    }

    // ================= SAVE =================

    public void save() {
        FileConfiguration cfg = plugin.getConfig();
        cfg.set("firmy", null);

        for (Firma f : firmy.values()) {

            String path = "firmy." + f.getName();

            cfg.set(path + ".owner", f.getOwner().toString());
            cfg.set(path + ".members", f.getMembers().stream().map(UUID::toString).toList());
            cfg.set(path + ".roles", f.getRoles());

            cfg.set(path + ".level", f.getLevel());
            cfg.set(path + ".xp", f.getXp());
            cfg.set(path + ".balance", f.getBalance());
        }

        plugin.saveConfig();
    }

    // ================= LOAD =================

    public void load() {
        FileConfiguration cfg = plugin.getConfig();

        if (!cfg.contains("firmy")) return;

        for (String name : cfg.getConfigurationSection("firmy").getKeys(false)) {

            String path = "firmy." + name;

            UUID owner = UUID.fromString(cfg.getString(path + ".owner"));
            Firma f = new Firma(name, owner);

            List<String> members = cfg.getStringList(path + ".members");
            for (String m : members) {
                UUID u = UUID.fromString(m);
                f.addMember(u);
                playerFirma.put(u, name.toLowerCase());
            }

            firmy.put(name.toLowerCase(), f);
            playerFirma.put(owner, name.toLowerCase());
        }
    }
                                                     }
