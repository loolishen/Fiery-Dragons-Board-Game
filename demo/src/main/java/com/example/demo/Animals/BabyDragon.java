package com.example.demo.Animals;

import com.example.demo.Config;


public class BabyDragon extends Animal{
    public BabyDragon(int count) {
        super(Config.ANIMAL_IMAGE_IMAGE_PATH_PREFIX_MAPPINGS.get(AnimalType.BABY_DRAGON), count);
    }
}
