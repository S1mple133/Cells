package me.s1mple.Cells;

import me.s1mple.Cells.listeners.PlayerListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Cells extends JavaPlugin {
    private static Cells plugin;
    private static List<UUID> globalPlayers = new ArrayList<>();

    @Override
    public void onEnable() {
        plugin = this;
        plugin.saveDefaultConfig();


        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getLogger().info("Loaded");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        return false;
    }

    @Override
    public void onDisable() {

    }

    public static Cells getPlugin() {
        return plugin;
    }

    public static List<UUID> getGlobalPlayers() {
        return globalPlayers;
    }
}
