package com.example.demo.Model;

import com.example.demo.Animals.AnimalType;
import com.example.demo.Config;
import com.example.demo.Controller.ChitCardAdapter;
import com.example.demo.EntityFactory.VolcanoRingFactory;
import javafx.scene.shape.Circle;

public class Player {
    private DragonToken dragonToken;
    private boolean doNothingContinueTurn;
    private boolean turnEnded=false;
    private final int ID;
    private final AnimalType animalType;
    public Player(int newID, AnimalType randAnimalType){
        ID = newID;
        animalType = randAnimalType;
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
    public int makeMove(Circle cardChosen) {

        int destinationRingID; // hypothetical destination id in the volcano ring if player successfully makes a move
        AnimalType animalUsedForChecking = dragonToken.getMovedOutOfCave()? dragonToken.getAnimalType():animalType;
        AnimalType chitCardAnimal = ChitCardAdapter.getViewControllerMapping().get(cardChosen).getAnimalType(); // animal on chit card
        int ringID = dragonToken.getCurrentPositionInRing(); // current position in ring

        if (animalUsedForChecking == chitCardAnimal)
        {

            int chitCardAnimalCount = ChitCardAdapter.getViewControllerMapping().get(cardChosen).getAnimalCount();

            destinationRingID = ringID + chitCardAnimalCount;

            // cannot go past cave once a full round is made around the volcano
            if (dragonToken.getTotalMovementCount()+chitCardAnimalCount > Config.VOLCANO_RING_NUM_CARDS) {
                return Config.END_TURN_RESULT;
            }

        }else if (chitCardAnimal == AnimalType.DRAGON_PIRATE) {
            // if player has not moved out of cave, do not allow going back further than cave
            destinationRingID = dragonToken.getCurrentPositionInRing() + ChitCardAdapter.getViewControllerMapping().get(cardChosen).getAnimalCount();
            if ((destinationRingID < dragonToken.getInitialVolcanoCardID()) && !dragonToken.getMovedOutOfCave()) {
                this.setDoNothingContinueTurn(true);
                return dragonToken.getCurrentPositionInRing();
            }
        }
        else {
            return Config.END_TURN_RESULT;
        }
        // adjust destination ring id to always be within the range 1,24
        if( destinationRingID > Config.VOLCANO_RING_NUM_CARDS){
            destinationRingID -= Config.VOLCANO_RING_NUM_CARDS;
        } else if (destinationRingID < 1){destinationRingID = Config.VOLCANO_RING_NUM_CARDS+destinationRingID;}

        // no matter what, do not allow players to share same volcano card.
        boolean occupied = VolcanoRingFactory.getVolcanoCardByID(destinationRingID).getOccupiedStatus();
        if (occupied) {
            return Config.END_TURN_RESULT;
        }
        return destinationRingID; // all checks passed, player can move

    }

    public AnimalType getAnimalType() {
        return animalType;
    }
}
