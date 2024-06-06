package com.example.demo;

import com.example.demo.Animals.AnimalType;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


public interface LoadSave {

    default Path getSaveFilePath(int slotIndex) {
        String userHome = System.getProperty("user.home");
        return Paths.get(userHome, String.valueOf(slotIndex));
    }

    void load(int slotIndex) throws IOException;

    void save(BufferedWriter writer, int slotIndex) throws IOException;

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
