package main.game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 Represents the main panel of the "Fiery Dragons Game", responsible for displaying the game interface.
 */

public class GamePanel extends JPanel {
    static final int NUM_CARDS = 24;
    static final int PANEL_WIDTH = 900;
    static final int PANEL_HEIGHT = 850;

    private List<Card> chitCards;
    private String[] volcanoImageNames = {"babydragon.png", "bat.png", "salamander.png", "spider.png"};

    public GamePanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setLayout(null);
        chitCards = loadChitCards();

        JPanel gridPanel = new JPanel();
        add(gridPanel);
        GameSetupFacade.setupChitCards(gridPanel, chitCards);
        GameSetupFacade.setupVolcanoRing(this, Arrays.asList(volcanoImageNames));
        GameSetupFacade.setupPlayerIndicators(this, new String[]{"dragon1.png", "bat1.png", "spider1.png", "salamander1.png"}, new int[]{1, 7, 13, 19});
        GameSetupFacade.setupPlayerTokens(this, new int[]{1, 2, 3, 4}, new int[]{1, 7, 13, 19});

    }


    private List<Card> loadChitCards() {
        String[] imageNames = {
                "bat1.png", "bat2.png", "bat3.png",
                "dragon1.png", "dragon2.png", "dragon3.png",
                "salamander1.png", "salamander2.png", "salamander3.png",
                "spider1.png", "spider2.png", "spider3.png",
                "skull1.png", "skull2.png", "skull3.png", "skull4.png"
        };
        List<Card> cards = new ArrayList<>();
        for (String imageName : imageNames) {
            cards.add(new Card(imageName));
        }
        return cards;
    }

    }
