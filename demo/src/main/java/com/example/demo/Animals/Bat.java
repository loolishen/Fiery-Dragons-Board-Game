package com.example.demo.Animals;

import com.example.demo.Config;

/**
 * Bat class
 * @author Maliha Tariq
 */

public class Bat extends Animal {
    public Bat(int count) {
        super(Config.ANIMAL_IMAGE_IMAGE_PATH_PREFIX_MAPPINGS.get(AnimalType.BAT), count, AnimalType.BAT);
    }


}
