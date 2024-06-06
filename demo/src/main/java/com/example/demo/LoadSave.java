package com.example.demo;

import com.example.demo.Animals.AnimalType;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Classes that implement this are involved in the load/save process by providing implementations of what to save, and
 * how to parse the corresponding save file to initialise the state for a loaded game.
 */
public interface LoadSave {

    /**
     * As usual we load/save to user's hom directory

     */
    default Path getSaveFilePath(int slotIndex) {
        String userHome = System.getProperty("user.home");
        return Paths.get(userHome, String.valueOf(slotIndex));
    }

    void load(int slotIndex) throws IOException;

    void save(BufferedWriter writer, int slotIndex) throws IOException;

    /**
     * Since the (text) save files store the animal types as string, we need to be able to convert the String to the
     * equivalent Animal Type enum.
     * @param animalTypeString the animal string
     * @return the enum
     */
    default AnimalType stringTypeMapping(String animalTypeString){
        return switch (animalTypeString) {
            case "BAT" -> AnimalType.BAT;
            case "SALAMANDER" -> AnimalType.SALAMANDER;
            case "BABY_DRAGON" -> AnimalType.BABY_DRAGON;
            case "SPIDER" -> AnimalType.SPIDER;
            case "DRAGON_PIRATE"-> AnimalType.DRAGON_PIRATE;
            case "LEPRECHAUN"-> AnimalType.LEPRECHAUN;
            default -> null;
        };
    }
}
