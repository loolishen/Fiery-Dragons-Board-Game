package com.example.demo.Controller;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.example.demo.Config;
import com.example.demo.Model.Player;

/**
 * Class used to display various text, including during player turn transitions and end game scenario
 * @author Liang Dizhen
 */

public class TextDisplayManager {
    private boolean gameEnd;
    private Entity currentTurnTextEntity;
    private Entity currentMovementCountEntity;

    private Entity currentPointsEntity;

    private Player currPlayer;


    public TextDisplayManager(){
    }

    public Entity getCurrentTextEntity() {
        return currentTurnTextEntity;
    }

    public Entity getCurrentMovementCountEntity() {
        return currentMovementCountEntity;
    }

    public Entity getCurrentPointsEntity(){
        return currentPointsEntity;
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

    public Entity displayPlayerPointsMsg1(int currPlayerID, int points) {
        SpawnData pointsMsgData = new SpawnData(20, 100);
        pointsMsgData.put("playerTurn", currPlayerID);
        pointsMsgData.put("playerPoints", points); // adding points data to SpawnData
        FXGL.spawn("playerPointsMsg", pointsMsgData);
        return  FXGL.spawn("playerPointsMsg", pointsMsgData);
    }
//
//    public void displayPlayerPointsMsg(int currPlayerID, int points) {
//        SpawnData pointsMsgData = new SpawnData(20, 70);
//        pointsMsgData.put("playerTurn", currPlayerID);
//        pointsMsgData.put("playerPoints", points); // adding points data to SpawnData
//        FXGL.spawn("playerPointsMsg", pointsMsgData);
//    }

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

    private void setCurrentPlayerPointsEntity(Entity pointsEntity){
        this.currentPointsEntity = pointsEntity;
    }

    public void initialize(int currPlayerID, int currDragonTokenMovementCount){
        gameEnd= true;
        setCurrentTextEntity(displayTurnUpdateMsg(currPlayerID));
        setCurrentMovementEntity(displayMovementCountMsg(currPlayerID, currDragonTokenMovementCount));
    }

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

    public void handleRemoveOldPointsMsg(int currPlayerID, int currPlayerPointsCount){
        setCurrentPlayerPointsEntity(displayPlayerPointsMsg1(currPlayerID,currPlayerPointsCount));
    }


    public void updatePointsDisplay(int currPlayerID, int points) {
        removeOldPointsMsg(currentPointsEntity);
        setCurrentPlayerPointsEntity(displayPlayerPointsMsg1(currPlayerID, points));
    }

//    public void showMessage(String message) {
//        SpawnData messageData = new SpawnData(20, 120);
//        messageData.put("text", message);
//        FXGL.spawn("message", messageData);
//    }

    private void removeOldPointsMsg(Entity pointsEntity) {
        if (pointsEntity != null) {
            FXGL.getGameWorld().removeEntity(pointsEntity);
        }
    }


}
