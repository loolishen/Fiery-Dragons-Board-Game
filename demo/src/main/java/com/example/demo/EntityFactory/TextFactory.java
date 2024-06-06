package com.example.demo.EntityFactory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.example.demo.Config;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Provides the FXGL implementations for updating UI
 * @author Loo Li Shen
 */
public class TextFactory extends SpawnFactory {
    @Spawns("winningMsg")
    public Entity winMsg(SpawnData data){

        Text winningMessage = new Text("Player "+ data.get("winnerID") + " wins!");
        winningMessage.setFont(Font.font("Arial", 24));
        int playerID = data.get("winnerID");
        winningMessage.setFill(Config.COLORS[playerID-1]);

        return FXGL.entityBuilder(data).view(winningMessage).build();
    }
    @Spawns("turnUpdateMsg")
    public Entity turnUpdateMsg(SpawnData data){

        Text turnUpdateMsg = new Text("Player "+ data.get("playerTurn") + " 's turn");
        turnUpdateMsg.setFont(Font.font("Arial", 24));
        int playerID = data.get("playerTurn");
        turnUpdateMsg.setFill(Config.COLORS[playerID-1]);
        return FXGL.entityBuilder(data).view(turnUpdateMsg).build();
    }

    @Spawns("movementCountMsg")
    public Entity movementCountMsg(SpawnData data){
        int playerID = data.get("playerTurn");
        // movement count
        int movementCount = data.get("movementCount");
        Text movementCountText = new Text(data.getX()-20, data.getY()+10, "Movement count: "+movementCount);
        movementCountText.setFill(Config.COLORS[playerID-1]);
        movementCountText.setFont(Font.font("Arial", 24));

        return FXGL.entityBuilder(data).view(movementCountText).build();
    }

    @Spawns("playerPointsMsg")
    public Entity playerPointsMsg(SpawnData data) {
        int currPlayerID = data.get("playerTurn");
        int currPlayerPoints = data.get("playerPoints");
        Text pointsText = new Text(data.getX()-20, data.getY()+40, "Points: "+currPlayerPoints);
        pointsText.setFill(Config.COLORS[currPlayerID-1]);
        pointsText.setFont(Font.font("Arial", 24));
        return  FXGL.entityBuilder(data).view(pointsText).build();
    }

}
