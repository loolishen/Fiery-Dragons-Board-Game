package com.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.Objects;


public class DragonTokenFactory extends Spawnable {
    private final double volcanoCircleRadius;
    private final Player[] players; // a reference to the players of the game round
    private static final String[] DRAGON_TOKEN_IMAGE_PATHS = {"/com/example/demo/assets/bluedragon.png", "/com/example/demo/assets/greenDragon.png",
            "/com/example/demo/assets/redDragon.png","/com/example/demo/assets/yellowDragon.png"};

    public DragonTokenFactory(double newVolcanoCircleRadius, Player[] newPlayers){
        volcanoCircleRadius = newVolcanoCircleRadius;
        players = newPlayers;
    }

    @Spawns("dragonToken")
    public Entity dragonTokens(SpawnData data){
        Group tokenGroup = new Group();
        // Calculate the radius for positioning the tokens
        double tokenRadius = volcanoCircleRadius + Constants.TOKEN_PATH_RADIUS_OFFSET;

        int appCentreX = Constants.SCENE_WIDTH / 2;
        int appCentreY = Constants.SCENE_HEIGHT / 2;

        // Calculate the angle increment between each token position
        double angleIncrement = 360.0 / Constants.NUM_PLAYERS;
        int nextPlayerAt = 2; // volcano card id
        for (int i = 0; i < Constants.NUM_PLAYERS; i++) {
            // Calculate the angle, x, and y coordinates for the token position
            double angle = Math.toRadians(i * angleIncrement+Constants.ANGLE_OFFSET);
            double x = appCentreX + tokenRadius * Math.cos(angle);
            double y = appCentreY + tokenRadius * Math.sin(angle);
            Rectangle cardRect = new Rectangle(x - Constants.TOKEN_WIDTH / 2.0, y - Constants.TOKEN_HEIGHT / 2.0, Constants.TOKEN_WIDTH, Constants.TOKEN_HEIGHT);

            // get image path
            Image image = new Image(Objects.requireNonNull(getClass().getResource(DRAGON_TOKEN_IMAGE_PATHS[i])).toExternalForm());
            cardRect.setFill(new ImagePattern(image)); // Provide the path to your PNG image
            cardRect.setStroke(Color.BLACK);
            cardRect.setStrokeWidth(1);

            // TODO: init below managed by a central class in charge of init player,dragon token and cave?
            Volcano volcanoRing = data.get("volcano");
            // set occupied status to True at the start
            volcanoRing.getVolcanoCardByID(nextPlayerAt).setOccupied(true);
            DragonToken dragonToken = new DragonToken(volcanoRing.getVolcanoCardByID(nextPlayerAt), cardRect, tokenRadius, angle);
            players[i].setDragonToken(dragonToken);
            // Add the token view to the group
            tokenGroup.getChildren().add(cardRect);
            nextPlayerAt += 6;
        }
        return FXGL.entityBuilder(data).view(tokenGroup).build();

    }

    @Override
    public void spawn() {
        super.spawn();
        spawnData.put("volcano", VolcanoRingFactory.volcanoRing);
        FXGL.spawn("dragonToken", this.spawnData);
    }
}
