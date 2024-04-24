package com.example.demo;

public class Player {

    private DragonToken dragonToken;
    private boolean turnEnded;
    public Player(DragonToken token){
        dragonToken = token;
    }

    public boolean getTurnEnded() {
        return turnEnded;
    }

    public void setTurnEnded(boolean turnEnded) {
        this.turnEnded = turnEnded;
    }

    public DragonToken getDragonToken(){
        return dragonToken;
    }
    /**
     * The integer return value determines the course of action:
     * 0 means player's turn ends
     * 1 means player can make a choice again
     */
    public int makeMove(Volcano volcano, ChitCard cardChosen) {

        int destinationRingID = 0;
        if (cardChosen == null){
            return 0;
        }
        AnimalType currentPositionAnimal = dragonToken.getAnimalType();
        AnimalType chitCardAnimal = cardChosen.getAnimalType();
        if (currentPositionAnimal == chitCardAnimal)
        {
            int ringID = dragonToken.getCurrentPosition();
            int chitCardAnimalCount = cardChosen.getAnimalCount();

            // if the chitCardAnimalCount is negative(dragon pirate), do not move if player is in cave
            if ((ringID == 1) && (chitCardAnimalCount < 0) && !dragonToken.getMovedOutOfCave()) {
                return 0;
            }

            destinationRingID = ringID + chitCardAnimalCount; // TODO: write formula which takes into account exceeding maximum of 16

            // cannot go past cave once a full round is made around the volcano
            if (destinationRingID > volcano.getTotalCards()) {
                return 0;
            }
            // if player has moved out of cave, do not allow going back further than cave
            if ((destinationRingID < 1) && dragonToken.getMovedOutOfCave()) {
                destinationRingID = 1;
            }

            boolean occupied = volcano.getVolcanoCardByID(destinationRingID).getOccupiedStatus();

            if (occupied) { // no matter what, do not allow players to share same volcano card
                return 0;
            }
        }
        else {return 0;}

        return destinationRingID; // all checks passed, player can move

    }
}
