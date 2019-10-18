package me.s1mple.Cells.data;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.graalvm.compiler.core.common.type.ArithmeticOpTable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class Tier {
    private String name;
    private int id;
    private ItemStack menuItem;
    private List<Cell> cells = new ArrayList<>();

    //<editor-fold desc="Constructor">
    public Tier(int id, String name, Material menuMaterial) {
        this.id = id;
        this.name = name;
        this.menuItem = new ItemStack(menuMaterial);

        initializeCells();
    }
    //</editor-fold>

    //<editor-fold desc="Methods">

    /**
     * Adds the cells to the list
     */
    private void initializeCells() {
        throw new NotImplementedException();
    }

    public Inventory generateInventory() {
        Inventory inv = Bukkit.createInventory(null, 27, "Tiers");

        inv.setItem(4, paper);
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
    //</editor-fold>


}
