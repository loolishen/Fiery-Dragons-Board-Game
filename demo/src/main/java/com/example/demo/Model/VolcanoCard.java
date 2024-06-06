package com.example.demo.Model;


import com.example.demo.Animals.AnimalType;

/**
 * The VolcanoCard class represents a card in the volcano ring, containing information about its ring ID,
 * associated animal type, and occupancy status.
 * @author Jia Wynn Khor
 */

public class VolcanoCard {
    private final int ringID;
    private AnimalType animal;
    private boolean occupied;
    private final double xPos;
    private final double yPos;
    public VolcanoCard(int newRingID, AnimalType animalType, double xPos, double yPos, boolean occupied){
        ringID = newRingID;
        animal = animalType;
        this.xPos = xPos;
        this.yPos = yPos;
        this.occupied = occupied;

    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public boolean getOccupiedStatus(){
        return occupied;
    }

    public int getRingID(){
        return ringID;
    }

    public AnimalType getAnimal(){
        return animal;
    }

    public void setAnimal(AnimalType animal) {
        this.animal = animal;
    }


    public double getxPos() {
        return xPos;
    }

    public double getyPos() {
        return yPos;
    }
}
