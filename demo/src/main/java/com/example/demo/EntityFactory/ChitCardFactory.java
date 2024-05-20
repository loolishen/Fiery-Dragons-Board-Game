package com.example.demo.EntityFactory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.example.demo.Animals.*;
import com.example.demo.Config;
import com.example.demo.Controller.ChitCardAdapter;
import com.example.demo.utils.Utils;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Used to create chit cards, and pass the information of the covered sides to the ChitCardFlipManager once they are initialized
 * @author Loo Li Shen
 */
public class ChitCardFactory extends SpawnFactory{

    private final Circle[] coveredChitCardShapes = new Circle[Config.NUM_CHIT_CARDS]; // initialized with reference from FieryDragonsApplication class


    @Spawns("chitCard")
    public Entity newChitCard(SpawnData data){

        // init model: create the ChitCard model, and initialise the mapping of view to model
        ChitCardAdapter chitCardAdapter = new ChitCardAdapter();
        chitCardAdapter.createModel(data);

        // create new group for both sides of chit card
        Group cardGroup = new Group();

        // get metadata associated with the chit card
        int index = data.get("idx");

        // get the covered and uncovered image components
        Circle coveredShape = data.get("covered");
        coveredChitCardShapes[index] = coveredShape;
        Circle uncoveredShape = data.get("uncovered");

        cardGroup.getChildren().add(uncoveredShape);
        cardGroup.getChildren().add(coveredShape);
        return FXGL.entityBuilder(data).view(cardGroup).build();
    }

    public Circle[] getCoveredChitCards() {
        return coveredChitCardShapes;
    }


    private static List<int[]> getRandomCoordinates(){
        // Generate all possible pairs of numbers for range 1 ... sqrt(NUM_CHIT_CARDS) to represent coordinates of a square grid
        int min = 1;
        int max = (int) Math.sqrt(Config.NUM_CHIT_CARDS);
        ArrayList<int[]> allPairsInRange = Utils.generatePairs(min, max);

        // Shuffle for randomness
        Utils.shuffleIntArray(allPairsInRange, Config.NO_SEED); // this simulates randomness of chit card positions

        // get first 16 unique pairs
        return allPairsInRange.subList(0, Config.NUM_CHIT_CARDS);
    }

    @Override
    public void spawn(){
        super.spawn();
        List<int[]> randomCoordinates = getRandomCoordinates(); // get the list of random coordinates
        int chitCardIdx = 0;
        int animalImagePathsIdx = 0; // used for accessing a particular animal in Constants.ANIMAL_TYPES.
        int animalCounter = 1;
        int dragonPirateCounter = 1;
        while (chitCardIdx < Config.NUM_CHIT_CARDS){
            // create new covered image for every chit card
            Circle coveredChitCardShape = createCoveredChitCardFilledShape();

            if ((chitCardIdx!=0) && (chitCardIdx % (Config.ANIMAL_MAX_COUNT) == 0) && chitCardIdx!=15) {
                animalImagePathsIdx += 1;
            }
            int[] coordinates = randomCoordinates.get(chitCardIdx);
            // Calculate the centre of the circle
            int x = Config.START_X + coordinates[0] * (2 * Config.CARD_RADIUS + Config.CARD_SPACE_PADDING) - Config.CARD_RADIUS - Config.CARD_SPACE_PADDING;
            int y = Config.START_Y + coordinates[1] * (2 * Config.CARD_RADIUS + Config.CARD_SPACE_PADDING) - Config.CARD_RADIUS - Config.CARD_SPACE_PADDING;

            // prepare the Circle nodes filled with the appropriate animal image
            spawnData = new SpawnData(x,y);
            spawnData.put("idx", chitCardIdx);
            spawnData.put("animalType", Config.ANIMAL_TYPES[animalImagePathsIdx]);
            Circle uncoveredChitCardShape = createUncoveredChitCardShape();
            int animalCount;

            // the conditionals below set animalCount dynamically depending on whether it should be for dragon pirate or
            // the other animals. Normal animals go from 1 to 3, while dragon pirates go from 1 to 2
            //  index from 12 to 15 is for dragon pirate
            if (chitCardIdx >= (Config.NUM_CHIT_CARDS - Config.DRAGON_PIRATE_CHIT_CARD_COUNT)){
                animalCount = -dragonPirateCounter;
                dragonPirateCounter += 1;
                if (dragonPirateCounter == Config.DRAGON_PIRATE_MAX_COUNT+1) {
                    dragonPirateCounter = 1;
                }
            } else { // indexes 0 to 11
                if (animalCounter == Config.ANIMAL_MAX_COUNT+1) {
                    animalCounter = 1;
                }
                animalCount = animalCounter;
                animalCounter += 1;
            }
            Animal animal = Animal.getAnimal(Config.ANIMAL_TYPES[animalImagePathsIdx], Math.abs(animalCount));
            assert animal != null;
            animal.fillUIShape(uncoveredChitCardShape);

            uncoveredChitCardShape.setVisible(false);
            coveredChitCardShape.setVisible(true);
            spawnData.put("animalCount", animalCount);
            spawnData.put("uncovered", uncoveredChitCardShape);
            spawnData.put("covered", coveredChitCardShape);
            // spawn both sides of card
            FXGL.spawn("chitCard", spawnData);
            chitCardIdx += 1;
        }
    }

    private Circle createCoveredChitCardFilledShape(){
        Circle coveredChitCardShape = new Circle(Config.CARD_RADIUS);
        Image coveredImage = new Image(Objects.requireNonNull(getClass().getResource("/com/example/demo/assets/chitCard.png")).toExternalForm());
        coveredChitCardShape.setFill(new ImagePattern(coveredImage));
        coveredChitCardShape.setStrokeWidth(2); // Set the thickness to 2 pixels
        coveredChitCardShape.setStroke(Color.BLACK); // Set the outline color to black
        return coveredChitCardShape;
    }

    private Circle createUncoveredChitCardShape(){
        Circle uncoveredChitCardShape = new Circle(Config.CARD_RADIUS); // create new node
        uncoveredChitCardShape.setStrokeWidth(2);
        uncoveredChitCardShape.setStroke(Color.BLACK);
        return uncoveredChitCardShape;
    }

}
