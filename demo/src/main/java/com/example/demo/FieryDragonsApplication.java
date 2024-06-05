package com.example.demo;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.example.demo.Animals.AnimalType;
import com.example.demo.Animals.DragonPirate;
import com.example.demo.Controller.ChitCardAdapter;
import com.example.demo.Controller.ChitCardFlipManager;
import com.example.demo.Controller.PlayerTurnManager;
import com.example.demo.Controller.TextDisplayManager;
import com.example.demo.EntityFactory.*;
import com.example.demo.Model.*;
import com.example.demo.UserInterfaces.GameMenu;
import com.example.demo.UserInterfaces.LoadSaveUI;
import com.example.demo.UserInterfaces.MainMenu;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * Main application class
 * @author Jia Wynn Khor
 */
public class FieryDragonsApplication extends GameApplication implements LoadSave{
    private boolean startNewGame;
    public  boolean endGame = false;
    private boolean turnEnded = false;
    private final ChitCardAdapter chitCardAdapter = new ChitCardAdapter();
    private PlayerTurnManager playerTurnManager;
    private TextDisplayManager textDisplayManager=new TextDisplayManager();
    private LoadSaveUI loadSaveUI = new LoadSaveUI(this);

    private ArrayList<LoadSave> loadSaves = new ArrayList<>();
    private int slotToLoad=-1;

    private Shop shop;
    DragonToken dragonToken;
    Player player;

    public static void main(String[] args) {
        launch(args);
    }

    public ArrayList<LoadSave> getLoadSaves() {
        return loadSaves;
    }

    private void registerLoadSave(LoadSave loadSave){
        loadSaves.add(loadSave);
    }
    @Override
    protected void initSettings(GameSettings gameSettings) {

        int APP_WIDTH = 1000;
        gameSettings.setWidth(APP_WIDTH);
        int APP_HEIGHT = 700;
        gameSettings.setHeight(APP_HEIGHT);
        gameSettings.setTitle("Fiery Dragons");
        gameSettings.setVersion("1.0");
        gameSettings.setMainMenuEnabled(true);
        gameSettings.setDeveloperMenuEnabled(false);

        SceneFactory sceneFactory = new SceneFactory() {
            @Override
            public FXGLMenu newMainMenu() {
                return new MainMenu(loadSaveUI);
            }

            @Override
            public FXGLMenu newGameMenu(){
                GameMenu gameMenu= new GameMenu(loadSaveUI);
                return gameMenu;
            }

        };
        gameSettings.setSceneFactory(sceneFactory);
    }

    @Override
    protected void initUI() {
        // Load the background image
        Image backgroundImage = new Image("/com/example/demo/assets/background.png");
        // Set the background image to fill the entire game scene
        BackgroundSize backgroundSize = new BackgroundSize(
                1.0, // Width
                1.0, // Height
                true,               // Width auto-resize
                true,               // Height auto-resize
                false,                // Preserve aspect ratio
                false                // No repeat
        );
        FXGL.getGameScene().getRoot().setBackground(new Background(new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                backgroundSize)));
    }
    public boolean loadGame(int slotIndex) {
        slotToLoad = slotIndex;
        return true;
    }

    /**
     * This method creates the factories and state managers etc. according to saved file
     * @param slotIndex
     * @return
     */
    private void loadGameState(int slotIndex) {
        load(slotIndex);
        registerLoadSave(chitCardAdapter);
        // create a new player turn manager
        playerTurnManager = new PlayerTurnManager(textDisplayManager);
        registerLoadSave(playerTurnManager);

        // create volcano ring
        VolcanoRingFactory volcanoRingFactory = new VolcanoRingFactory(Config.VOLCANO_RING_NUM_CARDS, Config.VOLCANO_RING_NUM_CARDS / Config.VOLCANO_RING_SEGMENT_LENGTH);
        try {
            volcanoRingFactory.load(slotIndex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        registerLoadSave(volcanoRingFactory);
        volcanoRingFactory.spawn(false);

        // let playerTurnManager manage creation of players
        SpawnData data = new SpawnData();
        data.put("numPlayers", Config.NUM_PLAYERS);
        try {
            playerTurnManager.load(slotIndex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        DragonTokenFactory dragonTokenFactory = new DragonTokenFactory(playerTurnManager);
        dragonTokenFactory.spawn(false);
        CaveFactory caveFactory = new CaveFactory(Config.VOLCANO_RING_RADIUS, playerTurnManager);
        registerLoadSave(caveFactory);

        caveFactory.load(slotIndex);
        caveFactory.spawn(false);

        chitCardAdapter.load(slotIndex);
        // create chit card factory and add it to game world
        ChitCardFactory newChitCardFactory = new ChitCardFactory(chitCardAdapter);
        newChitCardFactory.spawn(false);

        // now that chit card nodes have been populated, we can give them to the ChitCardFlipManager
        ChitCardFlipManager.getInstance();

        // set uncoveredChitcards for ChitCardFlipManager based on chitcardadapter's chitcards
        ArrayList<Circle> uncoveredChitCardShapes = new ArrayList<>();
        Circle[] coveredShapes=new Circle[Config.NUM_CHIT_CARDS];
        int counter=0;
        for (ChitCard chitCard: chitCardAdapter.getChitCards()){
            if (chitCard.isUncoveredVisible()){ // if the chit card is uncovered
                uncoveredChitCardShapes.add(chitCard.getUncoveredForm());
            }
            else{
                coveredShapes[counter]=chitCard.getCoveredForm();
            }
            counter+=1;
        }

        ChitCardFlipManager.getInstance().setUncoveredChitCardsByPlayer(uncoveredChitCardShapes);
        ChitCardFlipManager.getInstance().setCoveredChitCardShapes(coveredShapes);
        // add factory for buttons
        ButtonFactory buttonFactory = new ButtonFactory();
        buttonFactory.spawn(false);
        buttonFactory.setListeners();

        // text factory for producing text messages
        TextFactory newTextFactory = new TextFactory();
        newTextFactory.spawn(false);
        // initialize first player turn display message and state
        textDisplayManager.initialize(playerTurnManager.getCurrPlayer().getId(),playerTurnManager.getCurrPlayer().getDragonToken().getTotalMovementCount() );

        // initialize point display
        textDisplayManager.initialize(playerTurnManager.getCurrPlayer().getId(), playerTurnManager.getCurrPlayer().getDragonToken().getTotalPointsCount());
        // start first player's turn
        playTurn();

    }

    @Override
    protected void initGame() {
        endGame = false;

        // if loading fails for whatever reason, then we start a new game
        if (slotToLoad==-1) {
            registerLoadSave(chitCardAdapter);

            playerTurnManager = new PlayerTurnManager(textDisplayManager);
            registerLoadSave(playerTurnManager);

            // Create the volcano ring card factory and add it to the game world and spawn them
            VolcanoRingFactory volcanoRingFactory = new VolcanoRingFactory(Config.VOLCANO_RING_NUM_CARDS, Config.VOLCANO_RING_NUM_CARDS / Config.VOLCANO_RING_SEGMENT_LENGTH);
            registerLoadSave(volcanoRingFactory);
            volcanoRingFactory.spawn(true);

            // let playerTurnManager manage creation of players
            SpawnData data = new SpawnData();
            data.put("numPlayers", Config.NUM_PLAYERS);
            playerTurnManager.createModel(data);

            // create dragon token factory and add it to game world
            DragonTokenFactory dragonTokenFactory = new DragonTokenFactory(playerTurnManager);
            // create cave factory and add it to game world
            CaveFactory caveFactory = new CaveFactory(Config.VOLCANO_RING_RADIUS, playerTurnManager);
            registerLoadSave(caveFactory);

            // spawn them
            dragonTokenFactory.spawn(true);
            caveFactory.spawn(true);

            // create chit card factory and add it to game world
            ChitCardFactory newChitCardFactory = new ChitCardFactory(chitCardAdapter);
            newChitCardFactory.spawn(true);

            // now that chit card nodes have been populated, we can give them to the ChitCardFlipManager
            ChitCardFlipManager.getInstance();
            Circle[] coveredShapes = newChitCardFactory.getCoveredChitCards();
            ChitCardFlipManager.getInstance().setCoveredChitCardShapes(coveredShapes);

            // add factory for buttons
            ButtonFactory buttonFactory = new ButtonFactory();
            buttonFactory.spawn(true);
            buttonFactory.setListeners();

            shop = new Shop();
            shop.addItem(new ShopItem("Shield", 0, "Protects you once from a pirate card", ItemType.SHIELD));
//            shop.addItem(new ShopItem("abcde", 10, "description stuff"));

            ShopFactory shopFactory = new ShopFactory(shop, playerTurnManager, textDisplayManager);
            shopFactory.spawn(true);

            // text factory for producing text messages
            TextFactory newTextFactory = new TextFactory();
            newTextFactory.spawn(true);
            // initialize first player turn display message and state
            textDisplayManager.initialize(1,0);
            // start first player's turn
            playTurn();
        }else{loadGameState(slotToLoad);}//for testing let's use slot 1
        slotToLoad =0;

    }

    public void setStartNewGame(boolean startNewGame) {
        this.startNewGame = startNewGame;
    }

    @Override
    protected void onUpdate(double tpf) {
        if (!ChitCardFlipManager.getInstance().getAnimationInProgress() && !turnEnded) {
            if (endGame) {
                textDisplayManager.handleEndGame(playerTurnManager.getWinner().getId());
            } else {
                if (playerTurnManager.getCurrPlayer().getTurnEnded()) {
                    turnEnded = true;
                    textDisplayManager.removeOldTurnUpdateMsg(textDisplayManager.getCurrentTextEntity());
                    textDisplayManager.removeOldMovementCountMsg(textDisplayManager.getCurrentMovementCountEntity());
                    ChitCardFlipManager.getInstance().handleTurnEnd(chitCardAdapter);
                    playerTurnManager.handleTurnTransition();
                    Player currPlayer = playerTurnManager.getCurrPlayer();
                    textDisplayManager.handleTurnTransition(currPlayer.getId());
                    textDisplayManager.handleMovementCountUpdate(currPlayer.getId(), currPlayer.getDragonToken().getTotalMovementCount());

                    textDisplayManager.handleRemoveOldPointsMsg(currPlayer.getId(), currPlayer.getPoints());
                    playTurn();
                    turnEnded=false;
                }
            }
        }
    }

    /**
     * Calls the state managers to respond to the circle click
     * @param chitCardChosen the chit card shape clicked
     */
    private void handleCircleClick(Circle chitCardChosen) {
        if (!endGame) {
            // handle the uncovering of chit card
            ChitCardFlipManager.getInstance().handleUncover(chitCardChosen, chitCardAdapter.getViewControllerMapping().get(chitCardChosen));

            AnimalType animalType = chitCardAdapter.getViewControllerMapping().get(chitCardChosen).getAnimal().getAnimalType();
            Player player = playerTurnManager.getCurrPlayer();

            if (animalType == AnimalType.DRAGON_PIRATE && player != null) {
                DragonPirate dragonPirate = (DragonPirate) chitCardAdapter.getViewControllerMapping().get(chitCardChosen).getAnimal();
                dragonPirate.handleSkullCardFlip(player);
            }

            int result = playerTurnManager.getCurrPlayer().validateMove(chitCardChosen, chitCardAdapter); // result = 0 means end turn, otherwise it is destination ring ID that token should move to
            if (!playerTurnManager.getCurrPlayer().getDoNothingContinueTurn()) { // this block is not executed when card is dragon pirate and player is currently on cave (has not moved out)
                if (result != Config.END_TURN_RESULT) {
                    int animalCount = chitCardAdapter.getViewControllerMapping().get(chitCardChosen).getAnimal().getCount();
                    playerTurnManager.getCurrPlayer().moveToken(animalCount, result, false);

                } else { // remember that if leprechaun also triggers this
                    playerTurnManager.getCurrPlayer().setTurnEnded(true);
                    ChitCardFlipManager.getInstance().resetOnClickListener(chitCardAdapter.getViewControllerMapping());
                }
            }
            if (playerTurnManager.getCurrPlayer().getDoNothingContinueTurn()) { // reset for next turn
                // reset the value
                playerTurnManager.getCurrPlayer().setDoNothingContinueTurn(false);
            }

            if (playerTurnManager.checkWinCondition()){
                endGame = true;
            }
        }

    }

    /**
     * Used to listen for player's clicks on the chit cards
     */
    private void playTurn() {
        for (Circle circle : ChitCardFlipManager.getInstance().getCoveredChitCardShapes()) {
            if (circle != null) {
                circle.setOnMouseClicked(event -> handleCircleClick(circle));
            }
        }

    }

    @Override
    public void load(int slotIndex) {
        try (BufferedReader reader = Files.newBufferedReader(getSaveFilePath(slotIndex))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("Application")) {
                    while (!(line = reader.readLine()).equals("*")) {
                        endGame = line.equals("true");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(BufferedWriter writer, int slotIndex) throws IOException {
        // Save current state
        writer.write("Application\n");
        writer.write(endGame+""+"\n");
        writer.write("*\n");
    }
}