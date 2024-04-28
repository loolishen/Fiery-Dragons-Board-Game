package main.java;

import java.awt.*;

public abstract class Character{
    public String name;
    public Image image;
    public int movementCount;

    public Character(String name, Image image, int movementCount){
        this.name = name;
        this.image = image;
        this.movementCount = movementCount;

    }


}