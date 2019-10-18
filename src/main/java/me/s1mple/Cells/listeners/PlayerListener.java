package me.s1mple.Cells.listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.s1mple.Cells.Cells;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onChat(AsyncPlayerChatEvent event) {
        if(event.getMessage().charAt(0) == '/' || !Cells.getGlobalPlayers().contains(event.getPlayer().getUniqueId()) || event.isCancelled())
            return;


        ByteArrayDataOutput out = null;

        String outputMessage = ChatColor.translateAlternateColorCodes('&',event.getMessage());
            // Send to other servers

            out = ByteStreams.newDataOutput();
            out.writeUTF("Forward");
            out.writeUTF("ALL");
            out.writeUTF("Cells");

            ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
            DataOutputStream msgout = new DataOutputStream(msgbytes);
            try {
                msgout.writeUTF(event.getPlayer().getName());
                msgout.writeUTF(outputMessage);
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            out.writeShort(msgbytes.toByteArray().length);
            out.write(msgbytes.toByteArray());

            event.getPlayer().sendPluginMessage(Cells.getPlugin(), "BungeeCord", out.toByteArray());
        }


    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        if(Cells.getGlobalPlayers().contains(event.getPlayer().getUniqueId()))
            Cells.getGlobalPlayers().remove(event.getPlayer().getUniqueId());
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
