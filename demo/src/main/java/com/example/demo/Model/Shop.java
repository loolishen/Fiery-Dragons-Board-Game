package com.example.demo.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Logical representation of a shop. It contains a list of purchasable items
 */
public class Shop {
    private final List<ShopItem> items;

    public Shop() {
        this.items = new ArrayList<>();
    }

    public void addItem(ShopItem item) {
        items.add(item);
    }

    public List<ShopItem> getItems() {
        return items;
    }

}
