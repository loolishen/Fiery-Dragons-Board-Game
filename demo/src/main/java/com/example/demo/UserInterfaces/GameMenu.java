package com.example.demo.UserInterfaces;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.example.demo.Config;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class GameMenu extends FXGLMenu{
    public GameMenu(LoadSaveUI loadInterface) {
        super(MenuType.GAME_MENU);
        Rectangle background = new Rectangle(0,0, Config.SCENE_WIDTH, Config.SCENE_HEIGHT);
        background.setFill(Color.rgb(255,255,255,0.4));
        Button btnResume = new Button("Resume", "Resume the game", this::fireResume);
        Button btnGoToMainMenu = new Button("Main Menu", "Go back to main menu", this::fireExitToMainMenu);
        Button btnSave = new Button("Save","Save game state", () -> {fireResume(); loadInterface.showSaveMenu();});

        double firstX = getAppWidth()/2.0-65;
        double firstY = getAppHeight()/2.0-20;
        btnResume.setTranslateX(firstX);
        btnResume.setTranslateY(firstY);
        btnGoToMainMenu.setTranslateX(firstX);
        btnGoToMainMenu.setTranslateY(firstY+50);
        btnSave.setTranslateX(firstX);
        btnSave.setTranslateY(firstY+100);

        getContentRoot().getChildren().addAll(background,btnResume, btnGoToMainMenu);
        getContentRoot().getChildren().addAll(btnSave);

    }

    private static class Button extends StackPane {
        private final String description;

        public Button(String name, String description, Runnable action){
            this.description = description;

            Text text = FXGL.getUIFactoryService().newText(name, Color.BLACK, 20.0);
            text.setStrokeWidth(0.5);

            // Add the text node to the StackPane
            getChildren().add(text);
            setPrefSize(130, 40);  // Example fixed width and height
            setAlignment(Pos.CENTER);
            setFocusTraversable(true);

            setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ENTER){
                    action.run();
                }
            });

            setOnMouseClicked(e -> {
                action.run();
            });

            // Set some visual properties for better visibility
            setStyle("-fx-background-color: grey; -fx-border-color: black; -fx-border-width: 3px;");
        }
    }
}
