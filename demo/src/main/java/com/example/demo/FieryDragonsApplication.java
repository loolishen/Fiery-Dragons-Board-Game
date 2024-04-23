package com.example.demo;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.example.demo.utils.RandomGeneration;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class FieryDragonsApplication extends GameApplication {

    private final static int ANIMAL_COUNT = 3;
    private final static int DRAGON_PIRATE_COUNT = 2;

    private final static int NUM_PLAYERS = 4;

    private final static String[] animalImagePaths = {"/com/example/demo/assets/salamander", "/com/example/demo/assets/bat",
            "/com/example/demo/assets/spider","/com/example/demo/assets/babyDragon","/com/example/demo/assets/skull" };

    private final static int NUM_CHIT_CARDS = 16;


    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FieryDragonsApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings gameSettings) {

    }

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new ChitCardFactory());

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
                spawnData.put("imagePath", animalImagePaths[animalImagePathsIdx]+dragonPirateCounter+".png");
                spawnData.put("animalCount", dragonPirateCounter);
                dragonPirateCounter += 1;
            } else {
                if (animalCounter == ANIMAL_COUNT+1) {
                    animalCounter = 1;
                }
                spawnData.put("imagePath", animalImagePaths[animalImagePathsIdx]+animalCounter+".png");
                spawnData.put("animalCount", animalCounter);
                animalCounter += 1;
            }
            FXGL.spawn("chitCard", spawnData);
            chitCardIdx += 1;
        }

        // Add the volcano ring card factory
        VolcanoRingFactory newVolcanoRingFactory = new VolcanoRingFactory(NUM_PLAYERS);
        FXGL.getGameWorld().addEntityFactory(newVolcanoRingFactory);
        FXGL.spawn("volcanoRing");

        VolcanoSegment[] segments = newVolcanoRingFactory.createSegments();



    }

}