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
import com.example.demo.Model.Player;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;


/**
 * Main application class
 * @author Jia Wynn Khor
 */
public class FieryDragonsApplication extends GameApplication {
    public static boolean endGame = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(1000);
        gameSettings.setHeight(700);
        gameSettings.setTitle("Fiery Dragons");
        gameSettings.setVersion("1.0");
        gameSettings.setMainMenuEnabled(true);
        gameSettings.setDeveloperMenuEnabled(false);

        SceneFactory sceneFactory = new SceneFactory() {
            @Override
            public FXGLMenu newMainMenu() {
                return new MainMenu();
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

    @Override
    protected void initGame() {
        // Create the volcano ring card factory and add it to the game world and spawn them
        VolcanoRingFactory volcanoRingFactory = new VolcanoRingFactory(Config.VOLCANO_RING_NUM_CARDS, Config.VOLCANO_RING_NUM_CARDS / Config.VOLCANO_RING_SEGMENT_LENGTH);
        volcanoRingFactory.spawn();

        // let playerTurnManager manage creation of players
        PlayerTurnManager.getInstance();
        SpawnData data = new SpawnData();
        data.put("numPlayers", Config.NUM_PLAYERS);
        PlayerTurnManager.getInstance().createModel(data);

        // create dragon token factory and add it to game world
        DragonTokenFactory dragonTokenFactory = new DragonTokenFactory();
        // create cave factory and add it to game world
        CaveFactory caveFactory = new CaveFactory(Config.VOLCANO_RING_RADIUS);
        // spawn them
        dragonTokenFactory.spawn();
        caveFactory.spawn();

        // create chit card factory and add it to game world
        ChitCardFactory newChitCardFactory = new ChitCardFactory();
        newChitCardFactory.spawn();

        // now that chit card nodes have been populated, we can give them to the ChitCardFlipManager
        ChitCardFlipManager.getInstance();
        Circle[] coveredShapes = newChitCardFactory.getCoveredChitCards();
        ChitCardFlipManager.setCoveredChitCardShapes(coveredShapes);

        // add factory for buttons
        ButtonFactory buttonFactory = new ButtonFactory();
        buttonFactory.spawn();

        buttonFactory.setListeners();

        // text factory for producing text messages
        TextFactory newTextFactory = new TextFactory();
        newTextFactory.spawn();
        // initialize first player turn display message and state
        TextDisplayManager.getInstance().initialize(PlayerTurnManager.getInstance().getCurrPlayer().getId());
        // start first player's turn
//        PlayerTurnManager.getInstance().getCurrPlayer().setTurnEnded(false);


        playTurn(PlayerTurnManager.getInstance().getCurrPlayer());

    }


    @Override
    protected void onUpdate(double tpf) {
        if (!ChitCardFlipManager.getInstance().getAnimationInProgress()) {
            if (endGame) {
                TextDisplayManager.getInstance().handleEndGame(PlayerTurnManager.getInstance().getCurrPlayer().getId());
            } else {
                if (PlayerTurnManager.getInstance().getCurrPlayer().getTurnEnded()) {
                    TextDisplayManager.getInstance().removeOldTurnUpdateMsg(TextDisplayManager.getInstance().getCurrentTextEntity());
                    ChitCardFlipManager.getInstance().handleTurnEnd();
                    PlayerTurnManager.getInstance().handleTurnTransition();
                    TextDisplayManager.getInstance().setFirstTimeTurnMsg(true);

                }
                if (!endGame) {
                    TextDisplayManager.getInstance().handleTurnTransition(PlayerTurnManager.getInstance().getCurrPlayer().getId());
                    playTurn(PlayerTurnManager.getInstance().getCurrPlayer());
                }
            }
        }
    }

    /**
     * Calls the state managers to respond to the circle click
     *
     * @param player         the player who clicked the chit card
     * @param chitCardChosen the chit card shape clicked
     */
    private void handleCircleClick(Player player, Circle chitCardChosen) {
        if (!endGame) {
            // handle the uncovering of chit card
            ChitCardFlipManager.getInstance().handleUncover(chitCardChosen, ChitCardAdapter.getViewControllerMapping().get(chitCardChosen));

            int result = player.makeMove(chitCardChosen); // result = 0 means end turn, otherwise it is destination ring ID that token should move to
            if (!player.getDoNothingContinueTurn()) { // doNothingContinueTurn is True when card is dragon pirate and player is currently on cave (has not moved out)
                if (result != Config.END_TURN_RESULT) {
                    PlayerTurnManager.getInstance().moveToken(player, ChitCardAdapter.getViewControllerMapping().get(chitCardChosen).getAnimalCount(), result);

                } else {
                    player.setTurnEnded(true);
                    ChitCardFlipManager.getInstance().resetOnClickListener(ChitCardAdapter.getViewControllerMapping());
                }
            }
            if (player.getDoNothingContinueTurn()) {
                // reset the value
                player.setDoNothingContinueTurn(false);
            }
            if (player.getDragonToken().getTotalMovementCount() == Config.VOLCANO_RING_NUM_CARDS) { // end the game
                endGame = true;
            }
        }

    }

    /**
     * Used to listen for player's clicks on the chit cards
     *
     * @param newPlayer currentPlayer
     */
    private void playTurn(Player newPlayer) {
        for (Circle circle : ChitCardFlipManager.getInstance().getCoveredChitCardShapes()) {
            if (circle != null) {
                circle.setOnMouseClicked(event ->
                        handleCircleClick(newPlayer, circle));
            }
        }

    }
}