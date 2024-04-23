package com.example.demo;

public class DragonToken {

    private int totalMovementCount;
    private VolcanoCard volcanoCard; // rationale: better than having reference to entire Volcano?
    private boolean movedOutOfCave;
    private int picResource;

    public void updateTotalMovementCount(int steps){
        totalMovementCount += steps;
    }

    public DragonToken(){
        movedOutOfCave = false; // by default has not moved out of cave
    }
    public int getCurrentPosition() {
        return volcanoCard.getRingID();
    }

    public void setCurrentPosition(VolcanoCard newVolcanoCard){
        volcanoCard = newVolcanoCard;
    }

    public boolean getMovedOutOfCave(){
        return movedOutOfCave;
    }

    public void setMovedOutOfCave(){
        movedOutOfCave = true;
    }

    public AnimalType getAnimalType(){
        return volcanoCard.getAnimal();
    }
}
