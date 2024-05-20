package com.example.demo.Model;

import com.example.demo.Animals.AnimalType;
import javafx.scene.shape.Circle;

/**
 * Stores the state of chit cards
 * @author Jia Wynn Khor
 */
public class ChitCard {
    private final int index;
    private final AnimalType animalType;
    private final int animalCount;
    private final Circle coveredForm; // this is default to all Chit cards
    private final Circle uncoveredForm;

    public ChitCard(int idx, AnimalType newAnimalType, int newAnimalCount, Circle defaultChitCardShape, Circle newUncoveredForm){
        index = idx;
        coveredForm = defaultChitCardShape;
        uncoveredForm = newUncoveredForm;
        animalType = newAnimalType;
        animalCount = newAnimalCount;
    }

    public int getIndex() {
        return index;
    }

    public Circle getUncoveredForm() {
        return uncoveredForm;
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
