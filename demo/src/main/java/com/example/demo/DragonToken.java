package com.example.demo;

public class DragonToken {

    private int totalMovementCount;
    private VolcanoCard volcanoCard; // rationale: better than having reference to entire Volcano?
    private boolean movedOutOfCave;
    private int picResource;

    public DragonToken(VolcanoCard initialVolcanoCard){
        volcanoCard = initialVolcanoCard;
        movedOutOfCave = false; // by default has not moved out of cave
    }

    public void updateTotalMovementCount(int steps){
        totalMovementCount += steps;
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
