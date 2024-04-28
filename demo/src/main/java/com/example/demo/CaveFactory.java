package com.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Used to spawn the caves according to the number of players playing
 */
public class CaveFactory extends SpawnFactory {

    private final int NUM_PLAYERS;

    private final double circle_radius;
    private static final Color[] COLOURS = {Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW};


    public CaveFactory(int num_players, double newCircleRadius){
        NUM_PLAYERS = num_players;
        circle_radius = newCircleRadius;
    }

    @Spawns("cave")
    public Entity cave(SpawnData data){
        Group tokenGroup = new Group();
        // Calculate the radius for positioning the tokens
        double tokenRadius = circle_radius + Constants.CAVE_POS_RADIUS_OFFSET;

        int appCentreX = Constants.SCENE_WIDTH / 2;
        int appCentreY = Constants.SCENE_HEIGHT / 2;

        // Calculate the angle increment between each token position
        double angleIncrement = 360.0 / NUM_PLAYERS;

        for (int i = 0; i < NUM_PLAYERS; i++) {
            // Calculate the angle, x, and y coordinates for the token position
            double angle = Math.toRadians(i * angleIncrement+Constants.ANGLE_OFFSET);
            double x = appCentreX + tokenRadius * Math.cos(angle);
            double y = appCentreY + tokenRadius * Math.sin(angle);

            // TODO: consider refactoring into a general CircleComponent class shared with chit cards
            Circle cave = new Circle(x,y,Constants.CAVE_CIRCLE_RADIUS);
            cave.setFill(COLOURS[i]); // Provide the path to your PNG image
            cave.setStroke(Color.BLACK);
            cave.setStrokeWidth(1);
            // decision to put it here instead of text factory: here makes more sense since it is tied to the cave
            Text playerID = new Text(""+(i+1));
            playerID.setX(x-Constants.CAVE_TEXT_OFFSET);
            playerID.setY(y+Constants.CAVE_TEXT_OFFSET);
            playerID.setFont(Font.font("Arial", 24));

            // Add the token view to the group
            tokenGroup.getChildren().add(cave);
            tokenGroup.getChildren().add(playerID);
        }
        return FXGL.entityBuilder(data).view(tokenGroup).build();

    }

    @Override
    public void spawn() {
        super.spawn();
        FXGL.spawn("cave");
    }
}
