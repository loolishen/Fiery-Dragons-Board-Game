package com.example.demo;

public class Volcano {
    private final VolcanoCard[] volcanoRing;

    public Volcano() {
        volcanoRing = new VolcanoCard[Constants.VOLCANO_RING_NUM_CARDS];
    }

    public VolcanoCard[] getVolcanoRing() {
        return volcanoRing;
    }

    public VolcanoCard getVolcanoCardByID(int id){
        return volcanoRing[id-1]; // our ID starts from 1 but array indexing is 0
    }

}
