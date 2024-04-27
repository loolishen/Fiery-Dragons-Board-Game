package com.example.demo;

public class Player {
    private DragonToken dragonToken;
    private boolean doNothingContinueTurn;
    private boolean turnEnded;
    private final int ID;
    public Player(int newID){
        ID = newID;
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
     * 1 means player can make a choice again
     */
    public int makeMove(Volcano volcano, ChitCard cardChosen) {

        int destinationRingID;

        AnimalType currentPositionAnimal = dragonToken.getAnimalType();
        AnimalType chitCardAnimal = cardChosen.getAnimalType();
        if (currentPositionAnimal == chitCardAnimal)
        {
            int ringID = dragonToken.getCurrentPosition();
            int chitCardAnimalCount = cardChosen.getAnimalCount();

            destinationRingID = ringID + chitCardAnimalCount;

            // cannot go past cave once a full round is made around the volcano
            if (dragonToken.getTotalMovementCount()+chitCardAnimalCount > Constants.VOLCANO_RING_NUM_CARDS) {
                System.out.println("Cannot move ahead of own cave");
                return Constants.END_TURN_RESULT;
            }

        }else if (chitCardAnimal == AnimalType.DRAGON_PIRATE) {
            // if player has not moved out of cave, do not allow going back further than cave
            destinationRingID = dragonToken.getCurrentPosition() + cardChosen.getAnimalCount();
            if ((destinationRingID < dragonToken.getInitialVolcanoCardID()) && !dragonToken.getMovedOutOfCave()) {
                this.setDoNothingContinueTurn(true);
                return dragonToken.getCurrentPosition();
            }
        }
        else {
            return Constants.END_TURN_RESULT;
        }
        if( destinationRingID >= Constants.VOLCANO_RING_NUM_CARDS+1){
            destinationRingID -= Constants.VOLCANO_RING_NUM_CARDS;
        } else if (destinationRingID < 1){destinationRingID = Constants.VOLCANO_RING_NUM_CARDS+destinationRingID;}
        // no matter what, do not allow players to share same volcano card.
        boolean occupied = volcano.getVolcanoCardByID(destinationRingID).getOccupiedStatus();
        if (occupied) {
            return Constants.END_TURN_RESULT;
        }
        return destinationRingID; // all checks passed, player can move

    }
}
