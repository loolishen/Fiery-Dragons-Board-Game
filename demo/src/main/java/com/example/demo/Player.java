package com.example.demo;

public class Player {
    private static final int TOTAL_CARDS = 24;
    private DragonToken dragonToken;
    private boolean doNothingContinueTurn;
    private boolean turnEnded;
    private int id;
    public Player(int ID){
        id = ID;
    }

    public int getId() {
        return id;
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
     * 0 means player's turn ends
     * 1 means player can make a choice again
     */
    public int makeMove(Volcano volcano, ChitCard cardChosen) {

        int destinationRingID = 0;
        if (cardChosen == null){
            System.out.println("For some reason chit card chosen is null");
            return 0;
        }
        AnimalType currentPositionAnimal = dragonToken.getAnimalType();
        AnimalType chitCardAnimal = cardChosen.getAnimalType();
        if (currentPositionAnimal == chitCardAnimal)
        {
            int ringID = dragonToken.getCurrentPosition();
            int chitCardAnimalCount = cardChosen.getAnimalCount();


            destinationRingID = ringID + chitCardAnimalCount;

            // cannot go past cave once a full round is made around the volcano
            if (dragonToken.getTotalMovementCount()+chitCardAnimalCount > volcano.getTotalCards()) {
                System.out.println("Cannot move ahead of own cave");
                return 0;
            }


        }else if (chitCardAnimal == AnimalType.DRAGON_PIRATE){
            // if player has not moved out of cave, do not allow going back further than cave
            destinationRingID = dragonToken.getCurrentPosition()+cardChosen.getAnimalCount();
            System.out.println("Current pos is "+ dragonToken.getCurrentPosition() + " and the potential destination is "+ destinationRingID);
            System.out.println("initial position is "+dragonToken.getInitialVolcanoCard().getRingID());
            System.out.println("The token's initial cave is at " + dragonToken.getInitialVolcanoCard().getRingID());
            System.out.println("Token has moved out of cave "+ dragonToken.getMovedOutOfCave());
           if ((destinationRingID < dragonToken.getInitialVolcanoCard().getRingID()) && !dragonToken.getMovedOutOfCave()) {
               System.out.println("Cannot go back further than own cave since has not moved out of cave");
                // TODO modify logic to ensure that when line 232 in FieryApplication is executed, even if the animal count is 2 for
               // dragon pirate, we do NOT move by 2, because we only stay stationary at the cave. We CANNOT return 0 because this means we won't
               // shift the token for cases when we need to shift token to intitial card, e.g. we are at 2, but we have 2 dragon pirate meaning we
               // need to move back by 2 places, but it is limited by the initial card pos of 1. So we only shift by -shiftAmount+(initialCardPos-(destinationRingID)) = -2+(0-(-1))=-1
               // another example is if initial card pos is 7, destination ring is 6 and our shift amount is -2 because our curr pos is 8, then we have -2+(7-(6)) = -1
                if ((dragonToken.getCurrentPosition()-destinationRingID+cardChosen.getAnimalCount() == 0)){
                    this.setDoNothingContinueTurn(true);
                    return dragonToken.getCurrentPosition();
                }
                else {
                    return cardChosen.getAnimalCount() + (dragonToken.getCurrentPosition() - destinationRingID);
                }
           } else if ((destinationRingID < dragonToken.getInitialVolcanoCard().getRingID()) && dragonToken.getMovedOutOfCave()){
               System.out.println("Proceed as usual to the destination since has already moved out of cave");
           }


        }
        else {
            System.out.println("Mismatch, so turn ends");
            return 0;}
        if( destinationRingID >= TOTAL_CARDS+1){
            System.out.println("Updating destination ringID");
            destinationRingID -= TOTAL_CARDS;
        } else if (destinationRingID < 1){destinationRingID = TOTAL_CARDS+destinationRingID;}
        // no matter what, do not allow players to share same volcano card.
        boolean occupied = volcano.getVolcanoCardByID(destinationRingID).getOccupiedStatus();
        if (occupied) {
            System.out.println("Cannot share card with existing player since "+ destinationRingID + " " + volcano.getVolcanoCardByID(destinationRingID).getAnimal() + " is occupied");
            return 0;
        }

        return destinationRingID; // all checks passed, player can move

    }
}
