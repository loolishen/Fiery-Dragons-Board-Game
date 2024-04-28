package com.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.example.demo.utils.Utils;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Spawns the volcano ring of cards consisting of alternating cut and uncut segments, incorporating random arrangement of segments.
 */
public class VolcanoRingFactory extends SpawnFactory {
    public static final VolcanoCard[] volcanoRing = new VolcanoCard[Constants.VOLCANO_RING_NUM_CARDS];
    private final int segmentLength;
    private final int numCards;
    private final int numSegments;
    private final double rotationAmount;
    public static final double CIRCLE_RADIUS = Constants.VOLCANO_RING_RADIUS;
    private static final double CARD_WIDTH = Constants.VOLCANO_CARD_WIDTH;
    private static final double CARD_HEIGHT = Constants.VOLCANO_CARD_HEIGHT;
    public VolcanoRingFactory(int newNumCards, int newNumSegments){
        numCards = newNumCards;
        numSegments = newNumSegments;
        segmentLength = newNumCards/newNumSegments;
        rotationAmount = 360.0/numCards;
    }
    public static VolcanoCard getVolcanoCardByID(int id){
        return volcanoRing[id-1]; // our ID starts from 1 but array indexing is 0
    }

    public static VolcanoCard[] getVolcanoRing() {
        return volcanoRing;
    }

    @Spawns("volcanoRing")
    public Entity volcanoRing(SpawnData data){
        // Create a group to hold the cards
        Group cardGroup = new Group();
        VolcanoSegment[] volcanoSegments = data.get("segments");
        ArrayList<Integer> arrangement = generateRandomUncutSegmentSequence();
        // Create a circle to represent the ring
        int appCentreX = Constants.SCENE_WIDTH/2;
        int appCentreY = Constants.SCENE_HEIGHT/2;
        // Calculate the angle increment between each card position
        int firstRotationAngle = 90; //we start with
        int offset = 2;
        int cardCounter = 0;
        for (int i=1; i<numSegments+1;i++){
            VolcanoSegment segment;
            if (i%2==0){
                segment =  volcanoSegments[arrangement.get(i-offset)-1];
                offset += 1;

            }else{
                segment = volcanoSegments[i-1];
            }
            for (int j=0; j<segmentLength; j++){
                // Calculate the angle for this card position
                double angle = Math.toRadians(cardCounter * rotationAmount);

                // Calculate the x and y coordinates for the card position. Adjust if needed
                double x = appCentreX+CIRCLE_RADIUS * Math.cos(angle);
                double y = appCentreY+CIRCLE_RADIUS * Math.sin(angle);
                Rectangle rect = new Rectangle(x - CARD_WIDTH / 2, y - CARD_HEIGHT / 2, CARD_WIDTH, CARD_HEIGHT);

                String imagePathPrefix = Constants.ANIMAL_IMAGE_IMAGE_PATH_PREFIX_MAPPINGS.get(segment.getSegmentCards()[j].getAnimal());
                Image image = new Image(Objects.requireNonNull(getClass().getResource(imagePathPrefix + "1.png")).toExternalForm());
                rect.setFill(new ImagePattern(image)); // Provide the path to your PNG image
                rect.setStroke(Color.BLACK);
                rect.setStrokeWidth(1);
                // rotate
                Rotate rotate = new Rotate();
                rotate.setAngle(firstRotationAngle+(cardCounter*rotationAmount));
                rotate.setPivotX(x);
                rotate.setPivotY(y);
                rect.getTransforms().addAll(rotate);
                cardGroup.getChildren().add(rect);

                volcanoRing[cardCounter] = new VolcanoCard(i, cardCounter+1, segment.getSegmentCards()[j].getAnimal());
                cardCounter += 1;
            }

        }

        return FXGL.entityBuilder(data).view(cardGroup).build();
    }

    /**
     * This method creates the 8 VolcanoSegments consisting of 3 VolcanoCards used in the Fiery Dragons default board (Refer to Picture in ED post: https://edstem.org/au/courses/14452/discussion/1786569)
     * Therefore, according to the image in ED, 4 segments are fixed to have caves (cut), and here they are segments 1,3,5 and 7, so 2,4,6, and 8
     * don't have caves (uncut)
     * Connascence of Execution: Obviously this must be called before spawning the Volcano Cards because the VolcanoCard objects only exists after this function is called
     */
    public VolcanoSegment[] createSegments(){

        VolcanoSegment[] segments = new VolcanoSegment[numSegments];
        int hasCaveSegmentIDs = 1;
        int segmentID = 1;
        VolcanoSegment volcanoSegment = new VolcanoSegment(segmentID, segmentLength);
        segments[0] = volcanoSegment;
        int segmentEnd = 2;
        int hasCaveIDs = 1;
        for (int i=0; i<numCards; i++){

            if (i!=0 && i%(segmentLength)==0){
                segmentID += 1;
                volcanoSegment = new VolcanoSegment(segmentID, segmentLength);
            }
            VolcanoCard newVolcanoCard = new VolcanoCard(segmentID, i+1, Constants.ANIMALS[i]);

            volcanoSegment.addVolcanoCard(newVolcanoCard);

            if (i==segmentEnd){
                segments[segmentID-1] = volcanoSegment;
                segmentEnd += segmentLength;
            }

            if (segmentID==hasCaveSegmentIDs){
                volcanoSegment.setHasCave(true);
                hasCaveSegmentIDs+=2;
            }

            if (i==hasCaveIDs){ // logic to compute cards with cave
                hasCaveIDs+=6;
            }
        }

        return  segments;
    }

    /**
     * Gets random ordering of uncut segments. Currently only supports one configuration
     * @return Random ordering of the uncut segments
     */
    private ArrayList<Integer> generateRandomUncutSegmentSequence(){
        // e.g. 1,3,5,7 are the segments that have cave
        ArrayList<Integer> randomArrangements = new ArrayList<>(Arrays.asList(2,4,6,8));
//        Utils.shuffleIntArray(randomArrangements, Constants.RNG_SEED);
        Utils.shuffleIntArray(randomArrangements, Constants.RNG_SEED);

        return randomArrangements;
    }

    @Override
    public void spawn() {
        super.spawn();
        VolcanoSegment[] segments = this.createSegments(); // TODO: this method creates all the volcano cards within their segments, what pattern?
        // random segments arrangements. remember that segments are alternately placed, so randomising is for position 2,4,6,8
        spawnData.put("segments", segments); // pass the information to the factory so that it can spawn based on the segments
        FXGL.spawn("volcanoRing", spawnData); // this also updates volcano ring
    }
}
