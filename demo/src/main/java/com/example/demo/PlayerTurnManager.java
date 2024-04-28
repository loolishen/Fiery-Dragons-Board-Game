package com.example.demo;


public class PlayerTurnManager {

    private static PlayerTurnManager playerTurnManager;
    private static Player[] players; // TODO: add indicators to who is player 1,2,3,4 in UI
    private Player currPlayer;
    private int playerTurn;

    private PlayerTurnManager(){}
    public static PlayerTurnManager getInstance(){
        if (playerTurnManager == null){
            playerTurnManager = new PlayerTurnManager();
        }
        return playerTurnManager;
    }


    /**
     * Connascenece of execution: can only be called after getInstance() is called
     * @return the players
     */
    public static Player[] getPlayers() {
        return players;
    }

    public void createPlayers(int numPlayers){
        players = new Player[numPlayers];
        // add players (to have player factory is over-engineering since we can add player name etc. in Player manager class, so no UI component needed
        for (int i =0; i<numPlayers; i++) {
            Player newPlayer = new Player(i + 1);
            players[i] = newPlayer;
        }
        playerTurn = 1; // always start with 1
        currPlayer = players[playerTurn-1];
    }

    public Player getCurrPlayer() {
        return currPlayer;
    }

    public void handleTurnTransition(){
        currPlayer.setTurnEnded(false); // reset for next round
        if (playerTurn == players.length) { // reset to 1 if the 4th player ended their turn
            playerTurn = 1;
        } else {
            playerTurn += 1;
        }
        currPlayer = players[playerTurn-1];
    }

    public void moveToken(Player player, ChitCard cardChosen, int destinationID){
        player.getDragonToken().moveToken(cardChosen.getAnimalCount()); //VIEW: move token
        // update the volcano card's (the token is on) 'occupied' status to false
        int currPositionRingID = player.getDragonToken().getCurrentPosition();
        VolcanoRingFactory.volcanoRing.getVolcanoCardByID(currPositionRingID).setOccupied(false);

        // update the state: dragon token's position and total movement count
        VolcanoRingFactory.volcanoRing.getVolcanoCardByID(destinationID).setOccupied(true);
        player.getDragonToken().setCurrentPosition(VolcanoRingFactory.volcanoRing.getVolcanoCardByID(destinationID));
        player.getDragonToken().updateTotalMovementCount((cardChosen.getAnimalCount()));

        // update movedOutOfCave to True if exiting cave for the first time
        if (!player.getDragonToken().getMovedOutOfCave()) {
            player.getDragonToken().setMovedOutOfCave();
        }
        player.setDoNothingContinueTurn(true); // since we already did the animation, continue onto next flip
    }

}
