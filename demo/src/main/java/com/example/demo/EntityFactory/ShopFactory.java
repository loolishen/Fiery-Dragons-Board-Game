package com.example.demo.EntityFactory;


import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.example.demo.Controller.PlayerTurnManager;
import com.example.demo.Controller.TextDisplayManager;
import com.example.demo.Model.Player;
import com.example.demo.Model.Shop;
import com.example.demo.Model.ShopItem;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Objects;

/**
 * Shop factory to spawn the components of the shop
 */
public class ShopFactory extends SpawnFactory {
    private final Shop shop; // the shop (model)
    private final String SHIELD_TYPE = "SHIELD"; // currently we have one shop item type, for future extensions we could
    // have even more items with varying bonuses

    PlayerTurnManager playerManager;

    TextDisplayManager textDisplayManager;

    public ShopFactory(Shop shop, PlayerTurnManager playerManager, TextDisplayManager textDisplayManager) {
        this.shop = shop;
        this.playerManager = playerManager;
        this.textDisplayManager = textDisplayManager;
    }

    /**
     * Creates the visuals for the shop which contain the ShopItem name, the price and the description, as well
     * as a 'Buy' button to purchase the item.
     */
    @Spawns("shop")
    public Entity shop(SpawnData data) {
        VBox shopUI = new VBox(10);
        shopUI.setTranslateX(20);
        shopUI.setTranslateY(550);

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

    /**
     * Handles click of the 'buy' button. Insufficient points means no effect.
     * Otherwise, reduces the player's points.
     * Makes use of FXGL's notification service to provide helpful feedback to the player
     * @param item
     */
    private void handleItemPurchase(ShopItem item) {
        Player currentPlayer = playerManager.getCurrPlayer();
        if (currentPlayer.getPoints() >= item.getPrice()) {
            currentPlayer.setPoints(currentPlayer.getPoints() - item.getPrice());
            if (Objects.equals(item.getName(), SHIELD_TYPE)) {
                currentPlayer.setShield(true);
                FXGL.getNotificationService().pushNotification("You have successfully bought: " + item.getName());
            }

            // Update the points display
            textDisplayManager.removeOldPointsMsg(textDisplayManager.getCurrentPointsEntity());
            textDisplayManager.updatePointsDisplay(currentPlayer.getId(), currentPlayer.getPoints());
        } else {
            FXGL.getNotificationService().pushNotification("You do not have enough points to buy: " + item.getName());
        }
    }

}
