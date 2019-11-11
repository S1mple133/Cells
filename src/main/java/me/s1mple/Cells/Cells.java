package me.s1mple.Cells;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.s1mple.Cells.data.Cell;
import me.s1mple.Cells.data.Tier;
import me.s1mple.Cells.listeners.PlayerListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Cells extends JavaPlugin {
    private static Cells plugin;
    private static List<UUID> globalPlayers = new ArrayList<>();
    public WorldGuardPlugin worldGuardPlugin = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");

    @Override
    public void onEnable() {
        plugin = this;
        plugin.saveDefaultConfig();

        Util.generateItems();
        Tier.initializeTiers();
        Tier.initializeCells();
        Cell.doCheckExpire();

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
