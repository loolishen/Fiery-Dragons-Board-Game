package com.example.demo.Model;

import com.example.demo.Animals.Animal;
import com.example.demo.Animals.AnimalType;
import javafx.scene.shape.Circle;

/**
 * Stores the state of chit cards
 * @author Jia Wynn Khor
 */
public class ChitCard {
    private final int index;
    private final Animal animal;
    private Circle coveredForm; // this is default to all Chit cards
    private Circle uncoveredForm;
    private final double x;
    private final double y;
    private final boolean coveredVisible;
    private final boolean uncoveredVisible;

    public ChitCard(int idx, Animal newAnimal, Circle defaultChitCardShape, Circle newUncoveredForm, double x, double y, boolean covered, boolean uncovered){
        index = idx;
        coveredForm = defaultChitCardShape;
        uncoveredForm = newUncoveredForm;
        animal = newAnimal;
        this.x=x;
        this.y=y;
        this.coveredVisible = covered;
        this.uncoveredVisible=uncovered;
    }

    public void setCoveredForm(Circle coveredForm) {
        this.coveredForm = coveredForm;
    }

    public void setUncoveredForm(Circle uncoveredForm) {
        this.uncoveredForm = uncoveredForm;
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

    public Animal getAnimal(){
        return animal;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean isCoveredVisible() {
        return coveredVisible;
    }

    public boolean isUncoveredVisible() {
        return uncoveredVisible;
    }
}
