package com.example.demo.Controller;

import com.almasb.fxgl.entity.SpawnData;
import com.example.demo.Animals.Animal;
import com.example.demo.Animals.AnimalType;
import com.example.demo.Config;
import com.example.demo.EntityFactory.ChitCardFactory;
import com.example.demo.InitModel;
import com.example.demo.LoadSave;
import com.example.demo.Model.ChitCard;
import com.example.demo.Utils.Utils;
import javafx.scene.shape.Circle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

/**
 * Used to adapt Circle into ChitCard by maintaining a mapping of view to model
 * @author Liang Dizhen
 */
public class ChitCardAdapter implements InitModel, LoadSave {
    private final ChitCard[] chitCards = new ChitCard[Config.NUM_CHIT_CARDS];
    private final HashMap<Circle, ChitCard> viewModelMapping = new HashMap<>();

    public void addMapping(Circle viewComponent, ChitCard chitCard){
        viewModelMapping.put(viewComponent, chitCard);
    }

    public  HashMap<Circle, ChitCard> getViewControllerMapping(){
        return viewModelMapping;
    }

    @Override
    public void createModel(SpawnData data) {
        // get info from payload
        int index = data.get("idx");
        Animal animal = data.get("animal");
        // get the covered and uncovered image components, and fill their associated arrays
        Circle coveredShape = data.get("covered");
        Circle uncoveredShape = data.get("uncovered");
        double x = data.getX();
        double y = data.getY();
        ChitCard chitCard = new ChitCard(index, animal, coveredShape, uncoveredShape, x,y, true,false);
        chitCards[index]=chitCard;
        addMapping(coveredShape, chitCard);
        addMapping(uncoveredShape, chitCard);
    }

    public ChitCard[] getChitCards() {
        return chitCards;
    }

    @Override
    public void load(int slotIndex) {
        try (BufferedReader reader = Files.newBufferedReader(getSaveFilePath(slotIndex))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("ChitCardAdapter")) {
                    while (!(line = reader.readLine()).equals("*")) {
                        String[] parts = line.split(":");
                        String[] fields = parts[1].split(",");
                        int idx=Integer.parseInt(parts[0]);
                        chitCards[idx] = new ChitCard(idx, Animal.getAnimal(Utils.stringTypeMapping(fields[3]),Integer.parseInt(fields[2])),
                                null,// these don't need to be passed since ChitCardFactory handles them
                                null,
                                Double.parseDouble(fields[0]),
                                Double.parseDouble(fields[1]),
                                fields[4].equals("true"),
                                fields[5].equals("true")
                                );

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(BufferedWriter writer, int slotIndex) throws IOException {
        // Save current state
        writer.write("ChitCardAdapter\n");
        for (ChitCard chitCard:chitCards){
            writer.write(chitCard.getIndex() + ":" +
                    chitCard.getX()+","+
                    chitCard.getY()+","+
                    chitCard.getAnimal().getCount() + "," +
                    chitCard.getAnimal().getAnimalType()+ "," +
                    chitCard.getCoveredForm().visibleProperty().getValue()+ ","+
                    chitCard.getUncoveredForm().visibleProperty().getValue() + "\n");
        }
        writer.write("*\n");

    }
}
