package com.example.demo.Controller;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.example.demo.Config;

/**
 * Class used to display various text, including during player turn transitions and end game scenario
 * @author Liang Dizhen
 */

public class TextDisplayManager {
    private boolean gameEnd;
    // below three help with ensuring that the text is only displayed once
    private Entity currentTurnTextEntity;
    private Entity currentMovementCountEntity;
    private Entity currentPointsEntity;

    public TextDisplayManager(){
    }

    public Entity getCurrentTextEntity() {
        return currentTurnTextEntity;
    }

    public Entity getCurrentMovementCountEntity() {
        return currentMovementCountEntity;
    }

    public Entity displayTurnUpdateMsg(int currPlayerID){
        SpawnData turnUpdateMsgData = new SpawnData(20,40);
        turnUpdateMsgData.put("playerTurn", currPlayerID);
        return FXGL.spawn("turnUpdateMsg", turnUpdateMsgData);
    }

    public Entity displayMovementCountMsg(int currPlayerID, int currPlayerMovementCount){
        SpawnData movementCountMsgData = new SpawnData(20,40-10);
        movementCountMsgData.put("playerTurn", currPlayerID);
        movementCountMsgData.put("movementCount", currPlayerMovementCount);
        return  FXGL.spawn("movementCountMsg", movementCountMsgData);

    }

    /**
     * Display winning message.
     */
    public void displayWinMsg(int currPlayerID){
        SpawnData winMsgData = new SpawnData(20,40);
        winMsgData.put("winnerID", currPlayerID);
        FXGL.spawn("winningMsg", winMsgData);
    }

    public void removeOldTurnUpdateMsg(Entity textEntity){
        FXGL.getGameWorld().removeEntity(textEntity);
    }

    private void setCurrentTextEntity(Entity currentTextEntity) {
        this.currentTurnTextEntity = currentTextEntity;
    }

    public void removeOldMovementCountMsg(Entity movementEntity){
        FXGL.getGameWorld().removeEntity(movementEntity);
    }

    private void setCurrentMovementEntity(Entity movementEntity){
        this.currentMovementCountEntity = movementEntity;
    }

    /**
     * Used for initialising the texts based on the supplied parameters. This works for loading a game as well
     * @param currPlayerID the current player
     * @param currDragonTokenMovementCount the current player's movement count
     * @param currPoints the current player's points (used for purchasing shop items)
     */
    public void initialize(int currPlayerID, int currDragonTokenMovementCount, int currPoints){
        gameEnd= true;
        setCurrentTextEntity(displayTurnUpdateMsg(currPlayerID));
        setCurrentMovementEntity(displayMovementCountMsg(currPlayerID, currDragonTokenMovementCount));
        setCurrentPlayerPointsEntity(displayPlayerPointsMsg1(currPlayerID, currPoints));
    }

    /**
     * Used to handle the end of the game. Once the winner message is displayed we don't display anything else
     * @param currPlayerID
     */
    public void handleEndGame(int currPlayerID){
        if (gameEnd) {
            removeOldTurnUpdateMsg(currentTurnTextEntity);
            displayWinMsg(currPlayerID);
            removeOldMovementCountMsg(currentMovementCountEntity);
            displayMovementCountMsg(currPlayerID, Config.VOLCANO_RING_NUM_CARDS);
        }
        gameEnd = false;

    }

    public void handleTurnTransition(int currPlayerID){
        setCurrentTextEntity(displayTurnUpdateMsg(currPlayerID));
    }

    public void handleMovementCountUpdate(int currPlayerID, int currPlayerMovementCount){
        setCurrentMovementEntity(displayMovementCountMsg(currPlayerID, currPlayerMovementCount));
    }

    public Entity displayPlayerPointsMsg1(int currPlayerID, int points) {
        SpawnData pointsMsgData = new SpawnData(20, 30);
        pointsMsgData.put("playerTurn", currPlayerID);
        pointsMsgData.put("playerPoints", points); // adding points data to SpawnData
        return  FXGL.spawn("playerPointsMsg", pointsMsgData);
    }

    private void setCurrentPlayerPointsEntity(Entity pointsEntity){
        this.currentPointsEntity = pointsEntity;
    }

    public void updatePointsDisplay(int currPlayerID, int points) {
        setCurrentPlayerPointsEntity(displayPlayerPointsMsg1(currPlayerID, points));
    }

    public void removeOldPointsMsg(Entity pointsEntity) {
        FXGL.getGameWorld().removeEntity(pointsEntity);
    }

    public Entity getCurrentPointsEntity() {
        return currentPointsEntity;
    }
}
