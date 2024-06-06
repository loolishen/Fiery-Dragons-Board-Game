package com.example.demo.Controller;

import com.example.demo.Config;
import com.example.demo.Model.DragonToken;

/**
 * Helps Leprechaun dragon chit card to find the closest dragon token to the current player's dragon token by managing
 * the list of dragon tokens.
 */
public class DragonTokenManager {

    private static int closestTokenDiff = 0;
    private static final DragonToken[] dragonTokens = new DragonToken[Config.NUM_PLAYERS];
    private static int counter=0;

    public static void addDragonToken(DragonToken dragonToken){
        dragonTokens[counter] = dragonToken;
        counter+=1;
        if (counter == Config.NUM_PLAYERS) {
            counter = 0;
        }
    }

    /**
     * Finds the dragon token that is closest to the dragonToken of interest, and can be swapped with. May be null
     * @param dragonToken the dragon token of interest
     * @return closest dragonToken
     */
    public static DragonToken findClosestDragonToken(DragonToken dragonToken){
        DragonToken tokenToSwapWith=null;
        double currDifference = Double.POSITIVE_INFINITY;
        int tokenDiff = 0;
        for (DragonToken token:dragonTokens){
            // compare with the tokens on the board excluding itself
            if (token!=dragonToken && token.getMovedOutOfCave()){ // token in cave cannot be swapped
                int toSwapPos = token.getCurrentPositionInRing();
                // normalising pos in ring
                if ((dragonToken.getCurrentPositionInRing() > 20 && dragonToken.getCurrentPositionInRing() <= 24)
                        && (token.getCurrentPositionInRing() >= 1 && token.getCurrentPositionInRing() <= 7)){
                    toSwapPos = token.getCurrentPositionInRing()+24;
                }
                // only get the shortest distance
                int firstTokenDiff = Math.abs(toSwapPos-dragonToken.getCurrentPositionInRing());
                int secondTokenDiff = Math.abs(dragonToken.getCurrentPositionInRing()-toSwapPos);
                tokenDiff = Math.min(firstTokenDiff, secondTokenDiff);
                if (tokenDiff < currDifference){
                    currDifference = tokenDiff;
                    tokenToSwapWith =token;
                }
            }
        }
        closestTokenDiff = (int)currDifference;
        return tokenToSwapWith;
    }

    /**
     * Connascence of execution, can only be called after findClosestDragonToken() has been called
     * @return the distance between the active dragon token and the closest token to it
     */
    public static int getClosestDragonTokenDistance(){
        return closestTokenDiff;
    }
}
