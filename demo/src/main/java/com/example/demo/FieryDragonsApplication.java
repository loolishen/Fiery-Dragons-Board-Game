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
public class FieryDragonsApplication extends GameApplication implements LoadSave {
    public boolean endGame = false;
    private boolean turnEnded = false;
    private final ChitCardAdapter chitCardAdapter = new ChitCardAdapter();
    private PlayerTurnManager playerTurnManager;
    private TextDisplayManager textDisplayManager = new TextDisplayManager();
    private LoadSaveUI loadSaveUI = new LoadSaveUI(this);

    private ArrayList<LoadSave> loadSaves = new ArrayList<>();
    private int slotToLoad = -1;

    public static void main(String[] args) {
        launch(args);
    }

    public ArrayList<LoadSave> getLoadSaves() {
        return loadSaves;
    }

    private void registerLoadSave(LoadSave loadSave) {
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
            public FXGLMenu newGameMenu() {
                GameMenu gameMenu = new GameMenu(loadSaveUI);
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

    private void loadGameState(int slotIndex) {
        load(slotIndex);
        registerLoadSave(chitCardAdapter);
        playerTurnManager = new PlayerTurnManager(textDisplayManager);
        registerLoadSave(playerTurnManager);

        VolcanoRingFactory volcanoRingFactory = new VolcanoRingFactory(Config.VOLCANO_RING_NUM_CARDS, Config.VOLCANO_RING_NUM_CARDS / Config.VOLCANO_RING_SEGMENT_LENGTH);
        try {
            volcanoRingFactory.load(slotIndex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        registerLoadSave(volcanoRingFactory);
        volcanoRingFactory.spawn();

        SpawnData data = new SpawnData();
        data.put("numPlayers", Config.NUM_PLAYERS);
        try {
            playerTurnManager.load(slotIndex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        DragonTokenFactory dragonTokenFactory = new DragonTokenFactory(playerTurnManager);
        dragonTokenFactory.spawn();
        CaveFactory caveFactory = new CaveFactory(Config.VOLCANO_RING_RADIUS, playerTurnManager);
        registerLoadSave(caveFactory);

        caveFactory.load(slotIndex);
        caveFactory.spawn();

        chitCardAdapter.load(slotIndex);
        ChitCardFactory newChitCardFactory = new ChitCardFactory(chitCardAdapter);
        newChitCardFactory.spawn();

        ChitCardFlipManager.getInstance();

        ArrayList<Circle> uncoveredChitCardShapes = new ArrayList<>();
        Circle[] coveredShapes = new Circle[Config.NUM_CHIT_CARDS];
        int counter = 0;
        for (ChitCard chitCard : chitCardAdapter.getChitCards()) {
            if (chitCard.isUncoveredVisible()) {
                uncoveredChitCardShapes.add(chitCard.getUncoveredForm());
            } else {
                coveredShapes[counter] = chitCard.getCoveredForm();
            }
            counter += 1;
        }

        ChitCardFlipManager.getInstance().setUncoveredChitCardsByPlayer(uncoveredChitCardShapes);
        ChitCardFlipManager.getInstance().setCoveredChitCardShapes(coveredShapes);
        ButtonFactory buttonFactory = new ButtonFactory();
        buttonFactory.spawn();
        buttonFactory.setListeners();

        TextFactory newTextFactory = new TextFactory();
        newTextFactory.spawn();
        textDisplayManager.initialize(playerTurnManager.getCurrPlayer().getId(), playerTurnManager.getCurrPlayer().getDragonToken().getTotalMovementCount());
        playTurn();
    }

    @Override
    protected void initGame() {
        endGame = false;

        if (slotToLoad == -1) {
            registerLoadSave(chitCardAdapter);

            playerTurnManager = new PlayerTurnManager(textDisplayManager);
            registerLoadSave(playerTurnManager);

            VolcanoRingFactory volcanoRingFactory = new VolcanoRingFactory(Config.VOLCANO_RING_NUM_CARDS, Config.VOLCANO_RING_NUM_CARDS / Config.VOLCANO_RING_SEGMENT_LENGTH);
            registerLoadSave(volcanoRingFactory);
            volcanoRingFactory.spawn();

            SpawnData data = new SpawnData();
            data.put("numPlayers", Config.NUM_PLAYERS);
            playerTurnManager.createModel(data);

            DragonTokenFactory dragonTokenFactory = new DragonTokenFactory(playerTurnManager);
            CaveFactory caveFactory = new CaveFactory(Config.VOLCANO_RING_RADIUS, playerTurnManager);
            registerLoadSave(caveFactory);

            dragonTokenFactory.spawn();
            caveFactory.spawn();

            ChitCardFactory newChitCardFactory = new ChitCardFactory(chitCardAdapter);
            newChitCardFactory.spawn();

            ChitCardFlipManager.getInstance();
            Circle[] coveredShapes = newChitCardFactory.getCoveredChitCards();
            ChitCardFlipManager.getInstance().setCoveredChitCardShapes(coveredShapes);

            ButtonFactory buttonFactory = new ButtonFactory();
            buttonFactory.spawn();
            buttonFactory.setListeners();

            TextFactory newTextFactory = new TextFactory();
            newTextFactory.spawn();
            textDisplayManager.initialize(1, 0);
            playTurn();
        } else {
            loadGameState(slotToLoad);
        }
        slotToLoad = 0;
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
                    playTurn();
                    turnEnded = false;
                }
            }
        }
    }

    private void handleCircleClick(Circle chitCardChosen) {
        if (!endGame) {
            ChitCardFlipManager.getInstance().handleUncover(chitCardChosen, chitCardAdapter.getViewControllerMapping().get(chitCardChosen));
            Player currPlayer = playerTurnManager.getCurrPlayer();
            int result = currPlayer.validateMove(chitCardChosen, chitCardAdapter);

            if (!currPlayer.getDoNothingContinueTurn()) {
                if (result != Config.END_TURN_RESULT) {
                    int animalCount = chitCardAdapter.getViewControllerMapping().get(chitCardChosen).getAnimal().getCount();
                    currPlayer.moveToken(animalCount, result, false);
                    currPlayer.resetIncorrectRevealCounter();
                } else {
                    currPlayer.incrementIncorrectRevealCounter();
                    if (currPlayer.getIncorrectRevealCounter() >= 3) {
                        currPlayer.setEqualityBoost(true);
                        currPlayer.resetIncorrectRevealCounter();
                        FXGL.getNotificationService().pushNotification("Equality Boost awarded to Player " + currPlayer.getId());
                    }
                    currPlayer.setTurnEnded(true);
                    ChitCardFlipManager.getInstance().resetOnClickListener(chitCardAdapter.getViewControllerMapping());
                }
            }
            if (currPlayer.getDoNothingContinueTurn()) {
                currPlayer.setDoNothingContinueTurn(false);
            }

            if (currPlayer.hasEqualityBoost()) {
                currPlayer.useEqualityBoost();
            }

            if (playerTurnManager.checkWinCondition()) {
                endGame = true;
            }
        }
    }

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
        writer.write("Application\n");
        writer.write(endGame + "");
        writer.write("*\n");
    }
}
