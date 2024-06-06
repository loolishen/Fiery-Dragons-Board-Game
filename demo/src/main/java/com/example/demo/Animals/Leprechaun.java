package com.example.demo.Animals;

import com.example.demo.Config;
import com.example.demo.Controller.DragonTokenManager;
import com.example.demo.Model.DragonToken;
import com.example.demo.Model.Player;

public class Leprechaun extends Animal{
    public Leprechaun(int count) {
        super(Config.ANIMAL_IMAGE_IMAGE_PATH_PREFIX_MAPPINGS.get(AnimalType.LEPRECHAUN), count, AnimalType.LEPRECHAUN);
    }

    @Override
    public int calculateDestinationID(Player player, DragonToken dragonToken, int animalCount) {
        System.out.println("Dragon token's movement count is "+ dragonToken.getTotalMovementCount());
        DragonToken dragonTokenToSwap = DragonTokenManager.findClosestDragonToken(dragonToken);
        if (dragonTokenToSwap!=null) {
            System.out.println("Token to swap's movement count is " + dragonTokenToSwap.getTotalMovementCount());
        }
        int closestTokenDiff = DragonTokenManager.getClosestDragonTokenDistance();
        System.out.println("Distance chosen is " + closestTokenDiff);
        int dragonTokenCurrPos = dragonToken.getCurrentPositionInRing();
        // calculate destination ID
        if (dragonTokenToSwap != null){
            int destinationID = dragonTokenToSwap.getCurrentPositionInRing();

            boolean inBetween =false;
            // check if cave in between
            int dragonTokenCavePos = dragonToken.getInitialVolcanoCardID();
            boolean modifiedDestinationID = false;
            if (dragonTokenCurrPos<=Config.VOLCANO_RING_NUM_CARDS && dragonTokenCurrPos > 13 && destinationID < 7 && destinationID > 1
                    && !(dragonTokenCavePos<dragonTokenCurrPos && dragonTokenCavePos>destinationID)){
                if (dragonTokenCavePos < 14){dragonTokenCavePos+=24;}
                destinationID += Config.VOLCANO_RING_NUM_CARDS;
                modifiedDestinationID = true;
                if (dragonTokenCavePos > dragonTokenCurrPos && dragonTokenCavePos < destinationID ){
                    inBetween = true;
                }
            } else {
                inBetween = (dragonTokenCavePos < destinationID && dragonTokenCavePos > dragonTokenCurrPos);
            }
            int counterPartMoveAmount;
            System.out.println("Destination is "+ destinationID + " and currently on "+ dragonTokenCurrPos + " in btwn " + inBetween);
            int moveAmount;
            if (inBetween){  // if cave is in between, check if totalMovementCount is positive
                if (dragonToken.getTotalMovementCount() > 0){
                    moveAmount = -(Config.VOLCANO_RING_NUM_CARDS-closestTokenDiff); // if positive, then move backwards by 24-closestTokenDiff
                    counterPartMoveAmount = -closestTokenDiff;
                } else {
                    moveAmount = closestTokenDiff;
                    counterPartMoveAmount = -closestTokenDiff;
                } // else: move forwards by closestTokenDiff
            } else {
                // normalising pos in ring
                if ((dragonToken.getCurrentPositionInRing() > 20 && dragonToken.getCurrentPositionInRing() <= 24)
                        && (dragonTokenToSwap.getCurrentPositionInRing() >= 1 && dragonTokenToSwap.getCurrentPositionInRing() <= 7)){
                    moveAmount = closestTokenDiff;
                    counterPartMoveAmount = Config.VOLCANO_RING_NUM_CARDS-closestTokenDiff;
                }
                else if (dragonTokenCurrPos > destinationID) {
                    moveAmount = Config.VOLCANO_RING_NUM_CARDS - closestTokenDiff;
                    counterPartMoveAmount = closestTokenDiff;
                } else {
                    moveAmount = closestTokenDiff;
                    counterPartMoveAmount = -moveAmount;
                }
            }

            if (modifiedDestinationID){destinationID-=Config.VOLCANO_RING_NUM_CARDS;}
            player.moveToken(moveAmount, destinationID, true);

            // other dragonToken must also move, and no validation is needed
            dragonTokenToSwap.getPlayer().moveToken(
                    counterPartMoveAmount,
                    dragonTokenCurrPos,
                    true);
        }
        // reset
        player.setDoNothingContinueTurn(false);

        return Config.END_TURN_RESULT;
    }
}
