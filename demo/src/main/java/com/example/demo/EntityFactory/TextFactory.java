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

}
