package com.example.demo.EntityFactory;


import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;


/**
 * Abstract class for classes that use the FXGL entity factory interface. Its default spawn()
 * method ensures that they register themselves to FXGL's game world as entity factories.
 * It requires them to call super() by default, and allows them to override the spawn() method to add to the SpawnData
 * payload.
 * @author Loo Li Shen
 */
public abstract class SpawnFactory implements EntityFactory {
    protected SpawnData spawnData;
    protected boolean isNewGame;

    /**
     * Spawns the entity into the game world by rendering it. It can only be added once into the FXGL gameworld
     * @param isNewGame boolean indicating whether it is a new game
     */
    public void spawn(boolean isNewGame){
        FXGL.getGameWorld().addEntityFactory(this);
        this.isNewGame = isNewGame;
        spawnData = new SpawnData();
    }

}
