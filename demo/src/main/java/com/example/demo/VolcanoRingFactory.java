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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import java.util.HashMap;
import java.util.Map;

import static com.example.demo.AnimalType.*;
import static java.util.Map.entry;

public class VolcanoRingFactory implements EntityFactory {
    private final static String[] animalImagePaths = {"/com/example/demo/assets/salamander", "/com/example/demo/assets/bat",
            "/com/example/demo/assets/spider","/com/example/demo/assets/babyDragon","/com/example/demo/assets/skull" };
    private static final AnimalType[] animals= {SALAMANDER, SPIDER, BAT, BABY_DRAGON, SALAMANDER, BAT, SPIDER, SALAMANDER, BABY_DRAGON,
            BAT, BABY_DRAGON, SALAMANDER, BAT, SPIDER, BABY_DRAGON, SALAMANDER, BABY_DRAGON,
            SPIDER, BABY_DRAGON, BAT, SPIDER, SPIDER, BAT,SALAMANDER };

    private static final Map<AnimalType, String> animalImagePathMapping
          =  Map.ofEntries(entry(SALAMANDER, "/com/example/demo/assets/salamander"),
            entry(SPIDER, "/com/example/demo/assets/spider"),
            entry(BAT, "/com/example/demo/assets/bat"),
            entry(BABY_DRAGON,"/com/example/demo/assets/babyDragon" ));

    private static final int SEGMENT_LENGTH = 3;
    private static final int NUM_CARDS = 24;
    private int numPlayers;
    private int[] hasCaveIndexes;
    private static final int NUM_SEGMENTS = NUM_CARDS/SEGMENT_LENGTH;
    private static final int ROTATION_AMOUNT = 360/NUM_CARDS;
    private static final double CIRCLE_RADIUS = 200;
    private static final double CARD_WIDTH = 40;
    private static final double CARD_HEIGHT = 60;
    public VolcanoRingFactory(int newNumPlayers){
        numPlayers = newNumPlayers;
        if (numPlayers == 4){
            hasCaveIndexes = new int[]{0,2,4,6};
        }
        //TODO: ADD more logic for 1/2/3 players
    }

    @Spawns("volcanoRing")
    public Entity newChitCard(SpawnData data){

        // Create a group to hold the cards
        Group cardGroup = new Group();

        // Create a circle to represent the ring
        Circle circle = new Circle(CIRCLE_RADIUS);
        int appCentreX = FXGL.getAppWidth()/2;
        int appCentreY = FXGL.getAppHeight()/2;
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);
        // Calculate the angle increment between each card position
        double angleIncrement = 360.0 / NUM_CARDS;

        int firstRotationAngle = 90;
        for (int i = 0; i < NUM_CARDS; i++) {

            // Calculate the angle for this card position
            double angle = Math.toRadians(i * angleIncrement);

            // Calculate the x and y coordinates for the card position. Adjust if needed
            double x = appCentreX+CIRCLE_RADIUS * Math.cos(angle);
            double y = appCentreY+CIRCLE_RADIUS * Math.sin(angle);

            // Create a rectangle for the card
            Rectangle cardRect = new Rectangle(x - CARD_WIDTH / 2, y - CARD_HEIGHT / 2, CARD_WIDTH, CARD_HEIGHT);

            // Get the image path
            AnimalType animalType = animals[i];
            String imagePath = animalImagePathMapping.get(animalType);
            Image image = new Image(getClass().getResource(imagePath+"1.png").toExternalForm());
            cardRect.setFill(new ImagePattern(image)); // Provide the path to your PNG image
            cardRect.setStroke(Color.BLACK);
            cardRect.setStrokeWidth(1);
            // rotate
            Rotate rotate = new Rotate();
            rotate.setAngle(firstRotationAngle+(i*ROTATION_AMOUNT));
            rotate.setPivotX(x);
            rotate.setPivotY(y);
            cardRect.getTransforms().addAll(rotate);

            // Add the card view to the group
            cardGroup.getChildren().add(cardRect);

        }

        return FXGL.entityBuilder(data).view(cardGroup).build();
    }
/**
 * Populates the volcano's volcanoCard array with the cards
 */
    public VolcanoSegment[] createSegments(Volcano volcano){

        VolcanoSegment[] segments = new VolcanoSegment[NUM_SEGMENTS];
        int segmentID = 1;
        VolcanoSegment volcanoSegment = new VolcanoSegment(segmentID, SEGMENT_LENGTH);
        segments[0] = volcanoSegment;
        int ringID = 0;
        for (int i=0; i<animals.length; i++){
            if (i!=0 && i%SEGMENT_LENGTH==0){
                segmentID += 1;
                volcanoSegment = new VolcanoSegment(segmentID, SEGMENT_LENGTH);
            }
            VolcanoCard newVolcanoCard = new VolcanoCard(segmentID, i+1, animals[i]);
            //add volcano card to ring
           volcano.getVolcanoRing()[ringID] = newVolcanoCard;
            ringID+=1;

            volcanoSegment.addVolcanoCard(newVolcanoCard);

            if (i!=0 && i%SEGMENT_LENGTH==0){
                segments[segmentID-1] = volcanoSegment;
            }

            if (i%(NUM_SEGMENTS-2)==0){ // logic to compute cards with cave
                newVolcanoCard.setCave(true);
                volcanoSegment.setHasCave(true);
            }
        }

        return  segments;
    }

}
