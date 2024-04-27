package com.example.demo;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.shape.Circle;

public class FieryDragonsApplication extends GameApplication {
    private boolean endGame = false;

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
        // Create the volcano ring card factory and add it to the game world and spawn them
        VolcanoRingFactory newVolcanoRingFactory = new VolcanoRingFactory(Constants.VOLCANO_RING_NUM_CARDS, Constants.VOLCANO_RING_NUM_CARDS/Constants.VOLCANO_RING_SEGMENT_LENGTH);
        newVolcanoRingFactory.spawn();

        // let playerTurnManager manage creation of players
        PlayerTurnManager.getInstance();
        PlayerTurnManager.getInstance().createPlayers(Constants.NUM_PLAYERS);

        // create dragon token factory and add it to game world and spawn them
        DragonTokenFactory dragonTokenFactory = new DragonTokenFactory(VolcanoRingFactory.CIRCLE_RADIUS, PlayerTurnManager.getPlayers());
        dragonTokenFactory.spawn();
        // create cave factory and add it to game world
        CaveFactory caveFactory = new CaveFactory(Constants.NUM_PLAYERS, VolcanoRingFactory.CIRCLE_RADIUS);
        caveFactory.spawn();
        // create chit card factory and add it to game world, and spawn them
        ChitCardFactory newChitCardFactory = new ChitCardFactory();
        newChitCardFactory.spawn();

        // now that chit card nodes have been populated, we can give them to the ChitCardFlipManager
        ChitCardFlipManager.getInstance();
        ChitCardFlipManager.setCoveredChitCardShapes(newChitCardFactory.getCoveredChitCards());
        // start game
        PlayerTurnManager.getInstance();
        PlayerTurnManager.getInstance().setCurrPlayer(PlayerTurnManager.getPlayers()[PlayerTurnManager.getInstance().getPlayerTurn()-1]); // Should be something like getCurrentPlayer from the PlayerTurnManager class
        TextFactory newTextFactory = new TextFactory();
        newTextFactory.spawn();

        // initialize first player turn display message and state
        TextDisplayManager.getInstance();
        TextDisplayManager.getInstance().initialize(PlayerTurnManager.getInstance().getCurrPlayer().getId());

        // start first player's turn
        playTurn(PlayerTurnManager.getInstance().getCurrPlayer());

    }
    @Override
    protected void onUpdate(double tpf) {
        if (!ChitCardFlipManager.getInstance().getANIMATION_IN_PROGRESS()) {
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

    private void handleCircleClick(Player player, Circle chitCardChosen){
        // handle the uncovering of chit card
        ChitCardFlipManager.getInstance().handleUncover(chitCardChosen, ChitCardFactory.getViewControllerMapping().get(chitCardChosen));

        int result = player.makeMove(VolcanoRingFactory.volcanoRing, ChitCardFactory.getViewControllerMapping().get(chitCardChosen)); // result = 0 means end turn, otherwise it is destination ring ID that token should move to
//        System.out.println("Result "+result);
        if (!player.getDoNothingContinueTurn()) {
//            System.out.println("Need to move");
            if (result != Constants.END_TURN_RESULT) {
                PlayerTurnManager.getInstance().moveToken(player, ChitCardFactory.getViewControllerMapping().get(chitCardChosen), result);

            } else {
                player.setTurnEnded(true);
                ChitCardFlipManager.getInstance().resetOnClickListener(ChitCardFactory.getViewControllerMapping());
            }
        }
        if (player.getDoNothingContinueTurn()) {
//            System.out.println("Do nothing");
            // reset the value
            player.setDoNothingContinueTurn(false);
        }
        if (player.getDragonToken().getTotalMovementCount() == Constants.VOLCANO_RING_NUM_CARDS) { // end the game
            endGame = true;
        }

    }

    private void playTurn(Player newPlayer){
        for (Circle circle : ChitCardFlipManager.getInstance().getCoveredChitCardShapes()) {
            if (circle!=null) {
                circle.setOnMouseClicked(event ->
                        handleCircleClick(newPlayer, circle));
            }
        }
    }
}