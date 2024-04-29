package main.java;

import main.java.characters.CharacterToken;
import main.java.characters.CharacterTypes;

import java.awt.*;

public class Player extends CharacterToken {
    public String playerName;

    public Player(Image image, String playerName) {
        super(image);
        this.playerName = playerName;
        this.addCapability(CharacterTypes.PLAYER);
    }
}
