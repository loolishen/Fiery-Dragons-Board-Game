package com.example.demo.Controller;

import com.example.demo.Model.ChitCard;
import javafx.animation.RotateTransition;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Used to respond to players flipping chit cards.
 * @author Liang Dizhen
 */

public class ChitCardFlipManager{
    private static ChitCardFlipManager chitCardFlipManager;
    private Circle[] coveredChitCardShapes; // used to ensure that they do not listen for clicks
    private boolean animationInProgress = false; // used to ensure smoother animations
    private ArrayList<Circle> uncoveredChitCardsByPlayer = new ArrayList<>(); // specific uncovered chit cards in a player's turn

    private ChitCardFlipManager(){}
    public static ChitCardFlipManager getInstance(){
        if (chitCardFlipManager == null){
            chitCardFlipManager = new ChitCardFlipManager();
        } return chitCardFlipManager;
    }

    public void setUncoveredChitCardsByPlayer(ArrayList<Circle> uncoveredChitCardsByPlayer) {
        this.uncoveredChitCardsByPlayer = uncoveredChitCardsByPlayer;
    }

    public void setCoveredChitCardShapes(Circle[] newCoveredChitCardShapes) {
        coveredChitCardShapes = newCoveredChitCardShapes;
    }

    public void setAnimationInProgress(boolean COVERING_ANIMATION_IN_PROGRESS) {
        this.animationInProgress = COVERING_ANIMATION_IN_PROGRESS;
    }
    public boolean getAnimationInProgress() {
        return animationInProgress;
    }

    public Circle[] getCoveredChitCardShapes() {
        return coveredChitCardShapes;
    }


    /**
     * Animates the uncovering of the clicked chit card
     * @param chitCardChosen the shape clicked by the player
     * @param cardChosen the chit card (model) chosen
     */
    public void handleUncover(Circle chitCardChosen, ChitCard cardChosen){
        getCoveredChitCardShapes()[cardChosen.getIndex()] = null; // this is so that it won't be listening for clicks on next turn
        uncoveredChitCardsByPlayer.add(cardChosen.getUncoveredForm());

        // do animation
        Circle hiddenCircle = cardChosen.getUncoveredForm();
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(500), chitCardChosen);
        rotateTransition.setAxis(Rotate.Y_AXIS); // Rotate around the Y axis
        rotateTransition.setByAngle(360); // Rotate 180 degrees
        setAnimationInProgress(true);
        rotateTransition.setOnFinished(event -> {
            chitCardChosen.setVisible(false);
            hiddenCircle.setVisible(true);
            chitCardChosen.getTransforms().clear(); // Clear the transformation
            setAnimationInProgress(false);

        });
        rotateTransition.play();
    }

    public void resetOnClickListener(HashMap<Circle, ChitCard> mapping){
        // all the chit cards that were uncovered have been covered, so they need to start listening for clicks again
        for (Circle uncoveredChitCard : uncoveredChitCardsByPlayer) {
            ChitCard card = mapping.get(uncoveredChitCard);
            getCoveredChitCardShapes()[card.getIndex()] = card.getCoveredForm();
        }
    }


    /**
     * Covers up all chit cards.
     * @param chitCardAdapter the chit card adapter containing model-view mappings
     */
    public void handleTurnEnd(ChitCardAdapter chitCardAdapter){
        // do flip back animation
        for (Circle uncoveredChitCard : uncoveredChitCardsByPlayer) {
            ChitCard chitCard = chitCardAdapter.getViewControllerMapping().get(uncoveredChitCard);
            // animate
            Circle hiddenCircle = chitCard.getCoveredForm();
            RotateTransition rotateTransition = new RotateTransition(Duration.millis(1500), uncoveredChitCard);
            rotateTransition.setAxis(Rotate.Y_AXIS); // Rotate around the Y axis
            rotateTransition.setByAngle(360);
            rotateTransition.setOnFinished(event -> {
                uncoveredChitCard.setVisible(false);
                hiddenCircle.setVisible(true);
                uncoveredChitCard.getTransforms().clear(); // Clear the transformation
            });
            rotateTransition.play();

        }
        uncoveredChitCardsByPlayer.clear(); // all the chit cards that were uncovered have been covered, so they need to start listening for clicks again

    }

}
