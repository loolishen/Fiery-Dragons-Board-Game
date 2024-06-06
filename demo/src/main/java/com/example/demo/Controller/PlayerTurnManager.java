package com.example.demo.Controller;

import com.almasb.fxgl.entity.SpawnData;
import com.example.demo.Animals.AnimalType;
import com.example.demo.Config;
import com.example.demo.EntityFactory.VolcanoRingFactory;
import com.example.demo.InitModel;
import com.example.demo.LoadSave;
import com.example.demo.Model.DragonToken;
import com.example.demo.Model.Player;
import com.example.demo.Utils.Utils;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static com.example.demo.EntityFactory.DragonTokenFactory.DRAGON_TOKEN_IMAGE_PATHS;

/**
 * Creates the players. Manages the players' turns. If a dragon token is moved, it calls upon the player to update its
 * dragon token's state, and asks the Volcano Cards involved tp toggle the occupied status
 * @author Liang Dizhen
 */

public class PlayerTurnManager implements InitModel, LoadSave {
    private final ArrayList<Integer> RAND_ANIMAL_CHOICES;
    private final Player[] players=new Player[Config.NUM_PLAYERS];
    private Player currPlayer;
    private int playerTurn;
    private Player winner;
    private final TextDisplayManager textDisplayManager;

    public PlayerTurnManager(TextDisplayManager textDisplayManager){
        RAND_ANIMAL_CHOICES = new ArrayList<>(Arrays.asList(0,1,2,3));
        Utils.shuffleIntArray(RAND_ANIMAL_CHOICES, Config.RNG_SEED); // random animal assigned
        this.textDisplayManager = textDisplayManager;
    }


    /**
     * Connascence of execution: can only be called after getInstance() is called
     * @return the players
     */
    public Player[] getPlayers() {
        return players;
    }

    /**
     * Creates the array of players and initialises the first player as current player. This is only used for new games
     * @param data payload containing the number of players to create
     */
    @Override
    public void createModel(SpawnData data){
        int numPlayers = data.get("numPlayers");
        // add players (to have player factory is over-engineering since we can add player name etc. in Player manager class, so no UI component needed
        for (int i =0; i<numPlayers; i++) {
            AnimalType randAnimalType = Config.ANIMAL_TYPES[RAND_ANIMAL_CHOICES.get(i)];
            Player newPlayer = new Player(i + 1, randAnimalType,textDisplayManager);
            players[i] = newPlayer;
        }
        playerTurn = 1; // always start with 1
        currPlayer = players[playerTurn-1];
    }

    public Player getCurrPlayer() {
        return currPlayer;
    }

    /**
     * Transitions player turn
     */
    public void handleTurnTransition(){
        currPlayer.setTurnEnded(false); // reset for next round
        if (playerTurn == players.length) { // reset to 1 if the 4th player ended their turn
            playerTurn = 1;
        } else {
            playerTurn += 1;
        }
        currPlayer = players[playerTurn-1];
    }

    /**
     * Checks for a win condition
     * @return boolean indicating a winner has been identified
     */
    public boolean checkWinCondition(){
        boolean winnerFound = false;
        for (Player player: players){
            if (player.getDragonToken().getTotalMovementCount()==Config.VOLCANO_RING_NUM_CARDS){
                winner=player;
                winnerFound = true;
            }
        }
        return winnerFound;
    }

    public Player getWinner() {
        return winner;
    }

    /**
     * Loads the state of itself, the players, and the dragon token.
     * While this method is bloated, it is necessary so that the logic is correct. It also prevents any errors
     * due to connascence of execution if we were to separate the load functionality into the separate classes.
     * For example, creating a player, and its dragon token, and linking them to each other
     * @param slotIndex the slot chosen for loading the game
     * @throws IOException error encountered in relation to opening a save file for reading
     */
    @Override
    public void load(int slotIndex) throws IOException{
        BufferedReader reader = Files.newBufferedReader(getSaveFilePath(slotIndex));
        String line;
        int currPlayerID = 1;
        int currPlayerTurn=1;
        int winnerID=0;
        int appCentreX = Config.SCENE_WIDTH / 2;
        int appCentreY = Config.SCENE_HEIGHT / 2;
        double tokenRadius = Config.VOLCANO_RING_RADIUS + Config.TOKEN_PATH_RADIUS_OFFSET;
        while ((line = reader.readLine()) != null) {
            if (line.equals("PlayerTurnManager")) {
                String currPlayer =reader.readLine();
                String [] parts_ = currPlayer.split(":");
                // parse the current player info
                String[] fields_ = parts_[1].split(",");
                currPlayerID = Integer.parseInt(fields_[0]);
                currPlayerTurn = Integer.parseInt(fields_[1]);
                if (!fields_[2].equals("null")){winnerID=Integer.parseInt(fields_[2]);} // parse whether a winner is present
                int playerIdx = 1;
                while (!(line = reader.readLine()).equals("*")) {
                    String[] parts = line.split(":");
                    // parse the token
                    if (parts[0].equals("Token")) {
                        String[] fields = parts[1].split(",");
                        double x = appCentreX + tokenRadius * Math.cos(Double.parseDouble(fields[0]));
                        double y = appCentreY + tokenRadius * Math.sin(Double.parseDouble(fields[0]));
                        Text playerID = new Text("" + (playerIdx) + "P");
                        playerID.setX(x - (Config.TOKEN_WIDTH / 2.0) + 30);
                        playerID.setY(y - (Config.TOKEN_HEIGHT / 2.0));
                        playerID.setFill(Config.COLORS[playerIdx-1]);
                        playerID.setFont(Font.font("Arial", 30));

                        Rectangle cardRect = new Rectangle(x - Config.TOKEN_WIDTH / 2.0, y - Config.TOKEN_HEIGHT / 2.0, Config.TOKEN_WIDTH, Config.TOKEN_HEIGHT);
                        // get image path
                        Image image = new Image(Objects.requireNonNull(getClass().getResource(DRAGON_TOKEN_IMAGE_PATHS[playerIdx-1])).toExternalForm());
                        cardRect.setFill(new ImagePattern(image)); // Provide the path to your PNG image
                        cardRect.setStroke(Color.BLACK);
                        cardRect.setStrokeWidth(1);
                        DragonToken dragonToken = new DragonToken(
                                VolcanoRingFactory.getVolcanoCardByID(Integer.parseInt(fields[1])),
                                cardRect,
                                Double.parseDouble(fields[0]),
                                playerID,
                                players[playerIdx-1]);
                        dragonToken.setCurrentPosition(VolcanoRingFactory.getVolcanoCardByID(Integer.parseInt(fields[2])));
                        dragonToken.setTotalMovementCount(Integer.parseInt(fields[4]));
                        if (fields[3].equals("true")){dragonToken.setMovedOutOfCave();}
                        players[playerIdx-1].setDragonToken(dragonToken); // set the initialised dragon token for the player
                        DragonTokenManager.addDragonToken(dragonToken);
                        playerIdx+=1;

                    }
                    // parse the individual player
                    else if (parts[0].equals("Player")){
                        String[] fields2 = parts[1].split(",");
                        Player newPlayer = new Player(playerIdx, Config.ANIMAL_TYPES[RAND_ANIMAL_CHOICES.get(playerIdx-1)], textDisplayManager);
                        newPlayer.setPoints(Integer.parseInt(fields2[2]));
                        newPlayer.setIncorrectRevealCounter(Integer.parseInt(fields2[3]));
                        newPlayer.setEqualityBoost(fields2[4].equals("true"));
                        newPlayer.setDoNothingContinueTurn(fields2[0].equals("true"));
                        newPlayer.setTurnEnded(fields2[1].equals("true"));
                        players[playerIdx-1]=newPlayer; // initialise Players
                    }
                }
                break;
            }
        }
        // initialise state based on loaded data
        currPlayer = players[currPlayerID-1];
        playerTurn = currPlayerTurn;
        if (winnerID!=0){winner=players[winnerID-1];}
    }

    /**
     * Here, we save our state into the file, and call upon the player and dragon tokens to save their states as well
     * @param writer the writer
     * @param slotIndex slot to save
     * @throws IOException error when trying to get a file path
     */
    @Override
    public void save(BufferedWriter writer, int slotIndex) throws IOException{
        // Save current state
        writer.write("PlayerTurnManager\n");
        String winnerID;
        if (winner!=null){
            winnerID = winner.getId()+"";
        }else{winnerID="null";}
        writer.write("currPlayer:" + currPlayer.getId()+ ","+playerTurn + ","+winnerID+"\n");
        // call respective components to save their state
        for (Player player:players){
            writer.write(player.loadSaveString() + "\n");
            writer.write(player.getDragonToken().loadSaveString() + "\n");
        }
        writer.write("*\n");
    }
}
