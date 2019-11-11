package me.s1mple.Cells.data;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.s1mple.Cells.Cells;
import me.s1mple.Cells.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Item;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.graalvm.compiler.core.common.type.ArithmeticOpTable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Tier {
    private String name;
    private int id;
    private ItemStack menuItem;
    private List<Cell> cells = new ArrayList<>();
    private static List<Tier> tiers = new ArrayList<>();
    private static Cells plugin = Cells.getPlugin();

    //<editor-fold desc="Constructor">
    public Tier(int id, String name, Material menuMaterial) {
        this.id = id;
        this.name = name;
        this.menuItem = new ItemStack(menuMaterial);
        this.menuItem.getItemMeta().setDisplayName(name);

        tiers.add(this);
        initializeCells();
    }
    //</editor-fold>

    //<editor-fold desc="Methods">

    /**
     * Adds the cells to the list
     */
    public static void initializeCells() {
        List<World> worldList = Bukkit.getServer().getWorlds();
        RegionManager regionManager;
        String[] data;

        // Load cells from the DB first
        Util.loadCellsFromDB();

        for (World world: worldList) {
            regionManager = plugin.worldGuardPlugin.getRegionManager(world);

            Map<String, ProtectedRegion> regionMap = regionManager.getRegions();
            Set<String> names = regionMap.keySet();

            for (String actName: names) {
                if(actName.substring(0,4).equalsIgnoreCase("cell")) {
                    data = actName.split("_");
                    Cell cell = Cell.loadCell(Integer.valueOf(data[2]),
                            data[3].charAt(0),
                            Integer.valueOf(data[4]),
                            Integer.valueOf(data[5]),
                            new BigInteger(data[6]),
                            Integer.valueOf(data[7]),
                            regionMap.get(actName),
                            world);

                    if(cell != null) {
                        tiers.get(Integer.valueOf(data[1])).cells.add(cell);
                    }
                }
            }
        }
    }

    /**
     * Returns the tier inventory
     * @return
     */
    public static Inventory getTierInventory() {
        int free = (9-tiers.size())/2;
        Inventory inv = Bukkit.createInventory(null, InventoryType.CHEST, Util.tierInvName);

        inv.setItem(4, Util.paper);
        inv.setItem(0, Util.redstone);

        for (Tier tier : tiers) {
            inv.setItem(free++, tier.getMenuItem());
        }

        return inv;
    }

    /**
     * Initializes all tiers from the config
     */
    public static void initializeTiers() {
        int maxCount = plugin.getConfig().getInt("TierCount");

        for (int i= 0; i < maxCount; i++) {
            ConfigurationSection section = plugin.getConfig().getConfigurationSection("Tiers." + i);

            new Tier(Integer.valueOf(section.getName()),
                    ChatColor.translateAlternateColorCodes('&',
                            section.getString("Name")), Material.valueOf(section.getString("Block")));
        }
    }

    /**
     * Generates cell inventory of tier
     * @return
     */
    public Inventory generateInventory() {
        int cnt = 0;
        Inventory inv = Bukkit.createInventory(null, InventoryType.CHEST, Util.cellInvName);

        inv.setItem(4, Util.paper);
        inv.setItem(0, Util.redstone);

        for(int i = 1; i < inv.getSize()/9; i++) {
            for(int j = 0; j < 9; j++) {
                if(cells.size() < cnt) {
                    break;
                }

                inv.setItem((i*9)+j-1, cells.get(cnt++).getItem(1));
            }

            if(cells.size() < cnt) {
                break;
            }
        }

        return inv;
    }

    /**
     * Returns the tier of the item from the tier inv
     * @param item
     * @return
     */
    public static Tier getTierOfItem(ItemStack item) {
        for(Tier t : tiers) {
            if(t.getName().equals(item.getItemMeta().getDisplayName())) {
                return t;
            }
        }

        return null;
    }
    //</editor-fold>

    //<editor-fold desc="Getters">
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public ItemStack getMenuItem() {
        return menuItem;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public static List<Tier> getTiers() {
        return tiers;
    }
    //</editor-fold>


}
