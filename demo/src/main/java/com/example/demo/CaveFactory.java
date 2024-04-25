package com.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class CaveFactory implements EntityFactory {
    private final int CAVE_RADIUS = 15;
    private final int NUM_PLAYERS;
    private static final int CAVE_OFFSET = 35;
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
        double tokenRadius = circle_radius + CAVE_OFFSET;

        int appCentreX = FXGL.getAppWidth() / 2;
        int appCentreY = FXGL.getAppHeight() / 2;

        // Calculate the angle increment between each token position
        double angleIncrement = 360.0 / NUM_PLAYERS;

        for (int i = 0; i < NUM_PLAYERS; i++) {
            // Calculate the angle, x, and y coordinates for the token position
            double angle = Math.toRadians(i * angleIncrement);
            double x = appCentreX + tokenRadius * Math.cos(angle);
            double y = appCentreY + tokenRadius * Math.sin(angle);

            Circle cave = new Circle(x,y,CAVE_RADIUS);

            cave.setFill(COLOURS[i]); // Provide the path to your PNG image
            cave.setStroke(Color.BLACK);
            cave.setStrokeWidth(1);


            // Add the token view to the group
            tokenGroup.getChildren().add(cave);
        }
        return FXGL.entityBuilder(data).type(EntityType.CAVE).view(tokenGroup).build();

    }
}
