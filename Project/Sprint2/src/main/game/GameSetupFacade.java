package main.game;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;

/**
 * Facade class for setting up various components of the game interface.
 */


public class GameSetupFacade {
    public static void setupChitCards(JPanel gridPanel, List<Card> chitCards) {
        int cardSize = 60;
        int spacing = 20;
        int gridWidth = 4 * (cardSize + spacing);
        int gridHeight = 4 * (cardSize + spacing);
        gridPanel.setLayout(new GridLayout(4, 4, spacing, spacing));
        gridPanel.setBounds((GamePanel.PANEL_WIDTH - gridWidth) / 2, (GamePanel.PANEL_HEIGHT - gridHeight) / 3, gridWidth, gridHeight);

        Collections.shuffle(chitCards);

        for (Card card : chitCards) {
            gridPanel.add(CardFactory.createChitCard(card.getImageName()));
        }
    }

    public static void setupVolcanoRing(GamePanel panel, List<String> volcanoImageNames) {
        int cardWidth = 55;
        int cardHeight = 65;
        int centerX = GamePanel.PANEL_WIDTH / 2;
        int centerY = GamePanel.PANEL_HEIGHT / 2 - 80;
        int radius = 270;

        Collections.shuffle(volcanoImageNames);

        for (int i = 0; i < GamePanel.NUM_CARDS; i++) {
            double angle = 2 * Math.PI * i / GamePanel.NUM_CARDS;
            int x = centerX + (int) (radius * Math.sin(angle)) - cardWidth / 2;
            int y = centerY + (int) (radius * Math.cos(angle)) - cardHeight / 2;
            String imageName = volcanoImageNames.get(i % volcanoImageNames.size());

            JLabel volcanoCard = CardFactory.createVolcanoCard(imageName, cardWidth, cardHeight);
            volcanoCard.setBounds(x, y, cardWidth, cardHeight);
            panel.add(volcanoCard);
        }
    }

    public static void setupPlayerIndicators(GamePanel panel, String[] playerImages, int[] cardPositions) {
        int radius = 295; // Base radius for placement
        int indicatorSize = 50; // The size of the player indicator

        for (int i = 0; i < playerImages.length; i++) {
            double angle = 2 * Math.PI * (cardPositions[i] - 1) / GamePanel.NUM_CARDS;
            int x = (int) (GamePanel.PANEL_WIDTH / 2 + radius * Math.cos(angle)) - indicatorSize / 2;
            int y = (int) ((GamePanel.PANEL_HEIGHT - 80) / 2 - radius * Math.sin(angle)) - indicatorSize / 2;
            x += 55 / 2 - indicatorSize / 2;  // Center horizontally on the card
            y += 65 / 2 - indicatorSize / 2;  // Center vertically on the card
            panel.add(CardFactory.createPlayerIndicator(playerImages[i], x, y));
        }
    }
}
