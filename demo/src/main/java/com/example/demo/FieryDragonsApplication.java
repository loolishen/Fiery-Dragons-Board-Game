package com.example.demo;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.example.demo.utils.RandomGeneration;
import javafx.animation.RotateTransition;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.Map.entry;

public class FieryDragonsApplication extends GameApplication {
    private final Player[] players = new Player[NUM_PLAYERS]; // TODO: clockwise direction,and youngest player starts first (first element is first player, others randomised
    private final static int TOTAL_CARDS = 24;
    private  Player currPlayer;
    private boolean firstTimeRemove = true;
    private boolean firstTimeWinMsg = true; // first time spawning win message
    private boolean firstTimeTurnMsg = true;
    private int playerTurn = 1;
    private long RNG_SEED = 531;
    private Entity currentTextEntity;
    private Lock animationLock = new ReentrantLock();
    private Volcano volcanoRing = new Volcano(3, 24);
    private boolean endGame = false;
    private Circle[] coveredChitCardShapes = new Circle[NUM_CHIT_CARDS];
    private Circle[] uncoveredChitCardShapes = new Circle[NUM_CHIT_CARDS];

    private final static int ANIMAL_COUNT = 3;
    private final static int DRAGON_PIRATE_COUNT = 2;
    private final static int NUM_PLAYERS = 4;
    private boolean COVERING_ANIMATION_IN_PROGRESS = false;

    private ArrayList<Circle> uncoveredChitCards = new ArrayList<Circle>();
    private final static AnimalType[] animalTypes = {AnimalType.SALAMANDER, AnimalType.BAT, AnimalType.SPIDER, AnimalType.BABY_DRAGON, AnimalType.DRAGON_PIRATE};

    private static final Map<AnimalType, String> animalImagePathMappings = Map.ofEntries(
            entry(AnimalType.SALAMANDER, "/com/example/demo/assets/salamander"),
            entry(AnimalType.SPIDER, "/com/example/demo/assets/spider"),
            entry(AnimalType.BABY_DRAGON, "/com/example/demo/assets/babyDragon"),
            entry(AnimalType.BAT, "/com/example/demo/assets/bat"),
            entry(AnimalType.DRAGON_PIRATE, "/com/example/demo/assets/skull")
    );
    private final static int NUM_CHIT_CARDS = 16;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings gameSettings) {
    }

    @Override
    protected void initUI() {
        // Set the background color of the application
        FXGL.getGameScene().getRoot().setStyle("-fx-background-color: antiquewhite;");
    }
    @Override
    protected void initGame() {
        // Add the volcano ring card factory
        VolcanoRingFactory newVolcanoRingFactory = new VolcanoRingFactory();
        FXGL.getGameWorld().addEntityFactory(newVolcanoRingFactory);

        VolcanoSegment[] segments = newVolcanoRingFactory.createSegments(volcanoRing); // this method instantiates the volcanoRing attribute

        // random segments arrangements. remember that segments are alternately place, so randomising is for position 2,4,6,8
        SpawnData segmentArrangementData = new SpawnData();
        segmentArrangementData.put("segments", segments);
        ArrayList<Integer> randomArrangements = new ArrayList<>(Arrays.asList(2,4,6,8)); // TODO: MAGIC NUMBER for segment length
        RandomGeneration.shuffleIntArray(randomArrangements, RNG_SEED);
        segmentArrangementData.put("arrangement", randomArrangements);
        segmentArrangementData.put("volcano", volcanoRing);
        FXGL.spawn("volcanoRing", segmentArrangementData); // this also updates volcano ring

        // add players  TODO: refactor to have player factory
        for (int i =0; i<NUM_PLAYERS; i++) {
            Player newPlayer = new Player(i + 1);
            players[i] = newPlayer;
        }
        // add dragon token and cave
        DragonTokenFactory dragonTokenFactory = new DragonTokenFactory(NUM_PLAYERS,VolcanoRingFactory.CIRCLE_RADIUS, players);
        CaveFactory caveFactory = new CaveFactory(NUM_PLAYERS, VolcanoRingFactory.CIRCLE_RADIUS);
        FXGL.getGameWorld().addEntityFactory(dragonTokenFactory);
        FXGL.getGameWorld().addEntityFactory(caveFactory);

        SpawnData spawnData1 = new SpawnData();
        spawnData1.put("caveID", volcanoRing);
        FXGL.spawn("dragonToken", spawnData1);
        FXGL.spawn("cave", spawnData1);


        // chit card factory
        ChitCardFactory newChitCardFactory = new ChitCardFactory(uncoveredChitCardShapes, coveredChitCardShapes);
        FXGL.getGameWorld().addEntityFactory(newChitCardFactory);

        int cardRadius = 20; // Radius of the circular card
        int padding = 20; // Padding between cards
        int sceneWidth = FXGL.getAppWidth();
        int sceneHeight = FXGL.getAppHeight();
        int gridWidth = NUM_CHIT_CARDS/4; // Number of cards in a row // TODO: better formula for this and usage of 4 rows and 4 columns
        int gridHeight = NUM_CHIT_CARDS/4; // Number of cards in a column

        // Calculate the total width and height occupied by the grid of cards
        int gridTotalWidth = gridWidth * (2 * cardRadius) + (gridWidth - 1) * padding;
        int gridTotalHeight = gridHeight * (2 * cardRadius) + (gridHeight - 1) * padding;

        // Calculate the starting position to center the grid within the scene
        int startX = (sceneWidth - gridTotalWidth) / 2 -60;
        int startY = (sceneHeight - gridTotalHeight) / 2 -60;

        // get random coordinates for the chit cards
        List<int[]> randomCoordinates = RandomGeneration.getRandomPairs(NUM_CHIT_CARDS, RNG_SEED);

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
                spawnData.put("idx", chitCardIdx);
                spawnData.put("animalType", animalTypes[animalImagePathsIdx]);
                spawnData.put("animalCount", -dragonPirateCounter);
                // uncoveredShape
                Circle newChitCardShape = new Circle(28);
                newChitCardShape.setStrokeWidth(2);
                newChitCardShape.setStroke(Color.BLACK);
                Image image = new Image(getClass().getResource(animalImagePathMappings.get(animalTypes[animalImagePathsIdx])+dragonPirateCounter+".png").toExternalForm());
                newChitCardShape.setFill(new ImagePattern(image));
                uncoveredChitCardShapes[chitCardIdx] = newChitCardShape;
                spawnData.put("uncoveredShape", newChitCardShape);
                dragonPirateCounter += 1;
                if (dragonPirateCounter == DRAGON_PIRATE_COUNT+1) {
                    dragonPirateCounter = 1;
                }
            } else {
                if (animalCounter == ANIMAL_COUNT+1) {
                    animalCounter = 1;
                }
                spawnData.put("idx", chitCardIdx);
                spawnData.put("animalType", animalTypes[animalImagePathsIdx]);
                spawnData.put("animalCount", animalCounter);
                // uncoveredShape
                Circle newChitCardShape = new Circle(28);
                newChitCardShape.setStrokeWidth(2);
                newChitCardShape.setStroke(Color.BLACK);
                Image image = new Image(getClass().getResource(animalImagePathMappings.get(animalTypes[animalImagePathsIdx])+animalCounter+".png").toExternalForm());
                newChitCardShape.setFill(new ImagePattern(image));
                uncoveredChitCardShapes[chitCardIdx] = newChitCardShape;
                spawnData.put("uncoveredShape", newChitCardShape);
                animalCounter += 1;
            }

            FXGL.spawn("chitCard", spawnData);
            FXGL.spawn("uncoveredChitCard", spawnData);
            chitCardIdx += 1;
        }

        // now that chit card nodes have been populated, we can assign them to the variable chitCards
        coveredChitCardShapes = newChitCardFactory.getCoveredChitCards();

        // start game
        currPlayer = players[playerTurn-1];
        TextFactory newTextFactory = new TextFactory();
        FXGL.getGameWorld().addEntityFactory(newTextFactory);
        currentTextEntity = displayTurnUpdateMsg();
        firstTimeTurnMsg = false;
        System.out.println("Current player " + currPlayer.getId() + " is at " + volcanoRing.getVolcanoRing()[currPlayer.getDragonToken().getCurrentPosition()-1].getAnimal() + " volcano ring id: " + (currPlayer.getDragonToken().getCurrentPosition())
                + "curPosAngle " + currPlayer.getDragonToken().getCurrPosAngle());
        playTurn(currPlayer);

    }
    @Override
    protected void onUpdate(double tpf) {
        if (!COVERING_ANIMATION_IN_PROGRESS) {
            if (endGame) {
                if (firstTimeRemove) {
                    removeOldTurnUpdateMsg(currentTextEntity);
                    firstTimeRemove = false;
                }
                if (firstTimeWinMsg) {
                    displayMessage();
                    firstTimeWinMsg = false;
                }

            } else {
                if (currPlayer.getTurnEnded()) {
                    animationLock.lock();

                    // Continue with the rest of the code after the pause
                    removeOldTurnUpdateMsg(currentTextEntity);
                    System.out.println("Starting to cover up");
                    // do flip back animation, remove the old entities and create new ones for the covered cards
                    for (Circle uncoveredChitCard : uncoveredChitCards) {
                        System.out.println("The uncovered chit card fill is " + uncoveredChitCard.getFill());
                        // all the chit cards that were uncovered have been covered, so they need to start listening for clicks again
                        ChitCard chitCard = ChitCardFactory.getViewControllerMapping().get(uncoveredChitCard);
                        // animate

                        Circle visibleCircle = uncoveredChitCard;
                        Circle hiddenCircle = chitCard.getCoveredForm();

                        RotateTransition rotateTransition = new RotateTransition(Duration.millis(500), visibleCircle);
                        rotateTransition.setAxis(Rotate.Y_AXIS); // Rotate around the Y axis
                        rotateTransition.setByAngle(360); // Rotate 180 degrees
                        rotateTransition.setOnFinished(event -> {
                            visibleCircle.setVisible(false);
                            hiddenCircle.setVisible(true);
                            visibleCircle.getTransforms().clear(); // Clear the transformation
                        });
                        rotateTransition.play();

                        chitCard.setCovered(true);

                        //update spawn data, adds uncovered side
                        SpawnData coveredSpawnData = new SpawnData(chitCard.getSpawnData().getX(), chitCard.getSpawnData().getY());
                        coveredSpawnData.put("animalType", chitCard.getAnimalType());
                        coveredSpawnData.put("animalCount", chitCard.getAnimalCount());
                        coveredSpawnData.put("idx", chitCard.getIndex());
                        AnimalType animalType = chitCard.getAnimalType();
                        int animalCount;
                        if (animalType == AnimalType.DRAGON_PIRATE) {
                            animalCount = -chitCard.getAnimalCount();
                        } else {
                            animalCount = chitCard.getAnimalCount();
                        }
                        coveredSpawnData.put("imagePath", animalImagePathMappings.get(animalType) + animalCount + ".png");
                    }
                    animationLock.unlock();

                    uncoveredChitCards.clear();
                    currPlayer.setTurnEnded(false); // reset for next round
                    System.out.println(currPlayer.getId() + " 's turn ended");
                    if (playerTurn == players.length) {
                        playerTurn = 1;
                    } else {
                        playerTurn += 1;
                    } // reset to 1 if the 4th player ended their turn
                    currPlayer = players[playerTurn - 1];
                    System.out.println(currPlayer.getId() + " 's turn starts");
                    firstTimeTurnMsg = true;

                }

                if (!endGame) {
                    if (firstTimeTurnMsg) {
                        currentTextEntity = displayTurnUpdateMsg();
                        firstTimeTurnMsg = false;
                    }
                    playTurn(currPlayer);
                }
            }
        }
    }

    /**
     * Display winning message.
     */
    private void displayMessage(){

        SpawnData winMsgData = new SpawnData(20,40);
        winMsgData.put("winnerID", currPlayer.getId());
        FXGL.spawn("winningMsg", winMsgData);
    }

    private void removeOldTurnUpdateMsg(Entity textEntity){
        FXGL.getGameWorld().removeEntity(textEntity);
    }
    private Entity displayTurnUpdateMsg(){
        SpawnData turnUpdateMsgData = new SpawnData(20,40);
        turnUpdateMsgData.put("playerTurn", currPlayer.getId());
        return FXGL.spawn("turnUpdateMsg", turnUpdateMsgData);
    }

    public void handleCircleClick(Player player, Circle chitCardChosen){
        if (!endGame) {
            HashMap<Circle, ChitCard> mapping = ChitCardFactory.getViewControllerMapping();
            System.out.println("Chosen card is " + mapping.get(chitCardChosen).getAnimalCount() + " " + mapping.get(chitCardChosen).getAnimalType() + " with status " + mapping.get(chitCardChosen).isCovered() + " and its fill is " + chitCardChosen.getFill());
            ChitCard cardChosen = mapping.get(chitCardChosen);
            coveredChitCardShapes[cardChosen.getIndex()] = null; // this is so that it won't be listening for clicks on next turn
            if (cardChosen.isCovered()) {
                cardChosen.setCovered(false);
                uncoveredChitCards.add(uncoveredChitCardShapes[cardChosen.getIndex()]);
                System.out.println("Animating uncover");
                // do animation
                animationLock.lock();
                Circle visibleCircle = chitCardChosen;
                Circle hiddenCircle = uncoveredChitCardShapes[cardChosen.getIndex()];
                RotateTransition rotateTransition = new RotateTransition(Duration.millis(500), visibleCircle);
                rotateTransition.setAxis(Rotate.Y_AXIS); // Rotate around the Y axis
                rotateTransition.setByAngle(360); // Rotate 180 degrees
                COVERING_ANIMATION_IN_PROGRESS = true;
                rotateTransition.setOnFinished(event -> {
                    visibleCircle.setVisible(false);
                    hiddenCircle.setVisible(true);
                    visibleCircle.getTransforms().clear(); // Clear the transformation
                    COVERING_ANIMATION_IN_PROGRESS = false;

                });
                rotateTransition.play();
                System.out.println("Finished uncovering");
                animationLock.unlock();
                //update spawn data, adds uncovered side
                SpawnData newSpawnData = new SpawnData(cardChosen.getSpawnData().getX(), cardChosen.getSpawnData().getY());
                newSpawnData.put("uncoveredShape", cardChosen.getUncoveredForm());
                newSpawnData.put("idx", cardChosen.getIndex());
                newSpawnData.put("animalType", cardChosen.getAnimalType());
                newSpawnData.put("animalCount", cardChosen.getAnimalCount());

                int result = player.makeMove(volcanoRing, cardChosen); // result = 0 means end turn, otherwise it is destination ring ID that token should move to
                if (!player.getDoNothingContinueTurn()) {

                    if (result != 0) {
                        player.getDragonToken().moveToken(cardChosen.getAnimalCount()); //VIEW: move token
                        // update the volcano card's (the token is on) 'occupied' status to false
                        int currPositionRingID = player.getDragonToken().getCurrentPosition();
                        volcanoRing.getVolcanoCardByID(currPositionRingID).setOccupied(false);

                        // update the state: dragon token's position and total movement count
                        volcanoRing.getVolcanoCardByID(result).setOccupied(true);
                        player.getDragonToken().setCurrentPosition(volcanoRing.getVolcanoCardByID(result));
                        player.getDragonToken().updateTotalMovementCount((cardChosen.getAnimalCount()));

                        // update movedOutOfCave to True if exiting cave for the first time
                        if (!player.getDragonToken().getMovedOutOfCave()) {
                            player.getDragonToken().setMovedOutOfCave();
                        }
                        player.setDoNothingContinueTurn(true); // since we already did the animation, continue onto next flip
                    } else {
                        player.setTurnEnded(true);
                        // all the chit cards that were uncovered have been covered, so they need to start listening for clicks again
                        for (Circle uncoveredChitCard : uncoveredChitCards) {
                            ChitCard card = mapping.get(uncoveredChitCard);
                            coveredChitCardShapes[card.getIndex()] = card.getCoveredForm();
                        }
                    }
                } else {
                    System.out.println("Do nothing, continue turn");
                    // reset the value later on in line 258
                }

            } else { // if card is uncovered, clicking on it does nothing
                System.out.println("No point clicking on uncovered card");
                player.setDoNothingContinueTurn(true);
            }

            if (player.getDoNothingContinueTurn()) {
                System.out.println("Do nothing, continue turn");
                // reset the value later on in final part
                player.setDoNothingContinueTurn(false);
            }

            if (player.getDragonToken().getTotalMovementCount() == TOTAL_CARDS) { // end the game
                endGame = true;
            }
        }
    }

    private void playTurn(Player newPlayer){
//        System.out.println("Chit cards is "+chitCards);
        for (Circle circle : coveredChitCardShapes) {
            if (circle != null) {
                circle.setOnMouseClicked(event ->
                        handleCircleClick(newPlayer, circle));
            } else {
//                System.out.println("Card already flipped so it does not listen for clicks");
            }
        }



    }
}