package de.tum.in.ase.eist.model;

import de.tum.in.ase.eist.util.ItemType;

public class Item {
    private String name;
    private ItemType itemType;
    private double price;

    public Item() {
        // Default
    }

    public Item(String itemName, ItemType itemType, double price) {
        this.name = itemName;
        this.itemType = itemType;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name +
                "', itemType='" + itemType +
                "', price=" + price +
                "}";
    }

}
