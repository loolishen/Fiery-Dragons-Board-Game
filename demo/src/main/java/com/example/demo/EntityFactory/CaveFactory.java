package com.example.demo.EntityFactory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.example.demo.Animals.AnimalType;
import com.example.demo.Config;
import com.example.demo.Controller.PlayerTurnManager;
import com.example.demo.LoadSave;
import com.example.demo.Model.Cave;
import com.example.demo.Utils.Utils;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

/**
 * Used to spawn the caves according to the number of players playing
 * @author Loo Li Shen
 */
public class CaveFactory extends SpawnFactory implements LoadSave {
    private final PlayerTurnManager playerTurnManager;
    private final double circle_radius;
    private  Cave[] caves=new Cave[Config.NUM_PLAYERS];

    public CaveFactory(double newCircleRadius, PlayerTurnManager playerTurnManager){
        circle_radius = newCircleRadius;
        this.playerTurnManager = playerTurnManager;
    }

    @Spawns("loadCaves")
    public Entity loadCaves(SpawnData data){
        Group tokenGroup = new Group();
        for (Cave cave:caves){
            Circle caveShape = createCaveShapes(cave.getxPos(), cave.getyPos(), cave.getPlayerID());
        // Add the token view to the group
            tokenGroup.getChildren().add(caveShape);
        }
        return FXGL.entityBuilder(data).view(tokenGroup).build();
    }
    @Spawns("newCaves")
    public Entity newCaves(SpawnData data){
        Group tokenGroup = new Group();
        // Calculate the radius for positioning the tokens
        double tokenRadius = circle_radius + Config.CAVE_POS_RADIUS_OFFSET;

        int appCentreX = Config.SCENE_WIDTH / 2;
        int appCentreY = Config.SCENE_HEIGHT / 2;

        // Calculate the angle increment between each token position
        double angleIncrement = 360.0 / Config.NUM_PLAYERS;

        for (int i = 0; i < Config.NUM_PLAYERS; i++) {
            // Calculate the angle, x, and y coordinates for the token position
            double angle = Math.toRadians(i * angleIncrement+ Config.ANGLE_OFFSET);
            double x = appCentreX + tokenRadius * Math.cos(angle);
            double y = appCentreY + tokenRadius * Math.sin(angle);

            Circle cave = createCaveShapes(x,y,i);
            // Add the token view to the group
            tokenGroup.getChildren().add(cave);

        }
        return FXGL.entityBuilder(data).view(tokenGroup).build();
    }

    @Override
    public void spawn(boolean isNewGame) {
        super.spawn(isNewGame);
        if (isNewGame) {
            FXGL.spawn("newCaves");
        }else{ FXGL.spawn("loadCaves");}
    }

    private Circle createCaveShapes(double x, double y, int i){
        Circle cave = new Circle(x,y, Config.CAVE_CIRCLE_RADIUS);
        String imagePathPrefix = Config.ANIMAL_IMAGE_IMAGE_PATH_PREFIX_MAPPINGS.get(playerTurnManager.getPlayers()[i].getCaveAnimalType());
        caves[i]=new Cave(i, x, y, playerTurnManager.getPlayers()[i].getCaveAnimalType());
        Image animalImage = new Image(Objects.requireNonNull(getClass().getResource(imagePathPrefix  + "1.png")).toExternalForm());
        ImagePattern newImgPat = new ImagePattern(animalImage);
        cave.setFill(newImgPat); // Provide the path to your PNG image
        cave.setStroke(Config.COLORS[i]);
        cave.setStrokeWidth(3);
        return cave;
    }

    @Override
    public void load(int slotIndex){
        try (BufferedReader reader = Files.newBufferedReader(getSaveFilePath(slotIndex))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("CaveFactory")) {
                    while (!(line = reader.readLine()).equals("*")) {
                        String[] parts = line.split(",");
                        caves[Integer.parseInt(parts[0])]=new Cave(
                                Integer.parseInt(parts[0]),
                                Double.parseDouble(parts[1]),
                                Double.parseDouble(parts[2]),
                                Utils.stringTypeMapping(parts[3])
                        );
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void save(BufferedWriter writer, int slotIndex)throws IOException {
        // Save VolcanoRingFactory
        writer.write("CaveFactory\n");
        for (Cave cave:caves){
            writer.write(cave.getPlayerID()+","+ cave.getxPos()+","+ cave.getyPos()+","+cave.getAnimalType()+"\n");
        }
        writer.write("*\n");
    }
}
