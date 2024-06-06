package com.example.demo.Animals;

import com.example.demo.Config;
import com.example.demo.Controller.ChitCardAdapter;
import com.example.demo.Controller.TextDisplayManager;
import com.example.demo.EntityFactory.VolcanoRingFactory;
import com.example.demo.Model.DragonToken;
import com.example.demo.Model.Player;
import javafx.scene.control.Alert;

import java.security.PrivateKey;

/**
 * Dragon class
 * @author Maliha Tariq
 */

public class DragonPirate extends Animal{

    private ChitCardAdapter chitCardAdapter;

    public DragonPirate(int count) {
        super(Config.ANIMAL_IMAGE_IMAGE_PATH_PREFIX_MAPPINGS.get(AnimalType.DRAGON_PIRATE), count, AnimalType.DRAGON_PIRATE);
    }

    @Override
    public int calculateDestinationID(Player player, DragonToken dragonToken, int animalCount)
    {
        // if player has not moved out of cave, do not allow going back further than cave
        int destinationRingID = dragonToken.getCurrentPositionInRing()-animalCount; // move BACKWARDS
        if ((destinationRingID < dragonToken.getInitialVolcanoCardID()) && !dragonToken.getMovedOutOfCave()) {
            player.setDoNothingContinueTurn(true);
            return dragonToken.getCurrentPositionInRing();
        }
        if (destinationRingID < 1){destinationRingID = Config.VOLCANO_RING_NUM_CARDS+destinationRingID;}

        // no matter what, do not allow players to share same volcano card.
        boolean occupied = VolcanoRingFactory.getVolcanoCardByID(destinationRingID).getOccupiedStatus();
        if (occupied) {
            return Config.END_TURN_RESULT;
        }
        return destinationRingID;
    }

    public void handleSkullCardFlip(Player player) {
        if (player.hasShield()) {
            player.setShield(false); // Consume the shield
            player.getDragonToken().moveToken(chitCardAdapter.getViewControllerMapping().get().getAnimal().getCount());
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Shield Used");
        alert.setHeaderText(null);
        alert.setContentText("You have successfully used Shield");
        alert.showAndWait();
    }

    @Override
    public int getCount() {
        return -super.getCount();
    }

    @Override
    protected String getSuffixedImgPath() {
        return baseImgPath + "" + Math.abs(count) + ".png";
    }
}
