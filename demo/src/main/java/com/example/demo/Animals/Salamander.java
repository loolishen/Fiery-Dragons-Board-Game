package com.example.demo.Animals;

import com.example.demo.Config;

public class Salamander extends Animal{
    public Salamander(int count) {
        super(Config.ANIMAL_IMAGE_IMAGE_PATH_PREFIX_MAPPINGS.get(AnimalType.SALAMANDER), count);
    }

}
