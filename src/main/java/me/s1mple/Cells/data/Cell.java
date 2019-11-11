package me.s1mple.Cells.data;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.s1mple.Cells.Cells;
import me.s1mple.Cells.HiddenStringUtils;
import me.s1mple.Cells.Util;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import sun.jvm.hotspot.opto.Block;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Cell {
    private int id;
    private Location pos;
    private int room;
    private char section;
    private Security security;
    private int requiredMiningLevel;
    private BigInteger price;
    private long expireTime; // Days
    private static int nextId = 0;
    private UUID owner;
    private long actTime;
    private static List<Cell> cells = new ArrayList<>();
    private boolean isSold;
    private ProtectedRegion region;

    //<editor-fold desc="Constructors">

    /**
     * @param room
     * @param section
     * @param security
     * @param requiredMiningLevel
     * @param price
     * @param expireTime
     * @param owner
     * @param actTime
     * @param regionX
     * @param regionY
     * @param regionZ
     * @param regionWorld
     */
    public Cell(int room, char section, int security, int requiredMiningLevel, BigInteger price, long expireTime, String owner, long actTime, int regionX, int regionY, int regionZ, String regionWorld) {
        this.id = nextId++;
        this.room = room;
        this.section = section;
        this.security = Security.values()[security];
        this.requiredMiningLevel = requiredMiningLevel;
        this.price = price;
        this.expireTime = expireTime;
        this.owner = UUID.fromString(owner);
        this.actTime = actTime;
        this.pos = new Location(Bukkit.getWorld(regionWorld), regionX, regionY, regionZ);
        isSold = (owner == null);
        Set<ProtectedRegion> regions= Cells.getPlugin().worldGuardPlugin.getRegionManager(Bukkit.getWorld(regionWorld)).getApplicableRegions(pos).getRegions();

        for(ProtectedRegion r : regions) {
            if (r.getId().substring(0,4).equalsIgnoreCase("cell")) {
                region = r;
                break;
            }
        }

        cells.add(this);
    }

    public static Cell loadCell(int room, char section, int security, int requiredMiningLevel, BigInteger price, long expireTime, ProtectedRegion region, World w) {
        BlockVector max = region.getMinimumPoint();
        com.sk89q.worldedit.BlockVector min = region.getMaximumPoint();

        Location actLoc = new Location(w, ((max.getBlockX() - min.getBlockX()) /2), max.getBlockY(), (max.getBlockZ() - min.getBlockZ())/2);
        // Make y on the ground
        while(actLoc.getBlock().getType() == Material.AIR) {
            actLoc.add(0, -1, 0);
        }

        for(Cell cell : cells) {
            if(cell.pos.getBlockX() == actLoc.getBlockX() && cell.pos.getBlockY() == actLoc.getBlockY() && cell.pos.getBlockZ() == actLoc.getBlockZ()) {
                return null;
            }
        }

        return new Cell(room, section, security, requiredMiningLevel, price, expireTime, null, 0, actLoc.getBlockX(), actLoc.getBlockY(), actLoc.getBlockZ(), w.getName());
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

    public Location getPos() {
        return this.pos;
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

    public boolean isSold() {
        return isSold;
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
        lore.add(HiddenStringUtils.encodeString(String.valueOf(id)));
        lore.add(ChatColor.WHITE + "Section " + ChatColor.RED + ChatColor.BOLD + section);
        lore.add(ChatColor.WHITE + "Security " + Security.getSecurityName(security));
        lore.add(" ");
        lore.add(ChatColor.RED + "Required mining level " + ChatColor.WHITE + ChatColor.BOLD + requiredMiningLevel);
        lore.add(" ");
        lore.add(ChatColor.WHITE + "Price " + ChatColor.GREEN + "$" + Util.ConverseMoney(price));
        lore.add(ChatColor.WHITE + "Owner " + ChatColor.YELLOW + ChatColor.BOLD + getOwnerName());
        lore.add(" ");
        lore.add("Expiring in " + ChatColor.YELLOW + Util.CalculateRemainingTime(expireTime, actTime));

        return menuItem;
    }

    /**
     * Player p buys the cell
     * @param p
     * @return
     */
    public String Buy(Player p) {
        if(isSold) {
            return ChatColor.RED + "This cell is already sold!";
        }

        // Check player money

        actTime = System.currentTimeMillis();
        owner = p.getUniqueId();
        this.isSold = true;

        DefaultDomain d = new DefaultDomain();
        d.addPlayer(p.getUniqueId());
        region.setOwners(d);

        Util.saveToDb(this);

        return ChatColor.GREEN + "Successfully bought!";
    }

    /**
     * Gets a cell by id
     * @param id
     * @return
     */
    public Cell getById(int id) {
        for(Cell c : cells) {
            if(c.id == id) {
                return c;
            }
        }

        return null;
    }

    /**
     * Cell expires
     */
    public void Expire() {
        if(!isSold) {
            return;
        }

        region.setOwners(new DefaultDomain());
        this.owner = null;
        this.actTime = 0;
        this.isSold = false;
        Util.removeCellFromDb(this);
    }

    /**
     * Returns owner name or none
     * @return
     */
    private String getOwnerName() {
       return Bukkit.getOfflinePlayer(owner).getName();
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
                for (Cell c : cells) {
                    if(c.actTime >= c.expireTime) {
                        c.Expire();
                    }
                    else {
                        c.actTime++;
                    }
                }
            }
        }.runTaskTimer(Cells.getPlugin(), 0, 20*60*60*24);
    }

    /**
     * Get the id of an ItemStack of a Cell
     * @param itemStack
     * @return
     */
    public static Cell getCellByItem(ItemStack itemStack) {
        return cells.get(Integer.valueOf(HiddenStringUtils.extractHiddenString(itemStack.getItemMeta().getLore().get(0))));
    }

    /**
     * Returns cells owned by Player
     * @param p
     * @return
     */
    public static Inventory getOwnedBy(Player p) {
        Inventory inv = Bukkit.createInventory(p, InventoryType.CHEST, "Owned Cells");
        int free = 9;

        inv.setItem(4, Util.paper);
        inv.setItem(0, Util.redstone);

        for(Cell c : cells) {
            if(c.getOwner() == p.getUniqueId()) {
                inv.setItem(free++, c.getItem(1));
            }
        }

        return inv;
    }

    public ProtectedRegion getRegion() {
        return region;
    }
    //</editor-fold>

}

