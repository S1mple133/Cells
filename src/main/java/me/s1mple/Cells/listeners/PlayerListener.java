package me.s1mple.Cells.listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.s1mple.Cells.Cells;
import me.s1mple.Cells.Util;
import me.s1mple.Cells.data.Cell;
import me.s1mple.Cells.data.Tier;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerListener implements Listener {
    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        if(Cells.getGlobalPlayers().contains(event.getPlayer().getUniqueId()))
            Cells.getGlobalPlayers().remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onInteract(InventoryClickEvent e) {
        if(e.getWhoClicked() instanceof Player && e.getCurrentItem() != null) {
            if(e.getInventory().getName().equals(Util.cellInvName)) {
                if(e.getCurrentItem().equals(Util.paper)) {
                    e.getWhoClicked().openInventory(Cell.getOwnedBy((Player)e.getWhoClicked()));
                }
                Util.doClickOnCell((Player) e.getWhoClicked(), Cell.getCellByItem(e.getCurrentItem()));
            }
            else if(e.getInventory().getName().equals(Util.tierInvName)) {
                Util.doClickOnTier(e.getCurrentItem(), (Player)e.getWhoClicked());
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if(Cells.getPlugin().getServer().getOnlinePlayers().size() == 1) {
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    Cells.getGlobalPlayers().add(event.getPlayer().getUniqueId());
                }
            }.runTaskLater(Cells.getPlugin(), 10);
        }
        else {
            Cells.getGlobalPlayers().add(event.getPlayer().getUniqueId());
        }
    }
}
