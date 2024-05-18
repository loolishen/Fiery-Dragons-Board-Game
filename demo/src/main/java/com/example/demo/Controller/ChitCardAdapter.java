package com.example.demo.Controller;

import com.almasb.fxgl.entity.SpawnData;
import com.example.demo.Animals.AnimalType;
import com.example.demo.InitModel;
import com.example.demo.Model.ChitCard;
import javafx.scene.shape.Circle;

import java.util.HashMap;

/**
 * Used to adapt Circle into ChitCard by maintaining a mapping of view to model
 */
public class ChitCardAdapter implements InitModel {
    private  static final HashMap<Circle, ChitCard> viewModelMapping = new HashMap<>();

    public void addMapping(Circle viewComponent, ChitCard chitCard){
        viewModelMapping.put(viewComponent, chitCard);
    }

    public static HashMap<Circle, ChitCard> getViewControllerMapping(){
        return viewModelMapping;
    }

    @Override
    public void createModel(SpawnData data) {
        // get info from payload
        int index = data.get("idx");
        AnimalType animalType = data.get("animalType");
        int animalCount = data.get("animalCount");

        // get the covered and uncovered image components, and fill their associated arrays
        Circle coveredShape = data.get("covered");
        Circle uncoveredShape = data.get("uncovered");
        ChitCard chitCard = new ChitCard(index, animalType,animalCount, coveredShape, uncoveredShape);
        addMapping(coveredShape, chitCard);
        addMapping(uncoveredShape, chitCard);
    }
}
