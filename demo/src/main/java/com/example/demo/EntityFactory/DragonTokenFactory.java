package com.example.demo.EntityFactory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.example.demo.Config;
import com.example.demo.Controller.PlayerTurnManager;
import com.example.demo.InitModel;
import com.example.demo.Model.DragonToken;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Objects;

/**
 * Used to create the dragon tokens, also sets the player's dragon token
 * @author Loo Li Shen
 */
public class DragonTokenFactory extends SpawnFactory implements InitModel {

    private static final String[] DRAGON_TOKEN_IMAGE_PATHS = {"/com/example/demo/assets/bluedragon.png", "/com/example/demo/assets/greenDragon.png",
            "/com/example/demo/assets/redDragon.png","/com/example/demo/assets/yellowDragon.png"};

    @Spawns("dragonToken")
    public Entity dragonTokens(SpawnData data){
        Group tokenGroup = new Group();
        // Calculate the radius for positioning the tokens
        double tokenRadius = Config.VOLCANO_RING_RADIUS + Config.TOKEN_PATH_RADIUS_OFFSET;

        int appCentreX = Config.SCENE_WIDTH / 2;
        int appCentreY = Config.SCENE_HEIGHT / 2;

        // Calculate the angle increment between each token position
        double angleIncrement = 360.0 / Config.NUM_PLAYERS;
        int nextPlayerAt = 1; // volcano card id
        for (int i = 0; i < Config.NUM_PLAYERS; i++) {
            // Calculate the angle, x, and y coordinates for the views
            double angle = Math.toRadians(i * angleIncrement+ Config.ANGLE_OFFSET);
            double x = appCentreX + tokenRadius * Math.cos(angle);
            double y = appCentreY + tokenRadius * Math.sin(angle);

            // token image
            Rectangle cardRect = createTokenImage(x,y,i);
            // player label
            Text playerID = createPlayerIDLabel(x,y,i);

            // initialise the model (the volcano cards where the token is supposed to be is set to 'occupied', and we bind the token
            // to the player
            data.put("nextPlayerAt", nextPlayerAt);
            data.put("cardRect", cardRect);
            data.put("idx", i);
            data.put("text", playerID);
            data.put("angle", angle);
            createModel(data);

            // Add the views to the group
            tokenGroup.getChildren().add(cardRect);
            tokenGroup.getChildren().add(playerID);

            // update index of volcano card where the next player should be
            nextPlayerAt += Config.SPACE_BETWEEN_PLAYERS+1;

        }
        return FXGL.entityBuilder(data).view(tokenGroup).build();

    }

    @Override
    public void spawn() {
        super.spawn();
        FXGL.spawn("dragonToken", this.spawnData);
    }

    @Override
    public void createModel(SpawnData spawnData) {
        int nextPlayerAt = spawnData.get("nextPlayerAt");
        Rectangle cardRect = spawnData.get("cardRect");
        double angle = spawnData.get("angle");
        Text playerIDText = spawnData.get("text");
        int i = spawnData.get("idx");

        // set occupied status to True at the start
        VolcanoRingFactory.getVolcanoCardByID(nextPlayerAt).setOccupied(true);

        // create token and assign it to player
        DragonToken dragonToken = new DragonToken(VolcanoRingFactory.getVolcanoCardByID(nextPlayerAt), cardRect, angle, playerIDText);
        PlayerTurnManager.getPlayers()[i].setDragonToken(dragonToken);
    }

    private Text createPlayerIDLabel(double x, double y, int id){
        Text playerID = new Text(""+(id+1) + "P");
        playerID.setX(x - (Config.TOKEN_WIDTH / 2.0) + 30);
        playerID.setY(y - (Config.TOKEN_HEIGHT / 2.0) );
        playerID.setFill(Config.COLORS[id]);
        playerID.setFont(Font.font("Arial", 30));
        return playerID;
    }

    private Rectangle createTokenImage(double x, double y, int idx){
        Rectangle cardRect = new Rectangle(x - Config.TOKEN_WIDTH / 2.0, y - Config.TOKEN_HEIGHT / 2.0, Config.TOKEN_WIDTH, Config.TOKEN_HEIGHT);
        // get image path
        Image image = new Image(Objects.requireNonNull(getClass().getResource(DRAGON_TOKEN_IMAGE_PATHS[idx])).toExternalForm());
        cardRect.setFill(new ImagePattern(image)); // Provide the path to your PNG image
        cardRect.setStroke(Color.BLACK);
        cardRect.setStrokeWidth(1);
        return cardRect;
    }
}
