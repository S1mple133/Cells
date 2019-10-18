package me.s1mple.Cells;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Util {
    public static ItemStack paper;
    public static ItemStack redstone;

    public static void generateItems() {
        paper = new ItemStack(Material.PAPER, 1);
        paper.getItemMeta().setDisplayName(ChatColor.YELLOW + "My Cells");

        redstone = new ItemStack(Material.REDSTONE, 1);
        redstone.getItemMeta().setDisplayName(ChatColor.RED + "Go back");
    }
    /**
     * Returns money amount like 3,000,000,000
     * @param requiredMiningLevel
     * @return
     */
    public static String ConverseMoney(int requiredMiningLevel) {
        throw new NotImplementedException();
    }

    /**
     * Calculates remaining time and outputs it as for example 7d
     * @param expireTime
     * @return
     */
    public static String CalculateRemainingTime(long expireTime) {
        throw new NotImplementedException();
    }

}
