package com.example.demo.Animals;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Shape;

import java.util.Objects;

public abstract class Animal{
    private final String baseImgPath;
    private final int count;
    public Animal(String baseImgPath, int count){
        this.baseImgPath = baseImgPath;
        this.count = count;
    }

    /**
     * Creates the image and fill the given UI shapes with it. Must be called after initUIShape() has been called
     */
    public void fillUIShape(Shape uiShape){
        Image animalImage = new Image(Objects.requireNonNull(getClass().getResource(getSuffixedImgPath())).toExternalForm());
        ImagePattern animalImagePattern = new ImagePattern(animalImage);
        uiShape.setFill(animalImagePattern);
    }

    /**
     * Get the desired image path by appending with suffix number (1,2,3) to get the image with number of animals required
     * This can only be called after the baseImgPath field has been set
     * @return modified image path
     */
    private String getSuffixedImgPath(){
        return baseImgPath + "" + count + ".png";
    }

    /**
     * This method returns the specific animal instance based on the animal type and count desired
     * @param animalType Enum AnimalType
     * @param count number of animals
     * @return the specific Animal
     */
    public static Animal getAnimal(AnimalType animalType, int count){
        if (animalType == AnimalType.BABY_DRAGON){
            return new BabyDragon(count);
        }else if (animalType == AnimalType.BAT){
            return new Bat(count);
        }else if (animalType == AnimalType.SALAMANDER){
            return new Salamander(count);
        } else if (animalType == AnimalType.DRAGON_PIRATE){
            return new DragonPirate(count);
        } else if (animalType == AnimalType.SPIDER){
            return new Spider(count);
        } else {
            System.out.println("Error: Animal type does not match known animals");
            return null;
        }
    }
}
