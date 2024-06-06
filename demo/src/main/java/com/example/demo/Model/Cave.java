package com.example.demo.Model;

import com.example.demo.Animals.AnimalType;

/**
 * Stores the information of a cave necessary for enabling the load/save functionality
 */
public class Cave {
    private final int playerID;
    private final double xPos;
    private final double yPos;
    private final AnimalType animalType;
    public Cave(int idx, double xPos, double yPos, AnimalType animalType){
        this.playerID=idx;
        this.xPos=xPos;
        this.yPos =yPos;
        this.animalType=animalType;
    }

    public int getPlayerID() {
        return playerID;
    }

    public double getxPos() {
        return xPos;
    }

    public double getyPos() {
        return yPos;
    }

    public AnimalType getAnimalType() {
        return animalType;
    }
}
