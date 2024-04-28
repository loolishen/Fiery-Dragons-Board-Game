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
import javafx.scene.shape.Circle;

import java.util.*;


public class ChitCardFactory extends SpawnFactory {

    // below two are references of attributes of ChitCardManager class
    private final Circle[] coveredChitCardShapes = new Circle[Constants.NUM_CHIT_CARDS]; // initialized with reference from FieryDragonsApplication class

    // Maps a ChitCard's Circle node (UI) to its controller ChitCard class
    private  static final HashMap<Circle, ChitCard> viewControllerMapping = new HashMap<>() {
    };


    @Spawns("chitCard")
    public Entity newChitCard(SpawnData data){
        // create new group for both sides of chit card
        Group cardGroup = new Group();

        // get metadata associated with the chit card
        int index = data.get("idx");
        AnimalType animalType = data.get("animalType");
        int animalCount = data.get("animalCount");

        // get the covered and uncovered image components, and fill their associated arrays
        Circle coveredShape = data.get("covered");
        coveredChitCardShapes[index] = coveredShape;
        Circle uncoveredShape = data.get("uncovered");

        // create new chit card
        ChitCard chitCard = new ChitCard(index, animalType,animalCount, coveredShape, uncoveredShape);
        viewControllerMapping.put(coveredShape, chitCard);
        viewControllerMapping.put(uncoveredShape, chitCard);

        cardGroup.getChildren().add(uncoveredShape);
        cardGroup.getChildren().add(coveredShape);
        return FXGL.entityBuilder(data).view(cardGroup).build();

    }

    public Circle[] getCoveredChitCards() {
        return coveredChitCardShapes;
    }

    public static HashMap<Circle, ChitCard> getViewControllerMapping(){
        return viewControllerMapping;
    }

    private static List<int[]> getRandomCoordinates(){
        // Generate all possible pairs of numbers for range 1 ... sqrt(NUM_CHIT_CARDS) to represent coordinates of a square grid
        int min = 1;
        int max = (int) Math.sqrt(Constants.NUM_CHIT_CARDS);
        ArrayList<int[]> allPairsInRange = Utils.generatePairs(min, max);

        // Shuffle for randomness
//        Utils.shuffleIntArray(allPairsInRange, Constants.RNG_SEED);
        Utils.shuffleIntArray(allPairsInRange, Constants.RNG_SEED);

        // get first 16 unique pairs
        return allPairsInRange.subList(0, Constants.NUM_CHIT_CARDS);
    }

    @Override
    public void spawn(){
        super.spawn();
        List<int[]> randomCoordinates = getRandomCoordinates(); // get the list of random coordinates
        int chitCardIdx = 0;
        int animalImagePathsIdx = 0; // used for accessing a particular animal in Constants.ANIMAL_TYPES.
        int animalCounter = 1;
        int dragonPirateCounter = 1;
        while (chitCardIdx < Constants.NUM_CHIT_CARDS){
            // create new covered image for every chit card
            Circle defaultChitCardShape = new Circle(Constants.CARD_RADIUS);
            Image coveredImage = new Image(Objects.requireNonNull(getClass().getResource("/com/example/demo/assets/chitCard.png")).toExternalForm());
            defaultChitCardShape.setFill(new ImagePattern(coveredImage));
            defaultChitCardShape.setStrokeWidth(2); // Set the thickness to 2 pixels
            defaultChitCardShape.setStroke(Color.BLACK); // Set the outline color to black

            if ((chitCardIdx!=0) && (chitCardIdx % (Constants.ANIMAL_MAX_COUNT) == 0) && chitCardIdx!=15) {
                animalImagePathsIdx += 1;
            }
            int[] coordinates = randomCoordinates.get(chitCardIdx);
            // Calculate the centre of the circle
            int x = Constants.START_X + coordinates[0] * (2 * Constants.CARD_RADIUS + Constants.CARD_SPACE_PADDING) - Constants.CARD_RADIUS - Constants.CARD_SPACE_PADDING;
            int y = Constants.START_Y + coordinates[1] * (2 * Constants.CARD_RADIUS + Constants.CARD_SPACE_PADDING) - Constants.CARD_RADIUS - Constants.CARD_SPACE_PADDING;

            // prepare the Circle nodes filled with the appropriate animal image
            spawnData = new SpawnData(x,y);
            spawnData.put("idx", chitCardIdx);
            spawnData.put("animalType", Constants.ANIMAL_TYPES[animalImagePathsIdx]);
            Circle uncoveredChitCardShape = new Circle(Constants.CARD_RADIUS); // create new node
            uncoveredChitCardShape.setStrokeWidth(2);
            uncoveredChitCardShape.setStroke(Color.BLACK);
            String imagePathPrefix = Constants.ANIMAL_IMAGE_IMAGE_PATH_PREFIX_MAPPINGS.get(Constants.ANIMAL_TYPES[animalImagePathsIdx]);
            Image uncoveredImage;
            int animalCount;
//             index from 12 to 15 is for dragon pirate
            if (chitCardIdx >= (Constants.NUM_CHIT_CARDS - Constants.DRAGON_PIRATE_CHIT_CARD_COUNT)){
                animalCount = -dragonPirateCounter;
                uncoveredImage = new Image(Objects.requireNonNull(getClass().getResource(imagePathPrefix + dragonPirateCounter + ".png")).toExternalForm());
                dragonPirateCounter += 1;
                if (dragonPirateCounter == Constants.DRAGON_PIRATE_MAX_COUNT+1) {
                    dragonPirateCounter = 1;
                }
            } else { // indexes 0 to 11
                if (animalCounter == Constants.ANIMAL_MAX_COUNT+1) {
                    animalCounter = 1;
                }
                animalCount = animalCounter;
                uncoveredImage = new Image(Objects.requireNonNull(getClass().getResource(imagePathPrefix + animalCounter + ".png")).toExternalForm());
                animalCounter += 1;
            }

            uncoveredChitCardShape.setFill(new ImagePattern(uncoveredImage));
            uncoveredChitCardShape.setVisible(false);
            defaultChitCardShape.setVisible(true);
            spawnData.put("animalCount", animalCount);
            spawnData.put("uncovered", uncoveredChitCardShape);
            spawnData.put("covered", defaultChitCardShape);
            // spawn both sides of card
            FXGL.spawn("chitCard", spawnData);
            chitCardIdx += 1;
        }
    }

}
