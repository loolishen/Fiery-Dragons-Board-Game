package com.example.demo.EntityFactory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.example.demo.Animals.Animal;
import com.example.demo.Config;
import com.example.demo.Controller.ChitCardAdapter;
import com.example.demo.Model.ChitCard;
import com.example.demo.Utils.Utils;
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

    private final ChitCardAdapter chitCardAdapter;
    public ChitCardFactory(ChitCardAdapter chitCardAdapter) {
        this.chitCardAdapter = chitCardAdapter;
    }

    private final Circle[] coveredChitCardShapes = new Circle[Config.NUM_CHIT_CARDS]; // initialized with reference from FieryDragonsApplication class


    @Spawns("chitCard")
    public Entity newChitCard(SpawnData data){

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
        // initialise the model if we are loading a game
        if (!isNewGame) { // if we starting a new game, the model would have been initialised in spawn()
            ChitCard chitCard = data.get("chitCard");
            chitCardAdapter.addMapping(coveredShape, chitCard);
            chitCardAdapter.addMapping(uncoveredShape, chitCard);
        }

        return FXGL.entityBuilder(data).view(cardGroup).build();
    }

    public Circle[] getCoveredChitCards() {
        return coveredChitCardShapes;
    }


    /**
     * Get random coordinates for the cards, except Leprechaun
     * @return list of random coordinates
     */
    private static List<int[]> getRandomCoordinates(){
        // Generate all possible pairs of numbers for range 1 ... sqrt(NUM_CHIT_CARDS) to represent coordinates of a square grid
        int min = 1;
        int max = (int) Math.sqrt(Config.NUM_CHIT_CARDS-1);
        ArrayList<int[]> allPairsInRange = Utils.generatePairs(min, max);

        // get the16 coordinates
        List<int[]> sublist = allPairsInRange.subList(0, Config.NUM_CHIT_CARDS-1);
        // add coordinate for 17th card
        int x = Config.SCENE_WIDTH/2;
        int y = Config.SCENE_HEIGHT/2;

        sublist.add(new int[]{x, y});
        // get first 16 unique pairs
        // Shuffle for randomness
        Utils.shuffleIntArray(sublist, Config.RNG_SEED); // this simulates randomness of chit card positions
        return sublist;
    }

    @Override
    public void spawn(boolean isNewGame){
        super.spawn(isNewGame);
        if (isNewGame){
            List<int[]> randomCoordinates = getRandomCoordinates(); // get the list of random coordinates
            int chitCardIdx = 0;
            int animalImagePathsIdx = 0; // used for accessing a particular animal in Constants.ANIMAL_TYPES.
            int animalCounter = 1;
            int dragonPirateCounter = 1;
            while (chitCardIdx < Config.NUM_CHIT_CARDS){
                // create new covered image for every chit card
                Circle coveredChitCardShape = createCoveredChitCardFilledShape();
                int[] coordinates = randomCoordinates.get(chitCardIdx);

                if ((chitCardIdx!=0) && (chitCardIdx % (Config.ANIMAL_MAX_COUNT) == 0) && (chitCardIdx!=15)) {
                    animalImagePathsIdx += 1;
                }
                // If it is not the last one then calculate
                int x;
                int y;
                if (coordinates[0]!= Config.SCENE_WIDTH/2) {
                    x = Config.START_X + coordinates[0] * (2 * Config.CARD_RADIUS + Config.CARD_SPACE_PADDING) - Config.CARD_RADIUS - Config.CARD_SPACE_PADDING;
                    y = Config.START_Y + coordinates[1] * (2 * Config.CARD_RADIUS + Config.CARD_SPACE_PADDING) - Config.CARD_RADIUS - Config.CARD_SPACE_PADDING;
                }else{x = coordinates[0]; y=coordinates[1];}
                // prepare the Circle nodes filled with the appropriate animal image
                spawnData = new SpawnData(x,y);
                spawnData.put("idx", chitCardIdx);
                Circle uncoveredChitCardShape = createUncoveredChitCardShape();
                int animalCount;

                // the conditionals below set animalCount dynamically depending on whether it should be for dragon pirate or
                // the other animals. Normal animals go from 1 to 3, while dragon pirates go from 1 to 2
                //  index from 12 to 15 is for dragon pirate
                if (chitCardIdx >= (Config.NUM_CHIT_CARDS - Config.DRAGON_PIRATE_CHIT_CARD_COUNT-1)){
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
                spawnData.put("animal",animal );
                uncoveredChitCardShape.setVisible(false);
                coveredChitCardShape.setVisible(true);
                spawnData.put("uncovered", uncoveredChitCardShape);
                spawnData.put("covered", coveredChitCardShape);

                // init model: create the ChitCard model, and initialise the mapping of view to model
                chitCardAdapter.createModel(spawnData);

                // spawn both sides of card
                FXGL.spawn("chitCard", spawnData);

                // update animalCountIdx if it is for leprechaun
                if (chitCardIdx==15){
                    animalImagePathsIdx+=1;
                }
                chitCardIdx += 1;

            }

        } else spawnLoaded();
    }

    public Circle createCoveredChitCardFilledShape(){
        Circle coveredChitCardShape = new Circle(Config.CARD_RADIUS);
        Image coveredImage = new Image(Objects.requireNonNull(getClass().getResource("/com/example/demo/assets/chitCardCovered.png")).toExternalForm());
        coveredChitCardShape.setFill(new ImagePattern(coveredImage));
        coveredChitCardShape.setStrokeWidth(2); // Set the thickness to 2 pixels
        coveredChitCardShape.setStroke(Color.BLACK); // Set the outline color to black
        return coveredChitCardShape;
    }

    public Circle createUncoveredChitCardShape(){
        Circle uncoveredChitCardShape = new Circle(Config.CARD_RADIUS); // create new node
        uncoveredChitCardShape.setStrokeWidth(2);
        uncoveredChitCardShape.setStroke(Color.BLACK);
        return uncoveredChitCardShape;
    }

    private void spawnLoaded(){
        for (ChitCard chitCard : chitCardAdapter.getChitCards()) {
            // create new covered image for every chit card
            Circle coveredChitCardShape = createCoveredChitCardFilledShape();
            double[] coordinates = new double[]{chitCard.getX(), chitCard.getY()};
            spawnData = new SpawnData(coordinates[0], coordinates[1]);
            spawnData.put("idx", chitCard.getIndex());
            Circle uncoveredChitCardShape = createUncoveredChitCardShape();
            chitCard.getAnimal().fillUIShape(uncoveredChitCardShape);
            spawnData.put("animal", chitCard.getAnimal());
            uncoveredChitCardShape.setVisible(chitCard.isUncoveredVisible());
            coveredChitCardShape.setVisible(chitCard.isCoveredVisible());
            chitCard.setCoveredForm(coveredChitCardShape);
            chitCard.setUncoveredForm(uncoveredChitCardShape);
            spawnData.put("uncovered", uncoveredChitCardShape);
            spawnData.put("covered", coveredChitCardShape);
            spawnData.put("chitCard", chitCard);
            // spawn both sides of card
            FXGL.spawn("chitCard", spawnData);
        }
    }

}
