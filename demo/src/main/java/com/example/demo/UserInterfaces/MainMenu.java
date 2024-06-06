package com.example.demo.UserInterfaces;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.example.demo.Config;
import com.example.demo.FieryDragonsApplication;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * The MainMenu class defines the main menu of the application, including background image, menu buttons,
 * and their respective actions. Handles UI and user interactions for starting a new game, loading a saved game,
 * or quitting the game to the desktop.
 * @author Jia Wynn Khor
 */

public class MainMenu extends FXGLMenu {

    private static ObjectProperty<Button> selectedButton; // just for fancy visuals

    public MainMenu(LoadSaveUI loadSaveUI) {
        super(MenuType.MAIN_MENU);
        Image backGroundImage = new Image("/com/example/demo/assets/menu.png");
        Rectangle background = new Rectangle(0,0, Config.SCENE_WIDTH, Config.SCENE_HEIGHT);
        background.setFill(new ImagePattern(backGroundImage));
        Button btnPlayGame = new Button("New Game", "Start new game", loadSaveUI::loadNewGame);
        Button btnQuit = new Button("Quit Game", "Exit to desktop", this::fireExit);
        Button btnLoadGame = new Button("Load Game", "Load an existing game", loadSaveUI::showMainMenu);

        selectedButton = new SimpleObjectProperty<>(btnPlayGame);


        var textDescription = FXGL.getUIFactoryService().newText("",  Color.WHITE, 14.0);
        textDescription.textProperty().bind(
          Bindings.createStringBinding(()->selectedButton.get().description, selectedButton)
        );
        textDescription.setFill(Color.GRAY);
        var box = new VBox(10,
                btnPlayGame,
                btnLoadGame,
                btnQuit,
                new Separator(Orientation.HORIZONTAL),
                textDescription);

        box.setAlignment(Pos.CENTER_LEFT);
        box.setTranslateX(50);
        box.setTranslateY(530);

        getContentRoot().getChildren().addAll(background);
        getContentRoot().getChildren().addAll(box);
        getContentRoot().getChildren().addAll();
        getContentRoot().getChildren().addAll(loadSaveUI.mainMenuContainer);


    }

    // again, below two are for fancy visuals
    private static final Color SELECTED_COLOR = Color.BLACK;
    private static final Color NOT_SELECTED_COLOR = Color.GRAY;

    /**
     * Custome implementation for buttons on the main menu.
     */
    private static class Button extends StackPane {
        private final String description;

        public Button(String name, String description, Runnable action){
            this.description = description;

            Text text = FXGL.getUIFactoryService().newText(name, Color.WHITE, 20.0);
            text.fillProperty().bind(Bindings.when(focusedProperty()).then(SELECTED_COLOR).otherwise(NOT_SELECTED_COLOR));
            text.strokeProperty().bind(Bindings.when(focusedProperty()).then(SELECTED_COLOR).otherwise(NOT_SELECTED_COLOR));
            text.setStrokeWidth(0.5);

            Rectangle selector = new Rectangle(6, 17, Color.BLACK);
            selector.setTranslateX(-20);
            selector.setTranslateY(-2);
            selector.visibleProperty().bind(focusedProperty());

            focusedProperty().addListener((observable,oldValue,isSelected)->{
                if (isSelected){
                    selectedButton.setValue(this);
                }
            });

            setAlignment(Pos.CENTER_LEFT);
            setFocusTraversable(true);

            setOnKeyPressed(e -> {
                if (e.getCode()== KeyCode.ENTER){
                    action.run();
                }
            });

            setOnMouseEntered(e->{
                selectedButton.setValue(this);
            });

            setOnMouseClicked(e->{
                action.run();
            });

            getChildren().addAll(selector, text);
        }

    }


}
