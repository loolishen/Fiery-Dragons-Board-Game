package com.example.demo.Controller;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;

/**
 * Class used to display various text, including during player turn transitions and end game scenario
 */

public class TextDisplayManager {

    private Entity currentTextEntity;
    private boolean firstTimeRemove = true; // don't remove entities that are no longer there
    private boolean firstTimeWinMsg = true; // first time spawning win message
    private boolean firstTimeTurnMsg = true; // display player's turn message once only

    private static TextDisplayManager textDisplayManager;

    private TextDisplayManager(){}

    public static TextDisplayManager getInstance(){
        if (textDisplayManager == null){
            textDisplayManager = new TextDisplayManager();
        } return textDisplayManager;
    }

    public Entity getCurrentTextEntity() {
        return currentTextEntity;
    }

    public Entity displayTurnUpdateMsg(int currPlayerID){
        SpawnData turnUpdateMsgData = new SpawnData(20,40);
        turnUpdateMsgData.put("playerTurn", currPlayerID);
        return FXGL.spawn("turnUpdateMsg", turnUpdateMsgData);
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

    public void setCurrentTextEntity(Entity currentTextEntity) {
        this.currentTextEntity = currentTextEntity;
    }

    public boolean isFirstTimeRemove() {
        return firstTimeRemove;
    }

    public void setFirstTimeRemove(boolean firstTimeRemove) {
        this.firstTimeRemove = firstTimeRemove;
    }

    public boolean isFirstTimeWinMsg() {
        return firstTimeWinMsg;
    }

    public void setFirstTimeWinMsg(boolean firstTimeWinMsg) {
        this.firstTimeWinMsg = firstTimeWinMsg;
    }

    public boolean isFirstTimeTurnMsg() {
        return firstTimeTurnMsg;
    }

    public void setFirstTimeTurnMsg(boolean firstTimeTurnMsg) {
        this.firstTimeTurnMsg = firstTimeTurnMsg;
    }

    public void initialize(int currPlayerID){
        setCurrentTextEntity(displayTurnUpdateMsg(currPlayerID)); // might need to be refactored as attribute of TextManager class
        setFirstTimeTurnMsg(false);
    }

    public void handleEndGame(int currPlayerID){
        if (isFirstTimeRemove()) {
            removeOldTurnUpdateMsg(currentTextEntity);
            setFirstTimeRemove(false);
        }
        if (isFirstTimeWinMsg()) {
            displayWinMsg(currPlayerID);
            setFirstTimeWinMsg(false);
        }

    }

    public void handleTurnTransition(int currPlayerID){
        if (isFirstTimeTurnMsg()) {
            setCurrentTextEntity(displayTurnUpdateMsg(currPlayerID));
            setFirstTimeTurnMsg(false);
        }
    }

}
