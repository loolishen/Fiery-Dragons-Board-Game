package com.example.demo;


public class VolcanoCard {
    private final int segmentID;
    private final int ringID;
    private final AnimalType animal;
    private boolean occupied;
    public VolcanoCard(int newSegmentID, int newRingID, AnimalType animalType){
        segmentID =newSegmentID;
        ringID = newRingID;
        animal = animalType;

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

}
