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
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.Map;

import static com.example.demo.AnimalType.*;
import static java.util.Map.entry;

public class VolcanoRingFactory implements EntityFactory {
    private final static String[] animalImagePaths = {"/com/example/demo/assets/salamander", "/com/example/demo/assets/bat",
            "/com/example/demo/assets/spider","/com/example/demo/assets/babyDragon","/com/example/demo/assets/skull" };
    private static final AnimalType[] animals= {SALAMANDER,SPIDER, BAT, BABY_DRAGON, SALAMANDER, BAT, SPIDER, SALAMANDER, BABY_DRAGON,
            BAT, BABY_DRAGON, SALAMANDER, BAT, SPIDER, BABY_DRAGON, SALAMANDER, BABY_DRAGON,
            SPIDER, BABY_DRAGON, BAT, SPIDER, SPIDER, BAT,SALAMANDER };

    private static final Map<AnimalType, String> animalImagePathMapping
          =  Map.ofEntries(entry(SALAMANDER, "/com/example/demo/assets/salamander"),
            entry(SPIDER, "/com/example/demo/assets/spider"),
            entry(BAT, "/com/example/demo/assets/bat"),
            entry(BABY_DRAGON,"/com/example/demo/assets/babyDragon" ));

    private static final int SEGMENT_LENGTH = 3;
    private static final int NUM_CARDS = 24;

    private static final int NUM_SEGMENTS = NUM_CARDS/SEGMENT_LENGTH;
    private static final int ROTATION_AMOUNT = 360/NUM_CARDS;
    public static final double CIRCLE_RADIUS = 200;
    private static final double CARD_WIDTH = 40;
    private static final double CARD_HEIGHT = 60;
    public VolcanoRingFactory(){
        //TODO: ADD more logic for 1/2/3 players
    }

    @Spawns("volcanoRing")
    public Entity volcanoRing(SpawnData data){
        // Create a group to hold the cards
        Group cardGroup = new Group();
        VolcanoSegment[] volcanoSegments = data.get("segments");
        ArrayList<Integer> arrangement = data.get("arrangement");
        System.out.println(arrangement.toString());
        // Create a circle to represent the ring
        int appCentreX = FXGL.getAppWidth()/2;
        int appCentreY = FXGL.getAppHeight()/2;
        // Calculate the angle increment between each card position
        double angleIncrement = 360.0 / NUM_CARDS;
        int firstRotationAngle = 90;

        int offset = 2;
        int cardCounter = 0;
        for (int i=1; i<NUM_SEGMENTS+1;i++){

            VolcanoSegment segment;
            if (i%2==0){

                System.out.println("i is " + i + " Choosing segment "+(arrangement.get(i-offset)));
                segment =  volcanoSegments[arrangement.get(i-offset)-1];
                offset += 1;

            }else{
                System.out.println("i is " + i + " Choosing segment "+i);
                segment = volcanoSegments[i-1];
            }
            for (int j=0; j<SEGMENT_LENGTH; j++){
                // Calculate the angle for this card position
                double angle = Math.toRadians(cardCounter * angleIncrement);

                // Calculate the x and y coordinates for the card position. Adjust if needed
                double x = appCentreX+CIRCLE_RADIUS * Math.cos(angle);
                double y = appCentreY+CIRCLE_RADIUS * Math.sin(angle);
                Rectangle rect = new Rectangle(x - CARD_WIDTH / 2, y - CARD_HEIGHT / 2, CARD_WIDTH, CARD_HEIGHT);
                String imagePath = animalImagePathMapping.get(segment.getSegmentCards()[j].getAnimal());
                Image image = new Image(getClass().getResource(imagePath+"1.png").toExternalForm());
                rect.setFill(new ImagePattern(image)); // Provide the path to your PNG image
                rect.setStroke(Color.BLACK);
                rect.setStrokeWidth(1);
                // rotate
                Rotate rotate = new Rotate();
                rotate.setAngle(firstRotationAngle+(cardCounter*ROTATION_AMOUNT));
                rotate.setPivotX(x);
                rotate.setPivotY(y);
                rect.getTransforms().addAll(rotate);
                System.out.println("Adding "+ segment.getSegmentCards()[j].getAnimal());
                cardGroup.getChildren().add(rect);
                Volcano volcano = data.get("volcano");

                volcano.getVolcanoRing()[cardCounter] = new VolcanoCard(i, cardCounter+1, segment.getSegmentCards()[j].getAnimal());
                cardCounter += 1;
            }

        }

        System.out.println("The updated volcano ring is ");
        Volcano volcano = data.get("volcano");
        for (VolcanoCard card: volcano.getVolcanoRing()){
            System.out.println(card.getAnimal());
        }


        return FXGL.entityBuilder(data).type(EntityType.VOLCANO_RING).view(cardGroup).build();
    }

/**
 * Populates the volcano's volcanoCard array with the cards
 */
    public VolcanoSegment[] createSegments(Volcano volcano){

        VolcanoSegment[] segments = new VolcanoSegment[NUM_SEGMENTS];
        int hasCaveSegmentIDs = 1;
        int segmentID = 1;
        VolcanoSegment volcanoSegment = new VolcanoSegment(segmentID, SEGMENT_LENGTH);
        segments[0] = volcanoSegment;
        int segmentEnd = 2;
        int ringID = 0;
        int hasCaveIDs = 1;
        for (int i=0; i<NUM_CARDS; i++){

            if (i!=0 && i%(SEGMENT_LENGTH)==0){
                segmentID += 1;
                volcanoSegment = new VolcanoSegment(segmentID, SEGMENT_LENGTH);
            }
            System.out.println("New volcano card created for segment " +segmentID+ " and id "+(i+1)+" with animal "+animals[i]);
            VolcanoCard newVolcanoCard = new VolcanoCard(segmentID, i+1, animals[i]);
            //add volcano card to ring
           volcano.getVolcanoRing()[ringID] = newVolcanoCard;
           ringID+=1;

            volcanoSegment.addVolcanoCard(newVolcanoCard);

            if (i==segmentEnd){
                segments[segmentID-1] = volcanoSegment;
                segmentEnd += SEGMENT_LENGTH;
            }

            if (segmentID==hasCaveSegmentIDs){
                volcanoSegment.setHasCave(true);
                hasCaveSegmentIDs+=2;
            }

            if (i==hasCaveIDs){ // logic to compute cards with cave
                newVolcanoCard.setCave(true);
                hasCaveIDs+=6;
            }
        }

        return  segments;
    }

}
