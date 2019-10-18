package me.s1mple.Cells.data;

import me.s1mple.Cells.Cells;
import me.s1mple.Cells.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

public class Cell {
    private int id;
    private int room;
    private char section;
    private Security security;
    private int requiredMiningLevel;
    private BigInteger price;
    private long expireTime;
    private static int nextId = 0;
    private UUID owner;

    //<editor-fold desc="Constructors">
    public Cell(int room, char section, int security, int requiredMiningLevel, BigInteger price, long expireTime, UUID owner) {
        this.id = nextId++;
        this.room = room;
        this.section = section;
        this.security = Security.values()[security];
        this.requiredMiningLevel = requiredMiningLevel;
        this.price = price;
        this.expireTime = expireTime;
        this.owner = owner;
    }

    public Cell(int room, char section, int security, int requiredMiningLevel, BigInteger price, long expireTime) {
        this(room, section, security, requiredMiningLevel, price, expireTime, null);
    }
    //</editor-fold>

    //<editor-fold desc="Getters and Setters">
    public int getId() {
        return id;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public int getRoom() {
        return room;
    }

    public int getSection() {
        return section;
    }

    public Security getSecurity() {
        return security;
    }

    public int getRequiredMiningLevel() {
        return requiredMiningLevel;
    }

    public BigInteger getPrice() {
        return price;
    }

    public long getExpireTime() {
        return expireTime;
    }
    //</editor-fold>

    //<editor-fold desc="Methods">

    /**
     * Generate the Cell item shown in the menu
     * @param amount
     * @return
     */
    public ItemStack getItem(int amount) {
        ItemStack menuItem = new ItemStack(Material.valueOf(Cells.getPlugin().getConfig().getString("Menu.CellItem")), amount);
        ItemMeta itemMeta = menuItem.getItemMeta();

        itemMeta.setDisplayName("Room"+ ChatColor.BLUE + ChatColor.BOLD + " " + room);

        List<String> lore = itemMeta.getLore();
        lore.add(" ");
        lore.add(ChatColor.WHITE + "Section " + ChatColor.RED + ChatColor.BOLD + section);
        lore.add(ChatColor.WHITE + "Security " + Security.getSecurityName(security));
        lore.add(" ");
        lore.add(ChatColor.RED + "Required mining level " + ChatColor.WHITE + ChatColor.BOLD + requiredMiningLevel);
        lore.add(" ");
        lore.add(ChatColor.WHITE + "Price " + ChatColor.GREEN + "$" + Util.ConverseMoney(requiredMiningLevel));
        lore.add(ChatColor.WHITE + "Owner " + ChatColor.YELLOW + ChatColor.BOLD + getOwnerName());
        lore.add(" ");
        lore.add("Expiring in " + ChatColor.YELLOW + Util.CalculateRemainingTime(expireTime));

        return menuItem;
    }

    /**
     * Player p buys the cell
     * @param p
     * @return
     */
    public boolean Buy(Player p) {
        throw new NotImplementedException();
    }

    /**
     * Player p sells the cell
     * @param p
     */
    public void Sell(Player p) {

    }

    /**
     * Returns owner name or none
     * @return
     */
    private String getOwnerName() {
        throw new NotImplementedException();
    }
    //</editor-fold>

    //<editor-fold desc="Static Methods">

    /**
     * Starts the check expire cycle for all Cells
     */
    public static void doCheckExpire() {
        BukkitTask run = new BukkitRunnable() {
            @Override
            public void run() {

            }
        }.runTaskTimer(Cells.getPlugin(), 0, 20*60*60*24);
    }
    //</editor-fold>

}

