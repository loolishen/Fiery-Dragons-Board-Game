package com.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class DragonTokenFactory implements EntityFactory {
    private final int NUM_PLAYERS;
    private static final int TOKEN_OFFSET = 65;
    private final double circle_radius;
    private Player[] players;
    private final static String[] dragonTokenImgPaths = {"/com/example/demo/assets/bluedragon.png", "/com/example/demo/assets/greenDragon.png",
            "/com/example/demo/assets/redDragon.png","/com/example/demo/assets/yellowDragon.png"};

    public DragonTokenFactory(int num_players, double newCircleRadius, Player[] newPlayers){
        NUM_PLAYERS = num_players;
        circle_radius = newCircleRadius;
        players = newPlayers;
    }

    @Spawns("dragonToken")
    public Entity dragonTokens(SpawnData data){
        Group tokenGroup = new Group();
        // Calculate the radius for positioning the tokens
        double tokenRadius = circle_radius + TOKEN_OFFSET;

        int appCentreX = FXGL.getAppWidth() / 2;
        int appCentreY = FXGL.getAppHeight() / 2;

        // Calculate the angle increment between each token position
        double angleIncrement = 360.0 / NUM_PLAYERS;
        int nextPlayerAt = 2; // volcano card id
        for (int i = 0; i < NUM_PLAYERS; i++) {
            // Calculate the angle, x, and y coordinates for the token position
            double angle = Math.toRadians(i * angleIncrement+15);
            double x = appCentreX + tokenRadius * Math.cos(angle);
            double y = appCentreY + tokenRadius * Math.sin(angle);

            Rectangle cardRect = new Rectangle(x - 40 / 2, y - 60 / 2, 25, 45);

            // get image path
            Image image = new Image(getClass().getResource(dragonTokenImgPaths[i]).toExternalForm());
            cardRect.setFill(new ImagePattern(image)); // Provide the path to your PNG image
            cardRect.setStroke(Color.BLACK);
            cardRect.setStrokeWidth(1);

            Volcano volcanoRing = data.get("caveID");
            // set occupied status to True at the start
            volcanoRing.getVolcanoCardByID(nextPlayerAt).setOccupied(true);
            DragonToken dragonToken = new DragonToken(volcanoRing.getVolcanoCardByID(nextPlayerAt), cardRect, tokenRadius, ((i)*90.0+15)*Math.PI/180); // [0,2,4,6] refactor based on volcanoringfactory's hasCaveIndexes variable
            players[i].setDragonToken(dragonToken);
            // Add the token view to the group
            tokenGroup.getChildren().add(cardRect);
            nextPlayerAt += 6;
        }
        return FXGL.entityBuilder(data).type(EntityType.DRAGON_TOKEN).view(tokenGroup).build();

    }
}
