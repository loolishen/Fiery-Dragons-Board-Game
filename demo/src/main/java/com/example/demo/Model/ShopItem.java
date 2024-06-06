package com.example.demo.Model;

public class ShopItem {
    private String name;
    private int price;
    private String description;
    private ItemType type; // Add this field

    public ShopItem(String name, int price, String description, ItemType type) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }
}
