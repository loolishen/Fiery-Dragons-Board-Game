package com.example.demo.Model;

import com.example.demo.Animals.AnimalType;

public class Cave {
    private int playerID;
    private double xPos;
    private double yPos;
    private AnimalType animalType;
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
