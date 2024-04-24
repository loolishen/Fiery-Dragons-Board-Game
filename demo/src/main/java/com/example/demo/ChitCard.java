package com.example.demo;

import com.almasb.fxgl.entity.SpawnData;
import javafx.animation.ScaleTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class ChitCard {
    private int index;
    private AnimalType animalType;
    private boolean covered;
    private int animalCount;

    private Circle coveredForm; // this is default to all Chit cards
    private Circle uncoveredForm;
    private SpawnData spawnData;

    private ScaleTransition flipToFront; // Animation to flip the card to the front side
    private ScaleTransition flipToBack;  // Animation to flip the card to the back side

    public ChitCard(AnimalType newAnimalType, int newAnimalCount, Circle defaultChitCardShape, Circle newUncoveredForm, SpawnData newSpawnData){
        index = newSpawnData.get("idx");
        spawnData = newSpawnData;
        // initially allchit cards covered
        covered = true;

        // default covered form
        coveredForm = defaultChitCardShape;
        uncoveredForm = newUncoveredForm;
        animalType = newAnimalType;
        animalCount = newAnimalCount;


        // initialize flip animations
        flipToFront = new ScaleTransition(Duration.millis(300), uncoveredForm);
        flipToFront.setToX(1);
        flipToFront.setToY(1);
        flipToFront.setFromX(0);
        flipToFront.setFromY(1);

        // Initialize the flip to back animation
        flipToBack = new ScaleTransition(Duration.millis(300), coveredForm);
        flipToBack.setToX(0);
        flipToBack.setToY(1);
        flipToBack.setFromX(1);
        flipToBack.setFromY(1);

    }

    public boolean flipCard(AnimalType playerTokenAnimal){
        // Set event handlers to flip the card on click
        coveredForm.setOnMouseClicked(event -> flipToFrontAnimation());
        return false;
    }

    public SpawnData getSpawnData() {
        return spawnData;
    }

    public void setSpawnData(SpawnData spawnData) {
        this.spawnData = spawnData;
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

    public void flipToFrontAnimation(){
        if (covered) {
            flipToFront.play();
            flipToBack.play();
            covered = false;
        } else {
            System.out.println("already flipped, no animation");
        }
    }

    public Circle getCoveredForm() {
        return coveredForm;
    }

    public void flipToBackAnimation(){
        flipToFront.play();
        flipToBack.play();
    }


    public AnimalType getAnimalType(){
        return animalType;
    }

    public int getAnimalCount(){
        return animalCount;
    }



}
