package com.example.demo.Model;

import java.util.ArrayList;
import java.util.List;

public class Shop {
    private List<ShopItem> items;

    public Shop() {
        this.items = new ArrayList<>();
    }

    public void addItem(ShopItem item) {
        items.add(item);
    }

    public List<ShopItem> getItems() {
        return items;
    }

    public ShopItem getItemByName(String itemName) {
        for (ShopItem item : items) {
            if (item.getName().equals(itemName)) {
                return item;
            }
        }
        return null;
    }

}
