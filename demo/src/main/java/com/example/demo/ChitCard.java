package com.example.demo;

import javafx.scene.shape.Circle;

public class ChitCard {
    private final int index;
    private final AnimalType animalType;
    private boolean covered;
    private final int animalCount;
    private final Circle coveredForm; // this is default to all Chit cards
    private final Circle uncoveredForm;

    public ChitCard(int idx, AnimalType newAnimalType, int newAnimalCount, Circle defaultChitCardShape, Circle newUncoveredForm){
        index = idx;
        covered = true;         // initially all chit cards covered
        coveredForm = defaultChitCardShape;
        uncoveredForm = newUncoveredForm;
        animalType = newAnimalType;
        animalCount = newAnimalCount;
    }

    public boolean isCovered() {
        return covered;
    }
    public int getIndex() {
        return index;
    }

    public Circle getUncoveredForm() {
        return uncoveredForm;
    }

    public void setCovered(boolean covered) {
        this.covered = covered;
    }

    public Circle getCoveredForm() {
        return coveredForm;
    }

    public AnimalType getAnimalType(){
        return animalType;
    }

    public int getAnimalCount(){
        return animalCount;
    }
}
