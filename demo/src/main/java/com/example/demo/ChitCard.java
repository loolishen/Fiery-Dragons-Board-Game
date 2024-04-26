package com.example.demo;


import com.almasb.fxgl.entity.SpawnData;
import javafx.scene.shape.Circle;


public class ChitCard {
    private int index;
    private AnimalType animalType;
    private boolean covered;
    private int animalCount;

    private final Circle coveredForm; // this is default to all Chit cards
    private final Circle uncoveredForm;
    private final SpawnData spawnData;



    public ChitCard(int idx, AnimalType newAnimalType, int newAnimalCount, Circle defaultChitCardShape, Circle newUncoveredForm, SpawnData newData){
        index = idx;
        // initially all chit cards covered
        covered = true;
        spawnData = newData;

        // default covered form
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

    public void setIndex(int index) {
        this.index = index;
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

    public SpawnData getSpawnData() {
        return spawnData;
    }
}
