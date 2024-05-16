package com.example.demo.EntityFactory;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


public class ButtonFactory extends SpawnFactory {
    private Circle exitBtn;
    private Text textDesc;

    private static final ImagePattern EXIT = new ImagePattern(new Image("/com/example/demo/assets/exit.png"));
    private static final ImagePattern EXIT_WHITE = new ImagePattern(new Image("/com/example/demo/assets/exitWhite.png"));

    @Spawns("exitButton")
    public Entity exitBtn(SpawnData data){
        Group tokenGroup = new Group();

        Circle exitBtn = new Circle(30, 650, 30);
        exitBtn.setFill(EXIT);

        Text textDesc = new Text("Quit");
        textDesc.setX(exitBtn.getCenterX()+20);
        textDesc.setY(exitBtn.getCenterY()+7);
        textDesc.setFill(Color.BLACK);
        textDesc.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        tokenGroup.getChildren().addAll(exitBtn);
        tokenGroup.getChildren().addAll(textDesc);
        this.exitBtn = exitBtn;
        this.textDesc=textDesc;
        return FXGL.entityBuilder(data).view(tokenGroup).build();

    }

    @Override
    public void spawn() {
        super.spawn();
        FXGL.spawn("exitButton");
    }

    public void setListeners(){
        exitBtn.setOnMouseEntered(event-> {
            exitBtn.setFill(EXIT_WHITE);
            textDesc.setFill(Color.WHITE);
        });
        exitBtn.setOnMouseExited(event-> {
            exitBtn.setFill(EXIT);
            textDesc.setFill(Color.BLACK);
        });

        exitBtn.setOnMouseClicked(event ->{
            FXGL.getGameController().gotoGameMenu();
        } );

    }

}
