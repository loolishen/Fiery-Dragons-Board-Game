package com.example.demo.Model;

import com.almasb.fxgl.dsl.FXGL;
import com.example.demo.Animals.AnimalType;
import com.example.demo.Config;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * The DragonToken class represents a dragon token in the game, managing its movement around the volcano ring,
 * position, and associated animations.
 * @author Jia Wynn Khor
 */

public class DragonToken {
    private static final double DEGREES_TO_RADIANS = Math.PI/180;
    private int totalMovementCount;
    private final Rectangle tokenImage;
    private final int initialVolcanoCardID;
    private VolcanoCard currentVolcanoCard;
    private final double tokenRadius;
    private boolean movedOutOfCave;
    private final Text playerLabel;

    private final Player player;


    private double currPosAngle; // its current position on the volcano ring measured by angle

    public DragonToken(VolcanoCard newInitialVolcanoCard, Rectangle newTokenImage, double initialPosAngle, Text newPlayerLabel, Player newPlayer){
        currentVolcanoCard = newInitialVolcanoCard;
        initialVolcanoCardID = newInitialVolcanoCard.getRingID();
        tokenImage = newTokenImage;
        tokenRadius = Config.DRAGON_TOKEN_RADIUS;
        currPosAngle = initialPosAngle;
        playerLabel = newPlayerLabel;
        player = newPlayer;

    }

    public Rectangle getTokenImage() {
        return tokenImage;
    }

    public Text getPlayerLabel() {
        return playerLabel;
    }

    public int getInitialVolcanoCardID() {
        return initialVolcanoCardID;
    }

    public void moveToken(int animalCount){
        System.out.println(getAnimalType() + " moves by " + animalCount);
        double stopPosAngle = currPosAngle + ((Config.TOKEN_MOVE_ANGLE_INCREMENT * animalCount) * DEGREES_TO_RADIANS);

        // Calculate the start and end points
        double endX = FXGL.getAppWidth() / 2.0 + (tokenRadius * Math.cos(stopPosAngle)-10);
        double endY = FXGL.getAppHeight() / 2.0 + (tokenRadius * Math.sin(stopPosAngle)-10);

        double endXLabel = endX + 30;

        // Create a timeline for smooth movement
        Timeline timeline = new Timeline();
        KeyValue keyValueX = new KeyValue(tokenImage.xProperty(), endX);
        KeyValue labelKeyValueX = new KeyValue(playerLabel.xProperty(), endXLabel);
        KeyValue keyValueY = new KeyValue(tokenImage.yProperty(), endY);
        KeyValue labelKeyValueY = new KeyValue(playerLabel.yProperty(), endY);
        KeyFrame keyFrameLabel = new KeyFrame(Duration.seconds(1), labelKeyValueX, labelKeyValueY);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), keyValueX, keyValueY);
        timeline.getKeyFrames().add(keyFrameLabel);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
        currPosAngle = stopPosAngle;
    }

    /**
     * During leprechaun's swaps, if the dragon token moves past its cave, it has to go around the volcano again
     * Additionally, it would not be fair if a dragon token keeps going backward beyond its cave, so
     * if a dragon token moves beyond its cave by more than one cycle, we reset it
     * For example, if the total movement count becomes -25, we reset it to -1.
     * @param steps steps taken
     */
    public void updateTotalMovementCount(int steps){
        totalMovementCount += steps;
        if (totalMovementCount > Config.VOLCANO_RING_NUM_CARDS){ // reset if we swapped past cave
            totalMovementCount-=Config.VOLCANO_RING_NUM_CARDS;
        } else if (totalMovementCount < -Config.VOLCANO_RING_NUM_CARDS){ // prevent unbounded negative cycles
            totalMovementCount += Config.VOLCANO_RING_NUM_CARDS;
        }
    }

    public void setTotalMovementCount(int totalMovementCount) {
        this.totalMovementCount = totalMovementCount;
    }

    public int getTotalMovementCount() {
        return totalMovementCount;
    }

    public int getCurrentPositionInRing() {
        return currentVolcanoCard.getRingID();
    }

    public void setCurrentPosition(VolcanoCard newVolcanoCard){
        currentVolcanoCard = newVolcanoCard;
    }

    public boolean getMovedOutOfCave(){
        return movedOutOfCave;
    }

    public void setMovedOutOfCave(){
        movedOutOfCave = true;
    }

    public AnimalType getAnimalType(){
        return currentVolcanoCard.getAnimal();
    }
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns its save information for the use of the PlayerTurnManger
     */
    public String loadSaveString(){
        return "Token:"+currPosAngle+","+initialVolcanoCardID+","+getCurrentPositionInRing()+","+movedOutOfCave+","+getTotalMovementCount();
    }
}
