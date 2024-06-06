package com.example.demo.UserInterfaces;
import com.almasb.fxgl.dsl.FXGL;
import com.example.demo.Config;
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

/**
 * The user interface for saving/loading
 */
public class LoadSaveUI{
    public VBox mainMenuContainer=new VBox(50); // the main menu container is attached to the MainMenu
    public VBox gameMenuContainer=new VBox(50); // this is attached within the in-game scene
    // below two help with minimizing unnecessary object creation. we just show/hide instead
    private boolean loadContainerInitialised=false;
    private boolean saveContainerInitialised = false;
    private final Rectangle[] slots = new Rectangle[8]; // the rectangular 'slots'. Future potential: add a screenshot
    // of the board and fill the rectangle when a game is saved to that slot
    private final FieryDragonsApplication application;
    private final String[] slotDescriptions = new String[8]; //either the slot index or the timestamp
    private final Text[] slotTexts = new Text[8]; // the text node that contains the above slot description

    public LoadSaveUI(FieryDragonsApplication application) {
        this.application=application;
    }

    /**
     * We choose to save the file in the user's home directory for easy navigation
     */
    private Path getSaveFilePath(int slotIndex) {
        String userHome = System.getProperty("user.home");
        return Paths.get(userHome, String.valueOf(slotIndex));
    }


    /**
     * Display the save menu in-game
     */
    public void showSaveMenu(){
        // fetch latest save file metadata
        try {
            readSaveFileMetadata();
        }catch (IOException e){
            e.printStackTrace();
            // if no save file create one
            try {
                createNewSaveFileMetadata();
            } catch (IOException e2){
                e2.printStackTrace();
            }
        }
        // remove the menu and create a new one
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


    /**
     * Read the metadata of the saves (always in slot index 0, i.e the File with filename '0'
     * @throws IOException
     */
    private void readSaveFileMetadata() throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(getSaveFilePath(0))) {
            String line;
            int counter = 0;
            while ((line = reader.readLine()) != null) {
                if (line.equals("SaveInterface")) {
                    while (!(line = reader.readLine()).equals("*")) {
                        String[] parts = line.split("=");
                        if (parts[1].length()>1) {
                            slotDescriptions[counter] = parts[1];
                        } else {
                            slotDescriptions[counter] = (counter+1)+"";
                        }
                        counter += 1;
                    }
                }
            }
        } catch (IOException e) {
            // if save file does not exist, initialise with slot index
            for (int i=0;i<8;i++){
                slotDescriptions[i]=i+1+"";
            }
        }
    }

    /**
     * Create a new save file for the save metadata. Essentially this tells us whether the slots have a timestamp (save
     * file exists)
     * @throws IOException
     */
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
     * Shows the load interface when in the main menu
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
        // if created already, just get the updated slot descriptions and show it
        if (loadContainerInitialised){
            for (int i = 0; i < 8; i++) {
                slotTexts[i].setText(slotDescriptions[i]);
            }
            mainMenuContainer.setVisible(true);
        }else {
            // create this interface and show it
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
                slots[i].setOnMouseClicked(event -> handleLoadSlotClick(slotIndex + 1));

                // Create a Text node for displaying the slot index
                Text slotText = new Text();

                slotText.setText(slotDescriptions[i]);
                slotText.setFill(Color.WHITE);
                // Create a VBox to hold both the rectangle and the text
                VBox slotPane = new VBox(5);
                slotPane.setAlignment(Pos.CENTER);
                slotPane.getChildren().addAll(slots[i], slotText);
                slotsBox.getChildren().add(slotPane);
                slotTexts[i] = slotText; // this must be called after we have added it as the child
                // to ensure that the references still hold

            }
            mainMenuContainer.getChildren().addAll(title, closeButton, slotsBox);
            mainMenuContainer.toFront();
            mainMenuContainer.setVisible(true);
            loadContainerInitialised = true;
        }

    }

    /**
     * When player chooses to save in a slot, asks each of the LoadSave instances to save their info into the same file.
     * Polymorphic substitution occurs.
     * @param slotIndex slot index used for naming the save file
     */
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
            for (LoadSave loadSave: application.getLoadSaves()){ // polymorphic
                loadSave.save(writer2, slotIndex);
            }
            application.save(writer2, slotIndex); // application does not implement LoadSave to avoid
            // infinite recursion
        }catch (IOException e) {
            e.printStackTrace();
        }

        FXGL.getNotificationService().pushNotification("Game saved to slot "+ slotIndex);
    }

    /**
     * Load a game based on clicked slot. If slot is empty nothing occurs.
     */
    public void handleLoadSlotClick(int slotIndex) {
        if (slotDescriptions[slotIndex-1].length()>1) {
            hide(mainMenuContainer);
            if (application.loadGame(slotIndex)) { // this basically passes the slot index back to the application
                FXGL.getGameController().startNewGame();
                FXGL.getNotificationService().pushNotification("Loading game from slot " + slotIndex);

            }
        }
    }
    private void hide(VBox container) {
        container.setVisible(false);
    }

    /**
     * A method to start a new game. This is used by the main menu. By passing in slot index = -1, we tell the application
     * we are loading a game
     */
    public void loadNewGame(){
        if (application.loadGame(Config.NEW_GAME)) {
            FXGL.getGameController().startNewGame();
        }

    }



}
