package com.example.demo.utils;

import com.example.demo.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Utility class
 * @author Jia Wynn Khor
 */
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

    /**
     * Shuffle an array of items
     * @param array the array to be shuffled
     * @param seed seed to be used, if it is 0, it won't be used
     * @param <T> the generic item
     */
    public static <T> void shuffleIntArray(ArrayList<T> array, long seed){
        if (seed != Config.NO_SEED) {
            Collections.shuffle(array, new Random(seed));
        } else {Collections.shuffle(array);}
    }
}
