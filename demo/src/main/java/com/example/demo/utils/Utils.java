package com.example.demo.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Utils {

    /**
     * Method to generate all possible pairs of numbers within the given range
     */
    public static ArrayList<int[]> generatePairs(int min, int max) {
        ArrayList<int[]> pairs = new ArrayList<>();
        for (int i = min; i <= max; i++) {
            for (int j = min; j <= max; j++) {
                pairs.add(new int[]{i, j});
            }
        }
        return pairs;
    }

    public static <T> void shuffleIntArray(ArrayList<T> array, long seed){
        if (seed != 0) {
            Collections.shuffle(array, new Random(seed));
        } else {Collections.shuffle(array);}
    }
}
