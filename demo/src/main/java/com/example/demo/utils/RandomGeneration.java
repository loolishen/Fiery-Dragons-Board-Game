package com.example.demo.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomGeneration {
    public static List<int[]> getRandomPairs(int maxNumber) {
        // Define the range of numbers
        int min = 1;
        int max = (int) Math.sqrt(maxNumber);

        // Generate all possible pairs of numbers
        ArrayList<int[]> pairs = generatePairs(min, max);

        // Shuffle the list of pairs
        Collections.shuffle(pairs);

        // Select the first 16 unique pairs
        return pairs.subList(0, 16);

    }

    // Method to generate all possible pairs of numbers within the given range
    public static ArrayList<int[]> generatePairs(int min, int max) {
        ArrayList<int[]> pairs = new ArrayList<>();
        for (int i = min; i <= max; i++) {
            for (int j = min; j <= max; j++) {
                pairs.add(new int[]{i, j});
            }
        }
        return pairs;
    }
}
