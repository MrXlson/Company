package me.plugin.firma.data;

import me.plugin.firma.company.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class DataManager {

    private final JavaPlugin plugin;
    private final File file;

    public DataManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "companies.yml");

        if (!plugin.getDataFolder().exists())
            plugin.getDataFolder().mkdirs();
    }

    public void saveAsync(Map<String, Company> companies) {

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {

            try {
                YamlConfiguration cfg = new YamlConfiguration();

                for (Company c : companies.values()) {

                    String path = "companies." + c.name;

                    cfg.set(path + ".owner", c.owner.toString());
                    cfg.set(path + ".balance", c.balance);
                    cfg.set(path + ".level", c.level);
                    cfg.set(path + ".xp", c.xp);

                    List<String> members = new ArrayList<>();

                    for (UUID u : c.members.keySet()) {
                        members.add(u + ":" + c.members.get(u).name());
                    }

                    cfg.set(path + ".members", members);
                }

                cfg.save(file);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Map<String, Company> load() {

        Map<String, Company> map = new HashMap<>();

        if (!file.exists()) return map;

        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        if (!cfg.contains("companies")) return map;

        for (String key : cfg.getConfigurationSection("companies").getKeys(false)) {

            String path = "companies." + key;

            UUID owner = UUID.fromString(cfg.getString(path + ".owner"));
            Company c = new Company(key, owner);

            c.balance = cfg.getDouble(path + ".balance");
            c.level = cfg.getInt(path + ".level");
            c.xp = cfg.getDouble(path + ".xp");

            for (String s : cfg.getStringList(path + ".members")) {

                String[] split = s.split(":");
                UUID u = UUID.fromString(split[0]);
                Role role = Role.valueOf(split[1]);

                c.members.put(u, role);
            }

            map.put(key, c);
        }

        return map;
    }
}
