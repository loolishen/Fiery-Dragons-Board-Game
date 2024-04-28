package com.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class DragonToken {
    private static final double DEGREES_TO_RADIANS = Math.PI/180;
    private int totalMovementCount;
    private final Rectangle tokenImage;
    private final int initialVolcanoCardID;
    private VolcanoCard volcanoCard; // rationale: better than having reference to entire Volcano?
    private final double tokenRadius;
    private boolean movedOutOfCave;

    private double currPosAngle; // its current position on the volcano ring measured by angle

    public DragonToken(VolcanoCard newInitialVolcanoCard, Rectangle newTokenImage, double newTokenRadius, double initialPosAngle){
        volcanoCard = newInitialVolcanoCard;
        initialVolcanoCardID = newInitialVolcanoCard.getRingID();
        movedOutOfCave = false; // by default has not moved out of cave
        tokenImage = newTokenImage;
        tokenRadius = newTokenRadius;
        currPosAngle = initialPosAngle;
    }

    public double getCurrPosAngle() {
        return currPosAngle;
    }

    public int getInitialVolcanoCardID() {
        return initialVolcanoCardID;
    }

    public void moveToken(int animalCount){

        double stopPosAngle = currPosAngle + ((Constants.TOKEN_MOVE_ANGLE_INCREMENT * animalCount) * DEGREES_TO_RADIANS);

        // Calculate the start and end points
        double endX = FXGL.getAppWidth() / 2.0 + (tokenRadius * Math.cos(stopPosAngle)-10);
        double endY = FXGL.getAppHeight() / 2.0 + (tokenRadius * Math.sin(stopPosAngle)-10);


        // Create a timeline for smooth movement
        Timeline timeline = new Timeline();
        KeyValue keyValueX = new KeyValue(tokenImage.xProperty(), endX);
        KeyValue keyValueY = new KeyValue(tokenImage.yProperty(), endY);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), keyValueX, keyValueY);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
        currPosAngle = stopPosAngle;
    }

    public void updateTotalMovementCount(int steps){
        totalMovementCount += steps;
    }

    public int getTotalMovementCount() {
        return totalMovementCount;
    }

    public int getCurrentPosition() {
        return volcanoCard.getRingID();
    }

    public void setCurrentPosition(VolcanoCard newVolcanoCard){
        volcanoCard = newVolcanoCard;
    }

    public boolean getMovedOutOfCave(){
        return movedOutOfCave;
    }

    public void setMovedOutOfCave(){
        movedOutOfCave = true;
    }

    public AnimalType getAnimalType(){
        return volcanoCard.getAnimal();
    }
}
