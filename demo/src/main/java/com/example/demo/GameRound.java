package com.example.demo;

public class GameRound {

    private final Volcano volcanoRing;
    private final Player[] players; // TODO: clockwise direction,and youngest player starts first (first element is first player, others randomised
    private ChitCardFactory chitCardFactory;
    private boolean endGame;

    public GameRound(Volcano newVolcanoRing, Player[] newPlayers, ChitCardFactory newChitCardFactory){
        volcanoRing = newVolcanoRing;
        players = newPlayers;
        chitCardFactory = newChitCardFactory;
    }

    public Volcano getVolcanoRing() {
        return volcanoRing;
    }

    /**
     * Display winning message.
     */
    private void displayMessage(){

    }

    private void gameLoop(){
        int playerTurn = 1;
        Player currPlayer = players[playerTurn-1];
        while (!endGame){
            while (!currPlayer.getTurnEnded()) {
                playTurn(currPlayer); // this method will modify the player's turnEnded attribute
            }
            if (playerTurn == players.length){playerTurn = 1;}else{playerTurn+=1;} // reset to 1 if the 4th player ended their turn
            // turn ended so go to next player
            currPlayer = players[playerTurn-1];
        }
        displayMessage();
        // TODO: go back to home screen (on clicking a return button?) or perhaps just refresh board?
    }

    private void playTurn(Player player){

        // TODO: get user input (mouse click listener?)


        ChitCard cardChosen = new ChitCard(AnimalType.SALAMANDER, 2); // dummy
        int result = player.makeMove(this,cardChosen); // result = 0 means end turn, otherwise it is destination ring ID that token should move to
        if  (result != 0) {

            // update the volcano card's (the token is on) 'occupied' status to false
            int currPositionRingID = player.getDragonToken().getCurrentPosition();
            volcanoRing.getVolcanoCardByID(currPositionRingID).flipOccupiedStatus();


            // TODO: move token to volcano card at destination ring ID

            // update the state: dragon token's position and total movement count
            player.getDragonToken().setCurrentPosition(volcanoRing.getVolcanoCardByID(result));
            player.getDragonToken().updateTotalMovementCount((cardChosen.getAnimalCount()));

            // update movedOutOfCave to True if exiting cave for the first time
            if (!player.getDragonToken().getMovedOutOfCave()){player.getDragonToken().setMovedOutOfCave();}


        } else {player.setTurnEnded(true);}

        if (result == volcanoRing.getTotalCards()){endGame = true;} // end the game


    }
}
