package com.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;


public class ChitCardFactory implements EntityFactory {

    @Spawns("chitCard")
    public Entity newChitCard(SpawnData data){
        Circle chitCardShape = new Circle(20, Color.LIGHTBLUE);
        chitCardShape.setStrokeWidth(2); // Set the thickness to 2 pixels
        chitCardShape.setStroke(Color.LIGHTBLUE); // Set the outline color to black

        // Get the image path from the SpawnData
        String imagePath = data.get("imagePath");
        Image image = new Image(getClass().getResource(imagePath).toExternalForm());

        chitCardShape.setFill(new ImagePattern(image)); // Provide the path to your PNG image

        return FXGL.entityBuilder(data).view(chitCardShape).build();
    }
}
