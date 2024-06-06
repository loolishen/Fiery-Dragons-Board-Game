package com.example.demo.EntityFactory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

/**
 * The ButtonFactory class is responsible for creating and managing the exit button entity, including its appearance,
 * position, and mouse event listeners for interaction effects such as changing colors and handling clicks.
 * It is static and does not need to be saved/loaded
 * @author Loo Li Shen
 */

public class ButtonFactory extends SpawnFactory {
    private Circle exitBtn;

    private static final ImagePattern menuBlack = new ImagePattern(new Image("/com/example/demo/assets/menuIconBlack.png"));
    private static final ImagePattern menuWhite = new ImagePattern(new Image("/com/example/demo/assets/menuIconWhite.png"));

    @Spawns("exitButton")
    public Entity exitBtn(SpawnData data){
        Group tokenGroup = new Group();

        Circle exitBtn = new Circle(30, 670, 30);
        exitBtn.setFill(menuBlack);

        tokenGroup.getChildren().addAll(exitBtn);
        this.exitBtn = exitBtn;
        return FXGL.entityBuilder(data).view(tokenGroup).build();

    }

    @Override
    public void spawn(boolean isNewGame) {
        super.spawn(isNewGame);
        FXGL.spawn("exitButton");
    }

    public void setListeners(){
        exitBtn.setOnMouseEntered(event-> {
            exitBtn.setFill(menuWhite);
        });
        exitBtn.setOnMouseExited(event-> {
            exitBtn.setFill(menuBlack);
        });

        exitBtn.setOnMouseClicked(event ->{
            FXGL.getGameController().gotoGameMenu();
        } );

    }

}
