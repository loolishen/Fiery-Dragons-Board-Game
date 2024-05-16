package com.example.demo.EntityFactory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.example.demo.Config;
import com.example.demo.Controller.PlayerTurnManager;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.util.Objects;

/**
 * Used to spawn the caves according to the number of players playing
 */
public class CaveFactory extends SpawnFactory {

    private final double circle_radius;

    public CaveFactory(double newCircleRadius){
        circle_radius = newCircleRadius;
    }

    @Spawns("cave")
    public Entity cave(SpawnData data){

        Group tokenGroup = new Group();
        // Calculate the radius for positioning the tokens
        double tokenRadius = circle_radius + Config.CAVE_POS_RADIUS_OFFSET;

        int appCentreX = Config.SCENE_WIDTH / 2;
        int appCentreY = Config.SCENE_HEIGHT / 2;

        // Calculate the angle increment between each token position
        double angleIncrement = 360.0 / Config.NUM_PLAYERS;

        for (int i = 0; i < Config.NUM_PLAYERS; i++) {
            // Calculate the angle, x, and y coordinates for the token position
            double angle = Math.toRadians(i * angleIncrement+ Config.ANGLE_OFFSET);
            double x = appCentreX + tokenRadius * Math.cos(angle);
            double y = appCentreY + tokenRadius * Math.sin(angle);

            Circle cave = createCaveShapes(x,y,i);
            // Add the token view to the group
            tokenGroup.getChildren().add(cave);

        }
        return FXGL.entityBuilder(data).view(tokenGroup).build();

    }

    @Override
    public void spawn() {
        super.spawn();
        FXGL.spawn("cave");
    }

    private Circle createCaveShapes(double x, double y, int i){
        Circle cave = new Circle(x,y, Config.CAVE_CIRCLE_RADIUS);
        String imagePathPrefix = Config.ANIMAL_IMAGE_IMAGE_PATH_PREFIX_MAPPINGS.get(PlayerTurnManager.getPlayers()[i].getAnimalType());
        Image animalImage = new Image(Objects.requireNonNull(getClass().getResource(imagePathPrefix  + "1.png")).toExternalForm());
        ImagePattern newImgPat = new ImagePattern(animalImage);
        cave.setFill(newImgPat); // Provide the path to your PNG image
        cave.setStroke(Config.COLORS[i]);
        cave.setStrokeWidth(3);
        return cave;
    }

}
