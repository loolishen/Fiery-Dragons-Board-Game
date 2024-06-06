package com.example.demo;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.example.demo.Controller.ChitCardAdapter;
import com.example.demo.Controller.ChitCardFlipManager;
import com.example.demo.Controller.PlayerTurnManager;
import com.example.demo.Controller.TextDisplayManager;
import com.example.demo.EntityFactory.*;
import com.example.demo.Model.ChitCard;
import com.example.demo.Model.Player;
import com.example.demo.Model.Shop;
import com.example.demo.Model.ShopItem;
import com.example.demo.UserInterfaces.GameMenu;
import com.example.demo.UserInterfaces.LoadSaveUI;
import com.example.demo.UserInterfaces.MainMenu;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

import javax.swing.text.DefaultHighlighter;
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
    public  boolean endGame = false;
    private boolean turnEnded = false;
    private final ChitCardAdapter chitCardAdapter = new ChitCardAdapter();
    private PlayerTurnManager playerTurnManager;
    private final TextDisplayManager textDisplayManager=new TextDisplayManager();
    private final LoadSaveUI loadSaveUI = new LoadSaveUI(this);

    private ArrayList<LoadSave> loadSaves = new ArrayList<>();
    private int slotToLoad=-1;


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
        textDisplayManager.initialize(playerTurnManager.getCurrPlayer().getId(),
        playerTurnManager.getCurrPlayer().getDragonToken().getTotalMovementCount(),
        playerTurnManager.getCurrPlayer().getPoints());

        // shop factory does not need to be registered, it is static
        Shop newShop = new Shop();
        ShopFactory shopFactory = new ShopFactory(newShop, playerTurnManager, textDisplayManager);
        shopFactory.spawn(false);

        // start first player's turn
        playTurn();

    }

    @Override
    protected void initGame() {
        endGame = false;
        loadSaves = new ArrayList<>();

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

            // text factory for producing text messages
            TextFactory newTextFactory = new TextFactory();
            newTextFactory.spawn(true);
            // initialize first player turn display message and state
            textDisplayManager.initialize(1,0, 0);
            // shop factory does not need to be registered, it is static
            Shop newShop = new Shop();
            ShopItem newItem = new ShopItem("SHIELD", 5, "Shield that grants immunity to dragon pirate's effect");
            newShop.addItem(newItem);
            ShopFactory shopFactory = new ShopFactory(newShop, playerTurnManager, textDisplayManager);
            shopFactory.spawn(true);

            // start first player's turn
            playTurn();
        }else{loadGameState(slotToLoad);}//for testing let's use slot 1
        slotToLoad =0;

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
                    textDisplayManager.removeOldPointsMsg(textDisplayManager.getCurrentPointsEntity());
                    ChitCardFlipManager.getInstance().handleTurnEnd(chitCardAdapter);
                    playerTurnManager.handleTurnTransition();
                    Player currPlayer = playerTurnManager.getCurrPlayer();
                    textDisplayManager.handleTurnTransition(currPlayer.getId());
                    textDisplayManager.handleMovementCountUpdate(currPlayer.getId(), currPlayer.getDragonToken().getTotalMovementCount());
                    textDisplayManager.updatePointsDisplay(currPlayer.getId(), currPlayer.getPoints());
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
            int result = playerTurnManager.getCurrPlayer().validateMove(chitCardChosen, chitCardAdapter); // result = 0 means end turn, otherwise it is destination ring ID that token should move to
            System.out.println("The result is "+result);
            if (!playerTurnManager.getCurrPlayer().getDoNothingContinueTurn()) { // this block is not executed when card is dragon pirate and player is currently on cave (has not moved out)
                if (result != Config.END_TURN_RESULT) {
                    int animalCount = chitCardAdapter.getViewControllerMapping().get(chitCardChosen).getAnimal().getCount();
                    playerTurnManager.getCurrPlayer().moveToken(animalCount, result, false);
                    playerTurnManager.getCurrPlayer().resetIncorrectRevealCounter();
                } else { // remember that leprechaun also triggers this
                    playerTurnManager.getCurrPlayer().setTurnEnded(true);
                    ChitCardFlipManager.getInstance().resetOnClickListener(chitCardAdapter.getViewControllerMapping());
                    playerTurnManager.getCurrPlayer().incrementIncorrectRevealCounter();
                    if (playerTurnManager.getCurrPlayer().getIncorrectRevealCounter() >= 3) {
                        playerTurnManager.getCurrPlayer().setEqualityBoost(true);
                        playerTurnManager.getCurrPlayer().resetIncorrectRevealCounter();
                        playerTurnManager.getCurrPlayer().useEqualityBoost();
                        FXGL.getNotificationService().pushNotification("Equality Boost awarded to Player " + playerTurnManager.getCurrPlayer().getId());
                    }

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