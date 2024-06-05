package com.example.demo.Model;

import com.example.demo.Animals.Animal;
import com.example.demo.Animals.AnimalType;
import com.example.demo.Controller.ChitCardAdapter;
import com.example.demo.Controller.TextDisplayManager;
import com.example.demo.EntityFactory.VolcanoRingFactory;
import javafx.scene.shape.Circle;

/**
 * Represents a player in the game, handling their ID, assigned animal type, dragon token,
 * turn status, and actions. It includes methods for managing the dragon token, determining the player's
 * turn status, and making moves based on the chosen card.
 * @author Jia Wynn Khor
 */

public class Player {
    private DragonToken dragonToken;
    private boolean doNothingContinueTurn;
    private boolean turnEnded=false;
    private final int ID;
    private TextDisplayManager textDisplayManager;
    private final AnimalType caveAnimalType;

    private int points;
    private boolean hasShield = false;

    public Player(int newID, AnimalType randAnimalType, TextDisplayManager textDisplayManager){
        ID = newID;
        caveAnimalType = randAnimalType;
        this.textDisplayManager = textDisplayManager;
        points = 0;
    }

    public boolean hasShield() {
        return hasShield;
    }

    public void setShield(boolean hasShield) {
        this.hasShield = hasShield;
    }

    public int getPoints(){
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void updatePoints(int delta) {
        this.points += delta;
    }

    public int getId() {
        return ID;
    }

    public void setDragonToken(DragonToken dragonToken) {
        this.dragonToken = dragonToken;
    }

    public void setDoNothingContinueTurn(boolean val){
        doNothingContinueTurn= val;
    }
    public boolean getTurnEnded() {
        return turnEnded;
    }

    public void setTurnEnded(boolean turnEnded) {
        this.turnEnded = turnEnded;
    }

    public boolean getDoNothingContinueTurn() {
        return doNothingContinueTurn;
    }

    public DragonToken getDragonToken(){
        return dragonToken;
    }
    /**
     * The integer return value determines the course of action:
     * END_TURN_RESULT means player's turn ends
     * Otherwise, it is the destination volcano card's ID in volcano ring (index in VolcanoCard array)
     */
    public int validateMove(Circle cardChosen, ChitCardAdapter chitCardAdapter) {
        Animal animal = chitCardAdapter.getViewControllerMapping().get(cardChosen).getAnimal();
        int animalCount = chitCardAdapter.getViewControllerMapping().get(cardChosen).getAnimal().getCount();
        return animal.calculateDestinationID(this, dragonToken, animalCount);
    }
    public void moveToken(int animalCount, int destinationID, boolean isSwap){
        this.getDragonToken().moveToken(animalCount); //VIEW: move token
        // update the volcano card's (the token is on) 'occupied' status to false if it not a swap
        if (!isSwap) {
            int currPositionRingID = this.getDragonToken().getCurrentPositionInRing();
            VolcanoRingFactory.getVolcanoCardByID(currPositionRingID).setOccupied(false);
        }

        // update the state: dragon token's position and total movement count
        VolcanoRingFactory.getVolcanoCardByID(destinationID).setOccupied(true);
        this.getDragonToken().setCurrentPosition(VolcanoRingFactory.getVolcanoCardByID(destinationID));
        // since we updated movement count, message needs to updated as well
        this.getDragonToken().updateTotalMovementCount(animalCount);
        textDisplayManager.removeOldMovementCountMsg(textDisplayManager.getCurrentMovementCountEntity());
        textDisplayManager.handleMovementCountUpdate(this.getId(),this.getDragonToken().getTotalMovementCount());

        //update the point system display
        textDisplayManager.displayPlayerPointsMsg1(this.getId(),this.getDragonToken().getTotalPointsCount());

        // update movedOutOfCave to True if exiting cave for the first time
        if (!this.getDragonToken().getMovedOutOfCave()) {
            this.getDragonToken().setMovedOutOfCave();
        }

        int pointsAwarded = allocatePoints(animalCount, destinationID, isSwap);
        updatePoints(pointsAwarded);

        textDisplayManager.updatePointsDisplay(this.getId(), this.getPoints());

        this.setDoNothingContinueTurn(true); // since we already did the animation, continue onto next flip

    }


    private int allocatePoints(int animalCount, int destinationID, boolean isSwap) {
        return animalCount + 1;
    }


    public AnimalType getCaveAnimalType() {
        return caveAnimalType;
    }

    public String loadSaveString(){
        return "Player:"+doNothingContinueTurn+","+turnEnded;
    }
}
