package me.s1mple.Cells;

import me.s1mple.Cells.data.Cell;
import me.s1mple.Cells.data.Tier;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.math.BigInteger;

public class Util {
    public static ItemStack paper;
    public static ItemStack redstone;
    public static String tierInvName = "Tiers";
    public static  String cellInvName = "Cells";

    public static void generateItems() {
        paper = new ItemStack(Material.PAPER, 1);
        paper.getItemMeta().setDisplayName(ChatColor.YELLOW + "My Cells");

        redstone = new ItemStack(Material.REDSTONE, 1);
        redstone.getItemMeta().setDisplayName(ChatColor.RED + "Go back");
    }

    /**
     * Returns money amount like 3,000,000,000
     * @param price
     * @return
     */
    public static String ConverseMoney(BigInteger price) {
        return price.toString(3);
    }

    /**
     * Calculates remaining time and outputs it as for example 7d
     * @param expireTime
     * @return
     */
    public static String CalculateRemainingTime(long expireTime, long actTime) {
        return ((int)(actTime + 1000*60*60*24*expireTime)-System.currentTimeMillis()) + "d";
    }

    /**
     * Handles cell click
     * @param p
     * @param cell
     */
    public static void doClickOnCell(Player p, Cell cell) {
        if(cell.getOwner().equals(p.getUniqueId())) {
            p.teleport(cell.getPos()); // TP player
        }
        else {
            cell.Buy(p);
        }
    }

    /**
     * Handles Tier click
     * @param tier
     * @param p
     */
    public static void doClickOnTier(ItemStack tier, Player p) {
        if(tier == null) {
            return;
        }

        p.closeInventory();
        p.openInventory(Tier.getTierOfItem(tier).generateInventory());
    }

    /**
     * Loads a cell from the database
     */
    public static void loadCellsFromDB() {
        throw new NotImplementedException();
    }

    /**
     * Saves one cell to the database
     * @param cell
     */
    public static void saveToDb(Cell cell) {
        throw new NotImplementedException();
    }

    /**
     * Removes one cell from the database
     * @param cell
     */
    public static void removeCellFromDb(Cell cell) {
        throw new NotImplementedException();
    }
}
