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


public class ChitCardFactory implements EntityFactory {
    private static final int NUM_CHIT_CARDS = 16;
    private Circle[] uncoveredChitCardShapes = new Circle[16]; // initialized with reference from FieryDragonsApplication class
    private Circle[] coveredChitCardShapes = new Circle[16]; // initialized with reference from FieryDragonsApplication class

    private  static HashMap<Circle, ChitCard> viewControllerMapping = new HashMap<>() {
    };

    public ChitCardFactory(Circle[] uncoveredChitCardShapesRef, Circle[] coveredChitCardShapesRef){
        uncoveredChitCardShapes = uncoveredChitCardShapesRef;
        coveredChitCardShapes = coveredChitCardShapesRef;
    }

    @Spawns("chitCard")
    public Entity newChitCard(SpawnData data){

        Circle defaultChitCardShape = new Circle(28);
        Image image = new Image(getClass().getResource("/com/example/demo/assets/chitCard.png").toExternalForm());
        defaultChitCardShape.setFill(new ImagePattern(image));
        defaultChitCardShape.setStrokeWidth(2); // Set the thickness to 2 pixels
        defaultChitCardShape.setStroke(Color.BLACK); // Set the outline color to black
        int index = data.get("idx");
        coveredChitCardShapes[index] = defaultChitCardShape;
        // get animal type and count
        AnimalType animalType = data.get("animalType");
        int animalCount = data.get("animalCount");

        // get the uncoveredShape from SpawnData
        Circle uncoveredShape = data.get("uncoveredShape");

        ChitCard chitCard = new ChitCard(index, animalType,animalCount, defaultChitCardShape, uncoveredShape, data);
        viewControllerMapping.put(defaultChitCardShape, chitCard);
        viewControllerMapping.put(uncoveredShape, chitCard);
        coveredChitCardShapes[index].setVisible(true);
        return FXGL.entityBuilder(data).type(EntityType.CHIT_CARD).view(defaultChitCardShape).build();
    }


    @Spawns("uncoveredChitCard")
    public Entity newUncoveredChitCard(SpawnData data){
        Circle uncoveredChitCardShape = data.get("uncoveredShape");
        int index = data.get("idx");
        uncoveredChitCardShapes[index] = uncoveredChitCardShape;
        uncoveredChitCardShapes[index].setVisible(false);
        return FXGL.entityBuilder(data).view(uncoveredChitCardShape).build();
    }

    public Circle[] getCoveredChitCards() {
        return coveredChitCardShapes;
    }

    public Circle[] getUncoveredChitCardShapes(){
        return uncoveredChitCardShapes;
    }

    public static HashMap<Circle, ChitCard> getViewControllerMapping(){
        return viewControllerMapping;
    }
}
