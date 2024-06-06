package com.example.demo.Model;

/**
 * A shop item. The name, price and description will be displayed in the UI.
 */
public class ShopItem {
    private final String name;
    private final int price; // maybe we could implement price increases with each purchase in the future
    private final String description;

    public ShopItem(String name, int price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
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


}
