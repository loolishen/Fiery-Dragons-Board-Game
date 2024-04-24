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
import java.util.HashMap;
import java.util.Map;


public class ChitCardFactory implements EntityFactory {
    private static final int NUM_CHIT_CARDS = 16;
    private Circle[] chitCards = new Circle[16];

    private  static HashMap<Circle, ChitCard> viewControllerMapping = new HashMap<>() {
    };

    @Spawns("chitCard")
    public Entity newChitCard(SpawnData data){

        Circle defaultChitCardShape = new Circle(20, Color.LIGHTBLUE);
        defaultChitCardShape.setStrokeWidth(2); // Set the thickness to 2 pixels
        defaultChitCardShape.setStroke(Color.LIGHTBLUE); // Set the outline color to black
        int index = data.get("idx");
        chitCards[index] = defaultChitCardShape;
        // get animal type and count
        AnimalType animalType = data.get("animalType");
        int animalCount = data.get("animalCount");
        //get index

        // Get the image path from the SpawnData
        String imagePath = data.get("imagePath");
        Image image = new Image(getClass().getResource(imagePath).toExternalForm());

        Circle newChitCardShape = new Circle(20);
        newChitCardShape.setStrokeWidth(2);
        newChitCardShape.setStroke(Color.LIGHTBLUE);
        newChitCardShape.setFill(new ImagePattern(image)); // Provide the path to your PNG image

        ChitCard chitCard = new ChitCard(animalType,animalCount, defaultChitCardShape, newChitCardShape, data);
        viewControllerMapping.put(defaultChitCardShape, chitCard);
        viewControllerMapping.put(newChitCardShape, chitCard);
        return FXGL.entityBuilder(data).view(defaultChitCardShape).build();
    }


    @Spawns("uncoveredChitCard")
    public Entity newUncoveredChitCard(SpawnData data){
        Circle uncoveredChitCardShape = data.get("uncoveredShape");
//        Circle defaultChitCardShape = new Circle(20, Color.LIGHTBLUE);
//        defaultChitCardShape.setStrokeWidth(2); // Set the thickness to 2 pixels
//        defaultChitCardShape.setStroke(Color.LIGHTBLUE); // Set the outline color to black
        int index = data.get("idx");
        chitCards[index] = uncoveredChitCardShape;
//        // get animal type and count
//        AnimalType animalType = data.get("animalType");
//        int animalCount = data.get("animalCount");
//        //get index
//
//        // Get the image path from the SpawnData
//        String imagePath = data.get("imagePath");
//        Image image = new Image(getClass().getResource(imagePath).toExternalForm());
//
//        Circle newChitCardShape = new Circle(20);
//        newChitCardShape.setStrokeWidth(2);
//        newChitCardShape.setStroke(Color.LIGHTBLUE);
//        newChitCardShape.setFill(new ImagePattern(image)); // Provide the path to your PNG image
//
//        ChitCard chitCard = new ChitCard(animalType,animalCount, defaultChitCardShape, newChitCardShape, data);
//        viewControllerMapping.put(defaultChitCardShape, chitCard);
//        viewControllerMapping.put(newChitCardShape, chitCard);
        return FXGL.entityBuilder(data).view(uncoveredChitCardShape).build();
    }

    public Circle[] getChitCards() {
        return chitCards;
    }

    public static HashMap<Circle, ChitCard> getViewControllerMapping(){
        return viewControllerMapping;
    }
}
