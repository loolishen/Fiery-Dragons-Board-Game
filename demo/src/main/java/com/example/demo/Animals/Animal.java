package com.example.demo.Animals;

import com.almasb.fxgl.dsl.FXGL;
import com.example.demo.Config;
import com.example.demo.Controller.ChitCardAdapter;
import com.example.demo.EntityFactory.VolcanoRingFactory;
import com.example.demo.Model.DragonToken;
import com.example.demo.Model.Player;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Shape;

import java.util.Objects;

/**
 * The Animal class is an abstract base class representing an animal with an image path and count.
 * It provides methods to fill a UI shape with the animal's image and retrieve specific animal instances based on their type and count.
 * @author Maliha Tariq
 */

public abstract class Animal{
    public final String baseImgPath;
    protected final AnimalType animalType;
    protected final int count;
    public Animal(String baseImgPath, int count, AnimalType animalType){
        this.baseImgPath = baseImgPath;
        this.count = count;
        this.animalType = animalType;
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
    protected String getSuffixedImgPath(){
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
        } else if (animalType == AnimalType.LEPRECHAUN){
            return new Leprechaun(count);
        }
        else {
            return null;
        }

    }

    public int calculateDestinationID(Player player, DragonToken dragonToken, int animalCount){
        // access required information
        int currentPosInRing = dragonToken.getCurrentPositionInRing();
        int totalMovement = dragonToken.getTotalMovementCount();
        AnimalType animalUsedForChecking = dragonToken.getMovedOutOfCave()? dragonToken.getAnimalType():player.getCaveAnimalType();

        // cannot go past cave once a full round is made around the volcano
        // if animals don't match
        if (animalUsedForChecking != this.animalType || totalMovement+animalCount > Config.VOLCANO_RING_NUM_CARDS){
            player.setDoNothingContinueTurn(false);
            return Config.END_TURN_RESULT;
        }
        int destinationRingID; // hypothetical destination id in the volcano ring if player successfully makes a move
        destinationRingID = currentPosInRing + animalCount;
        // adjust destination ring id to always be within the range 1,24
        if( destinationRingID > Config.VOLCANO_RING_NUM_CARDS){
            destinationRingID -= Config.VOLCANO_RING_NUM_CARDS;
        }
        // no matter what, do not allow players to share same volcano card.
        boolean occupied = VolcanoRingFactory.getVolcanoCardByID(destinationRingID).getOccupiedStatus();

        // reset
        player.setDoNothingContinueTurn(false);
        if (occupied) {
            FXGL.getNotificationService().pushNotification("Destination is occupied!");
            return Config.END_TURN_RESULT;
        }
        return destinationRingID;
    }

    public AnimalType getAnimalType() {
        return animalType;
    }

    public int getCount() {
        return count;
    }
}
