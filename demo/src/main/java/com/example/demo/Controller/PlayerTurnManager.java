package com.example.demo.Controller;

import com.almasb.fxgl.entity.SpawnData;
import com.example.demo.Animals.AnimalType;
import com.example.demo.Config;
import com.example.demo.EntityFactory.VolcanoRingFactory;
import com.example.demo.InitModel;
import com.example.demo.Model.Player;
import com.example.demo.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Creates the players. Manages the players' turns. If a dragon token is moved, it calls upon the player to update its
 * dragon token's state, and asks the Volcano Cards involved tp toggle the occupied status
 * @author Liang Dizhen
 */

public class PlayerTurnManager implements InitModel {
    private final ArrayList<Integer> RAND_ANIMAL_CHOICES = new ArrayList<>(Arrays.asList(0,1,2,3));
    private static PlayerTurnManager playerTurnManager;
    private static Player[] players;
    private Player currPlayer;
    private int playerTurn;

    private PlayerTurnManager(){}
    public static PlayerTurnManager getInstance(){
        if (playerTurnManager == null){
            playerTurnManager = new PlayerTurnManager();
        }
        return playerTurnManager;
    }


    /**
     * Connascenece of execution: can only be called after getInstance() is called
     * @return the players
     */
    public static Player[] getPlayers() {
        return players;
    }
    @Override
    public void createModel(SpawnData data){
        int numPlayers = data.get("numPlayers");
        Utils.shuffleIntArray(RAND_ANIMAL_CHOICES, Config.NO_SEED); // random animal assigned
        players = new Player[numPlayers];
        // add players (to have player factory is over-engineering since we can add player name etc. in Player manager class, so no UI component needed
        for (int i =0; i<numPlayers; i++) {
            AnimalType randAnimalType = Config.ANIMAL_TYPES[RAND_ANIMAL_CHOICES.get(i)];
            Player newPlayer = new Player(i + 1, randAnimalType);
            players[i] = newPlayer;
        }
        playerTurn = 1; // always start with 1
        currPlayer = players[playerTurn-1];
    }

    public Player getCurrPlayer() {
        return currPlayer;
    }

    public void moveToken(Player player, int animalCount, int destinationID){
        player.getDragonToken().moveToken(animalCount); //VIEW: move token
        // update the volcano card's (the token is on) 'occupied' status to false
        int currPositionRingID = player.getDragonToken().getCurrentPositionInRing();
        VolcanoRingFactory.getVolcanoCardByID(currPositionRingID).setOccupied(false);

        // update the state: dragon token's position and total movement count
        VolcanoRingFactory.getVolcanoCardByID(destinationID).setOccupied(true);
        player.getDragonToken().setCurrentPosition(VolcanoRingFactory.getVolcanoCardByID(destinationID));
        player.getDragonToken().updateTotalMovementCount(animalCount);

        // update movedOutOfCave to True if exiting cave for the first time
        if (!player.getDragonToken().getMovedOutOfCave()) {
            player.getDragonToken().setMovedOutOfCave();
        }
        player.setDoNothingContinueTurn(true); // since we already did the animation, continue onto next flip
    }

    public void handleTurnTransition(){
        currPlayer.setTurnEnded(false); // reset for next round
        if (playerTurn == players.length) { // reset to 1 if the 4th player ended their turn
            playerTurn = 1;
        } else {
            playerTurn += 1;
        }
        currPlayer = players[playerTurn-1];
    }



}
