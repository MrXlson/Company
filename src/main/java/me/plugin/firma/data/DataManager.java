package me.plugin.firma.data;

import me.plugin.firma.company.Company;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DataManager {

    private final File file;

    public DataManager(JavaPlugin plugin) {
        file = new File(plugin.getDataFolder(), "data.dat");
    }

    public Map<String, Company> load() {
        return new HashMap<>();
    }

    public void save(Map<String, Company> map) {}
}
