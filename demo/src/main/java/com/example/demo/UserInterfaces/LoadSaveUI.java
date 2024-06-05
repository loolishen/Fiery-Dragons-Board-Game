package com.example.demo.UserInterfaces;
import com.almasb.fxgl.dsl.FXGL;
import com.example.demo.FieryDragonsApplication;
import com.example.demo.LoadSave;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class LoadSaveUI extends VBox{
    public VBox mainMenuContainer=new VBox(50);
    public VBox gameMenuContainer=new VBox(50);
    private boolean loadContainerInitialised = false;
    private boolean saveContainerInitialised = false;
    private final Rectangle[] slots = new Rectangle[8];
    private final FieryDragonsApplication application;
    private final String[] slotDescriptions = new String[8];//text
    private final Text[] slotTexts = new Text[8];

    public LoadSaveUI(FieryDragonsApplication application) {
        this.application=application;
    }

    private Path getSaveFilePath(int slotIndex) {
        String userHome = System.getProperty("user.home");
        return Paths.get(userHome, String.valueOf(slotIndex));
    }


    public void showSaveMenu(){
        // fetch latest save file metadata
        try {
            readSaveFileMetadata();
        }catch (IOException e){
            e.printStackTrace();
            try {
                createNewSaveFileMetadata();
            } catch (IOException e2){
                e2.printStackTrace();
            }
        }
        if (saveContainerInitialised){
            FXGL.removeUINode(gameMenuContainer);
        }
        gameMenuContainer = new VBox(50);
        gameMenuContainer.setAlignment(Pos.CENTER);
        gameMenuContainer.setPadding(new Insets(20));
        gameMenuContainer.setBackground(new Background(new BackgroundFill(Color.gray(0.1, 0.9), CornerRadii.EMPTY, Insets.EMPTY)));
        gameMenuContainer.prefWidthProperty().set(1000);
        gameMenuContainer.prefHeightProperty().set(700);
        Text title = new Text("Save Game");
        title.setFill(Color.WHITE);
        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> hide(gameMenuContainer));

        HBox slotsBox = new HBox(10);
        slotsBox.setAlignment(Pos.CENTER);
        for (int i = 0; i < 8; i++) {
            slots[i]= new Rectangle(100, 100, Color.GRAY);
            int slotIndex = i;
            slots[i].setOnMouseClicked(event -> handleSaveClick(slotIndex+1));

            // Create a Text node for displaying the slot index
            Text slotText = new Text();
            slotText.setText(slotDescriptions[i]);
            slotText.setFill(Color.WHITE);
            slotTexts[i]=slotText;
            // Create a VBox to hold both the rectangle and the text
            VBox slotPane = new VBox(5);
            slotPane.setAlignment(Pos.CENTER);
            slotPane.getChildren().addAll(slots[i],slotText);

            slotsBox.getChildren().add(slotPane);

        }
        gameMenuContainer.getChildren().addAll(title, closeButton, slotsBox);
        FXGL.addUINode(gameMenuContainer);
        gameMenuContainer.toFront();
        gameMenuContainer.setVisible(true);
        saveContainerInitialised=true;
    }


    private void readSaveFileMetadata() throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(getSaveFilePath(0))) {
            String line;
            int counter = 0;
            while ((line = reader.readLine()) != null) {
                if (line.equals("SaveInterface")) {
                    while (!(line = reader.readLine()).equals("*")) {
                        String[] parts = line.split("=");
                        if (parts[1].length()>1) {
                            System.out.println("parts[i] is "+ parts[1]);
                            slotDescriptions[counter] = parts[1];
                        } else {
                            slotDescriptions[counter] = String.valueOf(counter + 1);
                        }
                        counter += 1;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Save file does not exist");
            // if save file does not exist, initialise with slot index
            for (int i=0;i<8;i++){
                slotDescriptions[i]=i+1+"";
            }
        }
    }

    private void createNewSaveFileMetadata() throws IOException {
        try(BufferedWriter writer = Files.newBufferedWriter(getSaveFilePath(0))){
            // Save current state
            writer.write("SaveInterface\n");
            for (int i=0; i<8;i++){
                writer.write("Slot"+(i+1)+"="+""+ "\n");
            }
            writer.write("*\n");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * TODO: get from the save file
     */
    public void showMainMenu() {
        // try to read save file of metadata, if fail, try to create a new one
        try {
            readSaveFileMetadata();
        }catch (IOException e){
            e.printStackTrace();
            try {
                createNewSaveFileMetadata();
            } catch (IOException e2){
                e2.printStackTrace();
            }
        }

        if (loadContainerInitialised){
            for (int i = 0; i < 8; i++) {
                Text slotText=slotTexts[i];
                System.out.println("The description is "+slotDescriptions[i]);
                slotText.setText(slotDescriptions[i]);
                slotText.setVisible(true);
            }
            mainMenuContainer.setVisible(true);
        }else {
            mainMenuContainer.setAlignment(Pos.CENTER);
            mainMenuContainer.setPadding(new Insets(20));
            mainMenuContainer.setBackground(new Background(new BackgroundFill(Color.gray(0.1, 0.9), CornerRadii.EMPTY, Insets.EMPTY)));
            mainMenuContainer.prefWidthProperty().set(1000);
            mainMenuContainer.prefHeightProperty().set(700);
            Text title = new Text("Load Game");
            title.setFill(Color.WHITE);
            Button closeButton = new Button("Close");
            closeButton.setOnAction(event -> hide(mainMenuContainer));

            HBox slotsBox = new HBox(10);
            slotsBox.setAlignment(Pos.CENTER);
            for (int i = 0; i < 8; i++) {
                slots[i] = new Rectangle(100, 100, Color.GRAY);
                int slotIndex = i;
                slots[i].setOnMouseClicked(event -> handleLoadSlotClick(slotIndex+1));

                // Create a Text node for displaying the slot index
                Text slotText = new Text();

                slotText.setText(slotDescriptions[i]);
                slotText.setFill(Color.WHITE);
                slotTexts[i] = slotText;
                // Create a VBox to hold both the rectangle and the text
                VBox slotPane = new VBox(5);
                slotPane.setAlignment(Pos.CENTER);
                slotPane.getChildren().addAll(slots[i]);
                slotsBox.getChildren().add(slotPane);

            }
            mainMenuContainer.getChildren().addAll(title, closeButton, slotsBox);
            mainMenuContainer.toFront();
            mainMenuContainer.setVisible(true);
            loadContainerInitialised = true;
        }

    }

    private void handleSaveClick(int slotIndex){
        // Get the current timestamp
        LocalDateTime now = LocalDateTime.now();

        // Define a formatter for the timestamp
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Format the current timestamp
        String formattedTimestamp = now.format(formatter);
        slotDescriptions[slotIndex-1]=formattedTimestamp;
        slotTexts[slotIndex-1].setText(formattedTimestamp);
        // Implement save functionality
        try(BufferedWriter writer = Files.newBufferedWriter(getSaveFilePath(0))){
            // Save current state
            writer.write("SaveInterface\n");
            for (int i=0; i<8;i++){
                writer.write("Slot"+(i+1)+"="+slotDescriptions[i]+ "\n");
            }
            writer.write("*\n");
        }catch (IOException e) {
            e.printStackTrace();
        }
        try(BufferedWriter writer2 = Files.newBufferedWriter(getSaveFilePath(slotIndex))){
            for (LoadSave loadSave: application.getLoadSaves()){
                loadSave.save(writer2, slotIndex);
            }
            application.save(writer2, slotIndex);
        }catch (IOException e) {
            e.printStackTrace();
        }

        FXGL.getNotificationService().pushNotification("Game saved to slot "+ slotIndex);
    }

    public void handleLoadSlotClick(int slotIndex) {
        if (slotDescriptions[slotIndex-1].length()>1) {
            hide(mainMenuContainer);
            if (application.loadGame(slotIndex)) {
                FXGL.getGameController().startNewGame();
                FXGL.getNotificationService().pushNotification("Loading game from slot " + slotIndex);

            } else {
                FXGL.getNotificationService().pushNotification("Load failed.");
            }
        }
    }
    public void hide(VBox container) {
        container.setVisible(false);
    }

    public void loadNewGame(){
        application.setStartNewGame(true);
        if (application.loadGame(-1)) {
            FXGL.getGameController().startNewGame();
        }

    }



}
