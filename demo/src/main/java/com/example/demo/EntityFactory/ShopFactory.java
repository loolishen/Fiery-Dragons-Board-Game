package com.example.demo.EntityFactory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.example.demo.Controller.PlayerTurnManager;
import com.example.demo.Controller.TextDisplayManager;
import com.example.demo.Model.ItemType;
import com.example.demo.Model.Player;
import com.example.demo.Model.Shop;
import com.example.demo.Model.ShopItem;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ShopFactory extends SpawnFactory {

    private Shop shop;

    PlayerTurnManager playerManager;

    TextDisplayManager textDisplayManager;

    public ShopFactory(Shop shop, PlayerTurnManager playerManager, TextDisplayManager textDisplayManager) {
        this.shop = shop;
        this.playerManager = playerManager;
        this.textDisplayManager = textDisplayManager;
    }

    @Spawns("shop")
    public Entity shop(SpawnData data) {
        VBox shopUI = new VBox(10);
        shopUI.setTranslateX(20);
        shopUI.setTranslateY(120);

        for (ShopItem item : shop.getItems()) {
            VBox itemBox = new VBox(5);
            Text itemName = new Text("Item: " + item.getName());
            Text itemPrice = new Text("Price: " + item.getPrice() + " points");
            Text itemDescription = new Text("Description: " + item.getDescription());
            Button buyButton = new Button("Buy");

            buyButton.setOnAction(e -> {
                handleItemPurchase(item);
            });

            itemBox.getChildren().addAll(itemName, itemPrice, itemDescription, buyButton);
            shopUI.getChildren().add(itemBox);
        }

        return FXGL.entityBuilder(data).view(shopUI).build();
    }

    @Override
    public void spawn(boolean isNewGame) {
        super.spawn(isNewGame);
        FXGL.spawn("shop");
    }

    private void handleItemPurchase(ShopItem item) {
        Player currentPlayer = playerManager.getCurrPlayer();
        if (currentPlayer.getPoints() >= item.getPrice()) {
            currentPlayer.setPoints(currentPlayer.getPoints() - item.getPrice());
            if (item.getType() == ItemType.SHIELD) {
                currentPlayer.setShield(true);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Purchase Successful");
                alert.setHeaderText(null);
                alert.setContentText("You have successfully bought: " + item.getName());
                alert.showAndWait();
            }
            // Handle other item types...

            // Update the points display
            textDisplayManager.updatePointsDisplay(currentPlayer.getId(), currentPlayer.getPoints());
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Purchase Failed");
            alert.setHeaderText(null);
            alert.setContentText("You do not have enough points to buy: " + item.getName());
            alert.showAndWait();
        }
    }
}
