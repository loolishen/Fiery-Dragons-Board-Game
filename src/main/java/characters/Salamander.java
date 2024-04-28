package main.java.characters;

import java.awt.*;

// make a control panel to make everything work and call the image of salamander there

public class Salamander extends CharacterToken{

    public Salamander(Image image) {
        super(image);
        this.addCapability(CharacterTypes.SALAMANDER);
    }
}
