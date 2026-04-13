package me.plugin.firma.data;

import me.plugin.firma.company.Company;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.*;

public class DataManager {

    private final File file;

    public DataManager(JavaPlugin plugin) {
        file = new File(plugin.getDataFolder(), "companies.dat");

        if (!plugin.getDataFolder().exists())
            plugin.getDataFolder().mkdirs();
    }

    public void save(Map<String, Company> companies) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(companies);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Company> load() {
        if (!file.exists()) return new HashMap<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<String, Company>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new HashMap<>();
    }
}
