package me.plugin.firma;

import me.plugin.firma.chat.ChatInputManager;
import me.plugin.firma.listener.ChatListener;
import me.plugin.firma.listener.InventoryClickListener;
import me.plugin.firma.listener.FirmaListener;
import org.bukkit.plugin.java.JavaPlugin;

public class FirmaPlugin extends JavaPlugin {

    private static FirmaPlugin instance;
    private ChatInputManager chatInputManager;

    @Override
    public void onEnable() {
        instance = this;

        chatInputManager = new ChatInputManager();

        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new FirmaListener(this), this);

        getLogger().info("FirmaPlugin zapnut!");
    }

    public static FirmaPlugin getInstance() {
        return instance;
    }

    public ChatInputManager getChatInputManager() {
        return chatInputManager;
    }
}
