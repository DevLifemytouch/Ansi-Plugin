package de.lifemytouch.ansi.cosmetic;

import org.bukkit.inventory.ItemStack;

public class Cosmetic {

    private final String id;
    private final ItemStack item;
    private final long price;

    public Cosmetic(String id, ItemStack item, long price) {
        this.id = id;
        this.item = item;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public ItemStack getItem() {
        return item;
    }

    public long getPrice() {
        return price;
    }
}