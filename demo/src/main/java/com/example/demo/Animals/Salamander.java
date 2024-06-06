package com.example.demo.Animals;

import com.example.demo.Config;

/**
 * Salamander class
 * @author Maliha Tariq
 */

public class Salamander extends Animal{
    public Salamander(int count) {
        super(Config.ANIMAL_IMAGE_IMAGE_PATH_PREFIX_MAPPINGS.get(AnimalType.SALAMANDER), count, AnimalType.SALAMANDER);
    }

}
