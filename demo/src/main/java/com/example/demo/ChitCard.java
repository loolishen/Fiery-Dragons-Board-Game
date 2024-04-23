package com.example.demo;

public class ChitCard {

    private AnimalType animalType;
    private boolean covered;
    private int animalCount;

    public ChitCard(AnimalType newAnimalType, int newAnimalCount){
        animalType = newAnimalType;
        animalCount = newAnimalCount;
    }

    public boolean flipCard(AnimalType playerTokenAnimal){return false;}

    public AnimalType getAnimalType(){
        return animalType;
    }

    public int getAnimalCount(){
        return animalCount;
    }
}
