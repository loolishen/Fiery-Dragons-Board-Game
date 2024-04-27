package com.example.demo;


import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;

public abstract class Spawnable implements EntityFactory {
    protected SpawnData spawnData;

    public void spawn(){
        FXGL.getGameWorld().addEntityFactory(this);
        spawnData = new SpawnData();
    }
}
