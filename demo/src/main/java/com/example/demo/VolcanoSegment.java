package com.example.demo;

public class VolcanoSegment {

    private VolcanoCard[] segmentCards;
    private int segmentLength;
    private int counter = 0;
    private int segmentID;
    private boolean hasCave;

    public VolcanoSegment(int newSegmentID, int newSegmentLength){
        segmentLength = newSegmentLength;
        segmentID = newSegmentID;
        segmentCards = new VolcanoCard[segmentLength];
        hasCave = false;
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
