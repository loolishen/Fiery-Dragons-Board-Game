package com.example.demo;

public class Volcano {
    private VolcanoCard[] volcanoRing;
    private VolcanoSegment[] volcanoSegments;
    private int segmentLength;
    private int totalCards;

    public Volcano(int segmentLength, int numCards) {
        this.segmentLength = segmentLength;
        this.totalCards = numCards;
        volcanoRing = new VolcanoCard[numCards];
    }

    public VolcanoCard[] getVolcanoRing() {
        return volcanoRing;
    }

    private int calculateSegmentLength(int numCards){
        return 0;
    }

    public int getTotalCards(){
        return totalCards;
    }



    public VolcanoCard getVolcanoCardByID(int id){
        return volcanoRing[id-1]; // our ID starts from 1 but array indexing is 0
    }
}
