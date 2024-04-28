package com.example.demo;
/**
 * Logical representation of a volcano segment. It stores references to the volcano cards, and the number is segment length
 */
public class VolcanoSegment {
    private final VolcanoCard[] segmentCards;
    private final int segmentLength;
    private int counter = 0;
    private final int segmentID;
    private boolean hasCave; // used so that we don't hard code the segments that should be shuffled in initials setup

    public VolcanoSegment(int newSegmentID, int newSegmentLength){
        segmentLength = newSegmentLength;
        segmentID = newSegmentID;
        segmentCards = new VolcanoCard[segmentLength];
        hasCave = false;
    }

    public int getSegmentLength() {
        return segmentLength;
    }

    public void addVolcanoCard(VolcanoCard volcanoCard){
        segmentCards[counter] = volcanoCard;
        counter += 1;

    }
    public int getSegmentID() {
        return segmentID;
    }

    public boolean getHasCave() {
        return hasCave;
    }

    public void setHasCave(boolean hasCave) {
        this.hasCave = hasCave;
    }

    public VolcanoCard[] getSegmentCards() {
        return segmentCards;
    }
}
