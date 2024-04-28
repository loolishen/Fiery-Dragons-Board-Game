package com.example.demo;

import com.almasb.fxgl.dsl.FXGL;

import java.util.Map;

import static com.example.demo.AnimalType.*;
import static java.util.Map.entry;

public class Constants {
    // For RNG testing
    public static long RNG_SEED = 531;

    // Animal related info: types and max count
    public static final AnimalType[] ANIMAL_TYPES = {AnimalType.SALAMANDER, AnimalType.BAT, AnimalType.SPIDER, AnimalType.BABY_DRAGON, AnimalType.DRAGON_PIRATE};
    public static final int DRAGON_PIRATE_MAX_COUNT = 2;
    public static final int ANIMAL_MAX_COUNT = 3;

    // For image resource paths
    public final static Map<AnimalType, String> ANIMAL_IMAGE_IMAGE_PATH_PREFIX_MAPPINGS = Map.ofEntries(
            entry(AnimalType.SALAMANDER, "/com/example/demo/assets/salamander"),
            entry(AnimalType.SPIDER, "/com/example/demo/assets/spider"),
            entry(AnimalType.BABY_DRAGON, "/com/example/demo/assets/babyDragon"),
            entry(AnimalType.BAT, "/com/example/demo/assets/bat"),
            entry(AnimalType.DRAGON_PIRATE, "/com/example/demo/assets/skull")
    );

    // Application window dimensions
    public static final int SCENE_WIDTH = FXGL.getAppWidth();
    public static final int SCENE_HEIGHT = FXGL.getAppHeight();

    // Info about chit cards,their UI positioning, their UI components
    public static final int NUM_CHIT_CARDS = 16;
    public static final int DRAGON_PIRATE_CHIT_CARD_COUNT = 4;
    public static final int CARD_RADIUS = 28; // Radius of the circular card
    public static final int CARD_SPACE_PADDING = 10; // Padding between cards
    public static final int CHIT_CARD_GRID_WIDTH = NUM_CHIT_CARDS/4;
    public static final int CHIT_CARD_GRID_HEIGHT = NUM_CHIT_CARDS/4;
    public static final int  CHIT_CARD_GRID_TOTAL_WIDTH = CHIT_CARD_GRID_WIDTH * (2 * CARD_RADIUS) + (CHIT_CARD_GRID_WIDTH - 1) * CARD_SPACE_PADDING;
    public static final int CHIT_CARD_GRID_TOTAL_HEIGHT = CHIT_CARD_GRID_HEIGHT * (2 * CARD_RADIUS) + (CHIT_CARD_GRID_HEIGHT - 1) * CARD_SPACE_PADDING;
    public static final int  START_X = (SCENE_WIDTH - CHIT_CARD_GRID_TOTAL_WIDTH) / 2; // starting x position to center the grid within the scene
    public static final int  START_Y = (SCENE_HEIGHT - CHIT_CARD_GRID_TOTAL_HEIGHT) / 2; // starting y position to center the grid within the scene


    // info about volcano ring
    public static final AnimalType[] ANIMALS = {SALAMANDER,SPIDER, BAT, BABY_DRAGON, SALAMANDER, BAT, SPIDER, SALAMANDER, BABY_DRAGON,
            BAT, BABY_DRAGON, SALAMANDER, BAT, SPIDER, BABY_DRAGON, SALAMANDER, BABY_DRAGON,
            SPIDER, BABY_DRAGON, BAT, SPIDER, SPIDER, BAT,SALAMANDER };
    public static final int VOLCANO_RING_RADIUS = 200;
    public static final int VOLCANO_RING_NUM_CARDS = 24;
    public static final int VOLCANO_CARD_WIDTH = 40;
    public static final int VOLCANO_CARD_HEIGHT = 60;
    public static final int VOLCANO_RING_SEGMENT_LENGTH = 3;


    // info about setup related to player including cave and dragon token
    public static final int NUM_PLAYERS = 4;
    public static final int SPACE_BETWEEN_PLAYERS = (VOLCANO_RING_NUM_CARDS-NUM_PLAYERS)/NUM_PLAYERS;
    public static final int END_TURN_RESULT = 0;
    public static final int CAVE_CIRCLE_RADIUS = 15;
    public static final int CAVE_POS_RADIUS_OFFSET = 35;
    public static final int CAVE_TEXT_OFFSET = 7;
    public static final int ANGLE_OFFSET = 15; // because the cave and token starts at second chit card of any given segment
    public static final double TOKEN_MOVE_ANGLE_INCREMENT = 360.0/VOLCANO_RING_NUM_CARDS;
    public static final int TOKEN_PATH_RADIUS_OFFSET = 65; // so that token moves on a circular path of the circumference
    // of a larger circle compared to the volcano ring's circle
    public static final int TOKEN_WIDTH = 25;
    public static final int TOKEN_HEIGHT = 45;


}
