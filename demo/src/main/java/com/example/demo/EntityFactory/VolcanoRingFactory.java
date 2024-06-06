package com.example.demo.EntityFactory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.example.demo.Animals.Animal;
import com.example.demo.Config;
import com.example.demo.InitModel;
import com.example.demo.LoadSave;
import com.example.demo.Model.VolcanoCard;
import com.example.demo.Utils.Utils;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;


/**
 * Spawns the volcano ring of cards consisting of alternating cut and uncut segments, incorporating random arrangement of segments.
 * @author Loo Li Shen
 */
public class VolcanoRingFactory extends SpawnFactory implements InitModel, LoadSave {
    private static final VolcanoCard[] volcanoRing = new VolcanoCard[Config.VOLCANO_RING_NUM_CARDS];
    private final int segmentLength;
    private final int numCards;
    private final int numSegments;
    private final double rotationAmount;
    public VolcanoRingFactory(int newNumCards, int newNumSegments){
        numCards = newNumCards;
        numSegments = newNumSegments;
        segmentLength = newNumCards/newNumSegments;
        rotationAmount = 360.0/numCards;

    }
    public static VolcanoCard getVolcanoCardByID(int id){
        return volcanoRing[id-1]; // our ID starts from 1 but array indexing is 0
    }

    @Spawns("newVolcanoRing")
    public Entity newVolcanoRing(SpawnData data){
        // Create a group to hold the cards
        Group cardGroup = new Group();
        VolcanoSegment[] volcanoSegments = data.get("segments");

        // prepare the arrays for uncut and cut segments based on the number of segments
        ArrayList<Integer> arrangement = new ArrayList<>();
        ArrayList<Integer> arrangementOdd = new ArrayList<>();
        for (int i=1; i<numSegments+1; i++){
            boolean b = i % 2 == 0 ? arrangement.add(i) : arrangementOdd.add(i);
        }
        Utils.shuffleIntArray(arrangement, Config.RNG_SEED); // change to RNG_SEED for testing purposes
        Utils.shuffleIntArray(arrangementOdd, Config.RNG_SEED);
        // Create a circle to represent the ring
        int appCentreX = Config.SCENE_WIDTH/2;
        int appCentreY = Config.SCENE_HEIGHT/2;
        // Calculate the angle increment between each card position
        int offset = 2;
        int oddOffset = 0;
        int cardCounter = 23;
        for (int i=1; i<numSegments+1;i++){
            VolcanoSegment segment;
            // serialize the shuffled uncut and cut segments into the segments used for the volcano ring
            if (i%2==0){
                segment =  volcanoSegments[arrangement.get(i-offset)-1];
                offset += 1;
            }else{
                segment = volcanoSegments[arrangementOdd.get(i-oddOffset-1)-1];
                oddOffset += 1;
            }
            for (int j=0; j<segmentLength; j++){
                if (cardCounter == 24){cardCounter = 0;}

                // Calculate the angle for this card position
                double angle = Math.toRadians(cardCounter * rotationAmount);

                // Calculate the x and y coordinates for the card position. Adjust if needed
                double x = appCentreX+ Config.VOLCANO_RING_RADIUS * Math.cos(angle);
                double y = appCentreY+ Config.VOLCANO_RING_RADIUS * Math.sin(angle);
                Rectangle rect = createVolcanoCardShape(x,y,segment,j,cardCounter);
                cardGroup.getChildren().add(rect);

                // initialise the model: the volcano ring of VolcanoCard
                data.put("j",j);
                data.put("segment", segment);
                data.put("cardCounter",cardCounter);
                data.put("x", x);
                data.put("y",y);
                createModel(data);
                cardCounter += 1;
            }

        }

        return FXGL.entityBuilder(data).view(cardGroup).build();
    }

    @Spawns("loadVolcanoRing")
    public Entity loadVolcanoRing(SpawnData data){
        int cardCounter = 23;
        // Create a group to hold the cards
        Group cardGroup = new Group();
        for (VolcanoCard card:volcanoRing){
            if (cardCounter == 24){cardCounter = 0;}
            // Calculate the x and y coordinates for the card position. Adjust if needed
            double x = card.getxPos();
            double y = card.getyPos();
            Rectangle rect = new Rectangle(x - Config.VOLCANO_CARD_WIDTH / 2.0, y - Config.VOLCANO_CARD_HEIGHT / 2.0, Config.VOLCANO_CARD_WIDTH, Config.VOLCANO_CARD_HEIGHT);
            Animal newAnimal = Animal.getAnimal(card.getAnimal(), 1);
            assert newAnimal != null;
            newAnimal.fillUIShape(rect);
            rect.setStroke(Color.BLACK);
            rect.setStrokeWidth(1);
            // rotate
            Rotate rotate = new Rotate();
            rotate.setAngle(Config.FIRST_ROTATION_ANGLE+15+(cardCounter*rotationAmount));
            rotate.setPivotX(x);
            rotate.setPivotY(y);
            rect.getTransforms().addAll(rotate);
            cardGroup.getChildren().add(rect);
            cardCounter += 1;

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
        int segmentID = 1;
        VolcanoSegment volcanoSegment = new VolcanoSegment(segmentLength);
        segments[0] = volcanoSegment;
        int segmentEnd = 2;
        for (int i=0; i<numCards; i++){

            if (i!=0 && i%(segmentLength)==0){
                segmentID += 1;
                volcanoSegment = new VolcanoSegment(segmentLength);
            }
            VolcanoCard newVolcanoCard = new VolcanoCard(i+1, Config.ANIMALS[i], 0,0, false); // dummy values
            volcanoSegment.addVolcanoCard(newVolcanoCard);

            if (i==segmentEnd){
                segments[segmentID-1] = volcanoSegment;
                segmentEnd += segmentLength;
            }
        }

        return  segments;
    }

    @Override
    public void spawn(boolean isNewGame) {
        super.spawn(isNewGame);
        if (isNewGame) {
            VolcanoSegment[] segments = this.createSegments();
            //random segments arrangements. remember that segments are alternately placed, so randomising is for position 2,4,6,8
            spawnData.put("segments", segments); // pass the information to the factory so that it can spawn based on the segments

        FXGL.spawn("newVolcanoRing", spawnData); // this also updates volcano ring
        }
        else {
            FXGL.spawn("loadVolcanoRing", spawnData);
        }
    }

    private Rectangle createVolcanoCardShape(double x, double y, VolcanoSegment segment, int j, int cardCounter){
        Rectangle rect = new Rectangle(x - Config.VOLCANO_CARD_WIDTH / 2.0, y - Config.VOLCANO_CARD_HEIGHT / 2.0, Config.VOLCANO_CARD_WIDTH, Config.VOLCANO_CARD_HEIGHT);
        Animal newAnimal = Animal.getAnimal(segment.getSegmentCards()[j].getAnimal(), 1);
        assert newAnimal != null;
        newAnimal.fillUIShape(rect);
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(1);
        // rotate
        Rotate rotate = new Rotate();
        rotate.setAngle(Config.FIRST_ROTATION_ANGLE+(cardCounter*rotationAmount));
        rotate.setPivotX(x);
        rotate.setPivotY(y);
        rect.getTransforms().addAll(rotate);
        return rect;
    }

    /**
     * Create the Volcano Card and store in the array of the volcano ring
     * @param spawnData the payload containing information of the VolcanoCard
     */
    @Override
    public void createModel(SpawnData spawnData) {
        int cardCounter = spawnData.get("cardCounter");
        VolcanoSegment segment = spawnData.get("segment");
        int j = spawnData.get("j");
        double x = spawnData.get("x");
        double y = spawnData.get("y");
        volcanoRing[cardCounter] = new VolcanoCard(cardCounter+1, segment.getSegmentCards()[j].getAnimal(),x,y,false );

    }

    @Override
    public void load(int slotIndex) throws IOException{
        BufferedReader reader = Files.newBufferedReader(getSaveFilePath(slotIndex));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.equals("VolcanoRingFactory")) {
                while (!(line = reader.readLine()).equals("*")) {
                    String[] parts = line.split(",");
                    int index = Integer.parseInt(parts[0]);
                    boolean occupied = parts[3].equals("true");
                    volcanoRing[index-1] = new VolcanoCard(
                            index,
                            stringTypeMapping(parts[4]),
                            Double.parseDouble(parts[1]),
                            Double.parseDouble(parts[2]),
                            occupied);
                }
                break;
            }
        }

    }

    @Override
    public void save(BufferedWriter writer, int slotIndex) throws IOException{
        // Save current state
        writer.write("VolcanoRingFactory\n");
        for (VolcanoCard card:volcanoRing){
            writer.write(+card.getRingID()+","+card.getxPos()+","+card.getyPos()+","+card.getOccupiedStatus()+","+card.getAnimal()+"\n");
        }
        writer.write("*\n");
    }

    /**
     * Utility class to create segments of volcano cards, helps with random arrangement of uncut and cut segments
     */
    private static class VolcanoSegment {
        private final VolcanoCard[] segmentCards;
        private int counter = 0;
        private VolcanoSegment(int newSegmentLength){
            segmentCards = new VolcanoCard[newSegmentLength];
        }
        private void addVolcanoCard(VolcanoCard volcanoCard){
            segmentCards[counter] = volcanoCard;
            counter += 1;
        }
        private VolcanoCard[] getSegmentCards() {
            return segmentCards;
        }
    }

}
