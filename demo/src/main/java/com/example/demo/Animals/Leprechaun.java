package com.example.demo.Animals;

import com.example.demo.Config;
import com.example.demo.Controller.DragonTokenManager;
import com.example.demo.Model.DragonToken;
import com.example.demo.Model.Player;

/**
 * New class representing animal for dragon chit card 2. C
 */
public class Leprechaun extends Animal{
    public Leprechaun(int count) {
        super(Config.ANIMAL_IMAGE_IMAGE_PATH_PREFIX_MAPPINGS.get(AnimalType.LEPRECHAUN), count, AnimalType.LEPRECHAUN);
    }

    /**
     * Overrides the default implementation of Animal class
     * Custom implementation to swap the tokens involved. Calls on the DragonTokenManager to help determine the closest
     * dragon token
     * @param player player who flipped leprechaun chit card
     * @param dragonToken the dragon token of the player
     * @param animalCount even though the count is 1 for leprechauns, it is not needed
     * @return 0 to signify the end of turn as part of the rules
     */
    @Override
    public int calculateDestinationID(Player player, DragonToken dragonToken, int animalCount) {
        DragonToken dragonTokenToSwap = DragonTokenManager.findClosestDragonToken(dragonToken);
        int closestTokenDiff = DragonTokenManager.getClosestDragonTokenDistance();
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
                // normalising the position of the destination for the purposes of determining whether cave is in between
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
                // special case to handle because our indexing goes from 1 to 24
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
            // getting back the original destination id if we modified previously
            if (modifiedDestinationID){destinationID-=Config.VOLCANO_RING_NUM_CARDS;}

            // below two statements perform the swap, with isSwap set to true so that no validation is required
            player.moveToken(moveAmount, destinationID, true);
            dragonTokenToSwap.getPlayer().moveToken(
                    counterPartMoveAmount,
                    dragonTokenCurrPos,
                    true);
        }
        // as usual, since this is not a dragon pirate card, reset it to false
        player.setDoNothingContinueTurn(false);

        return Config.END_TURN_RESULT; // rules say player's turn ends
    }
}
