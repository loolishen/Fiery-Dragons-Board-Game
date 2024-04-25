package com.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import javafx.animation.AnimationTimer;
import javafx.scene.shape.Rectangle;


public class DragonToken {

    private static final int MOVE_ANGLE = 15;

    private static final double ANGLE_INCREMENT = 0.01;
    private static final double DEGREES_TO_RADIANS = Math.PI/180;

    private int totalMovementCount;

    private Rectangle tokenImage;

    private final VolcanoCard initialVolcanoCard;
    private VolcanoCard volcanoCard; // rationale: better than having reference to entire Volcano?

    private double tokenRadius;

    private boolean movedOutOfCave;

    private double currPosAngle;

    public DragonToken(VolcanoCard newInitialVolcanoCard, Rectangle newTokenImage, double newTokenRadius, double initialPosAngle){
        volcanoCard = newInitialVolcanoCard;
        initialVolcanoCard = newInitialVolcanoCard;
        movedOutOfCave = false; // by default has not moved out of cave
        tokenImage = newTokenImage;
        tokenRadius = newTokenRadius;
        currPosAngle = initialPosAngle;
    }

    public double getCurrPosAngle() {
        return currPosAngle;
    }

    public VolcanoCard getInitialVolcanoCard() {
        return initialVolcanoCard;
    }

    public void moveToken(int animalCount){
        double stopPosAngle = currPosAngle + ((MOVE_ANGLE*animalCount)*DEGREES_TO_RADIANS);
        System.out.println("The stop pos angle is " + stopPosAngle);

        AnimationTimer timer = new AnimationTimer() {
            double timerCurrPosAngleRadians = currPosAngle;
            @Override
            public void handle(long l) {
                timerCurrPosAngleRadians += ANGLE_INCREMENT;

                // new position of token
                double x  = FXGL.getAppWidth() / 2.0 + (tokenRadius*Math.cos(currPosAngle));
                double y = FXGL.getAppHeight()/2.0 + (tokenRadius*Math.sin(currPosAngle));

                tokenImage.setX(x);
                tokenImage.setY(y);

                if (timerCurrPosAngleRadians >= stopPosAngle){
                    stop();
                }

            }
        };
        timer.start();
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
