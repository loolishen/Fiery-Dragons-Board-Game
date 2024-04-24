package com.example.demo;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.example.demo.utils.RandomGeneration;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class FieryDragonsApplication extends GameApplication {
    private final Player[] players = new Player[NUM_PLAYERS]; // TODO: clockwise direction,and youngest player starts first (first element is first player, others randomised
    private final static int TOTAL_CARDS = 24;
    private Volcano volcanoRing = new Volcano(3, 24);
    private Circle[] chitCards = new Circle[NUM_CHIT_CARDS];
    private final static int ANIMAL_COUNT = 3;
    private final static int DRAGON_PIRATE_COUNT = 2;
    private boolean endGame = false;
    private final static int NUM_PLAYERS = 4;
    private final static AnimalType[] animalTypes = {AnimalType.SALAMANDER, AnimalType.BAT, AnimalType.SPIDER, AnimalType.BABY_DRAGON, AnimalType.DRAGON_PIRATE};
    private final static String[] animalImagePaths = {"/com/example/demo/assets/salamander", "/com/example/demo/assets/bat",
            "/com/example/demo/assets/spider","/com/example/demo/assets/babyDragon","/com/example/demo/assets/skull" };

    private final static int NUM_CHIT_CARDS = 16;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings gameSettings) {
    }
    @Override
    protected void initGame() {

        // Add the volcano ring card factory
        VolcanoRingFactory newVolcanoRingFactory = new VolcanoRingFactory(NUM_PLAYERS);
        FXGL.getGameWorld().addEntityFactory(newVolcanoRingFactory);
        FXGL.spawn("volcanoRing");

        VolcanoSegment[] segments = newVolcanoRingFactory.createSegments(volcanoRing); // this method instantiates the volcanoRing attribute
        System.out.println("Finished initializing volcano ring");


        // add players TODO: refactor to have player factory
        for (int i =0; i<NUM_PLAYERS; i++){
            DragonToken dragonToken = new DragonToken(volcanoRing.getVolcanoCardByID(i*6+1)); // [0,2,4,6] refactor based on volcanoringfactory's hasCaveIndexes variable
            Player newPlayer = new Player(dragonToken);
            players[i] = newPlayer;
        }

        // chit card factory
        ChitCardFactory newChitCardFactory = new ChitCardFactory();
        FXGL.getGameWorld().addEntityFactory(newChitCardFactory);

        int cardRadius = 20; // Radius of the circular card
        int padding = 10; // Padding between cards
        int sceneWidth = FXGL.getAppWidth();
        int sceneHeight = FXGL.getAppHeight();

        // TODO: better formula for this and usage of 4 rows and 4 columns
        int gridWidth = NUM_CHIT_CARDS/4; // Number of cards in a row
        int gridHeight = NUM_CHIT_CARDS/4; // Number of cards in a column

        // Calculate the total width and height occupied by the grid of cards
        int gridTotalWidth = gridWidth * (2 * cardRadius) + (gridWidth - 1) * padding;
        int gridTotalHeight = gridHeight * (2 * cardRadius) + (gridHeight - 1) * padding;

        // Calculate the starting position to center the grid within the scene
        int startX = (sceneWidth - gridTotalWidth) / 2 - 50;
        int startY = (sceneHeight - gridTotalHeight) / 2 - 50;

        // get random coordinates for the chit cards
        List<int[]> randomCoordinates = RandomGeneration.getRandomPairs(NUM_CHIT_CARDS);

        // spawn the chit cards
        int chitCardIdx = 0;
        int animalCounter = 1;
        int animalImagePathsIdx = 0;
        int dragonPirateCounter = 1;
        while (chitCardIdx < NUM_CHIT_CARDS){
            if ((chitCardIdx!=0) && (chitCardIdx % (gridWidth-1) == 0) && chitCardIdx!=15) {
                animalImagePathsIdx += 1;
            }
            int[] coordinates = randomCoordinates.get(chitCardIdx);
            int x = startX + coordinates[0] * (2 * cardRadius + padding) + cardRadius;
            int y = startY + coordinates[1] * (2 * cardRadius + padding) + cardRadius;

            SpawnData spawnData = new SpawnData(x, y);
            if (chitCardIdx >= (NUM_CHIT_CARDS - gridWidth)){
                if (dragonPirateCounter == DRAGON_PIRATE_COUNT) {
                    dragonPirateCounter = 1;
                }
                spawnData.put("idx", chitCardIdx);
                spawnData.put("animalType", animalTypes[animalImagePathsIdx]);
                spawnData.put("imagePath", animalImagePaths[animalImagePathsIdx]+dragonPirateCounter+".png");
                spawnData.put("animalCount", dragonPirateCounter);
                dragonPirateCounter += 1;
            } else {
                if (animalCounter == ANIMAL_COUNT+1) {
                    animalCounter = 1;
                }
                spawnData.put("idx", chitCardIdx);
                spawnData.put("animalType", animalTypes[animalImagePathsIdx]);
                spawnData.put("imagePath", animalImagePaths[animalImagePathsIdx]+animalCounter+".png");
                spawnData.put("animalCount", animalCounter);
                animalCounter += 1;
            }
            FXGL.spawn("chitCard", spawnData);
            chitCardIdx += 1;
        }

        // now that chit card nodes have been populated, we can assign them to the variable chitCards
        chitCards = newChitCardFactory.getChitCards();
        for (Circle circle : chitCards) {
            System.out.println(circle);
        }


    }
    @Override
    protected void onUpdate(double tpf) {
        // check that everything has been populated

        int playerTurn = 1;
        Player currPlayer = players[playerTurn-1];
        while (!endGame){

            while (!currPlayer.getTurnEnded()) {
                playTurn(currPlayer); // this method will modify the player's turnEnded attribute

                // debugging
                endGame = true;
                break;
            }
            if (playerTurn == players.length){playerTurn = 1;}else{playerTurn+=1;} // reset to 1 if the 4th player ended their turn
            // turn ended so go to next player
            currPlayer = players[playerTurn-1];
        }
        displayMessage();
    }

    /**
     * Display winning message.
     */
    private void displayMessage(){

    }

    public void handleCircleClick(Player player, Circle chitCardChosen){
        System.out.println("Chosen card is" + chitCardChosen);
        HashMap<Circle, ChitCard> mapping = ChitCardFactory.getViewControllerMapping();
        ChitCard cardChosen = mapping.get(chitCardChosen);
        cardChosen.flipToFrontAnimation(); // removes covered


        // update spawn data, adds uncovered side
        SpawnData newSpawnData = new SpawnData(cardChosen.getSpawnData().getX(), cardChosen.getSpawnData().getY());
        newSpawnData.put("uncoveredShape", cardChosen.getUncoveredForm());
        newSpawnData.put("idx", cardChosen.getIndex());
        newSpawnData.put("animalType", cardChosen.getAnimalType());
        newSpawnData.put("animalCount", cardChosen.getAnimalCount());
        FXGL.spawn("uncoveredChitCard", newSpawnData);// add new one

        int result = player.makeMove(volcanoRing, cardChosen); // result = 0 means end turn, otherwise it is destination ring ID that token should move to
        if  (result != 0) {

            // update the volcano card's (the token is on) 'occupied' status to false
            int currPositionRingID = player.getDragonToken().getCurrentPosition();
            volcanoRing.getVolcanoCardByID(currPositionRingID).flipOccupiedStatus();


            // TODO: move token to volcano card at destination ring ID

            // update the state: dragon token's position and total movement count
            player.getDragonToken().setCurrentPosition(volcanoRing.getVolcanoCardByID(result));
            player.getDragonToken().updateTotalMovementCount((cardChosen.getAnimalCount()));

            // update movedOutOfCave to True if exiting cave for the first time
            if (!player.getDragonToken().getMovedOutOfCave()){player.getDragonToken().setMovedOutOfCave();}

        } else {
            player.setTurnEnded(true);

        }

        if (result == volcanoRing.getTotalCards()){endGame = true;} // end the game

        // continue loop
        if (!player.getTurnEnded()){
            playTurn(player);
        }
    }

    private void playTurn(Player newPlayer){
        System.out.println("Current chit cards are");
        for (Circle circle : chitCards) {
            System.out.println(circle);
            circle.setOnMouseClicked(event -> handleCircleClick(newPlayer, circle));
        }

    }
}