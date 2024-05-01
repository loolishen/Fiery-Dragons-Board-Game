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

    public static void setupVolcanoRing(GamePanel panel, List<Animal> animals) {
        int cardWidth = 55;
        int cardHeight = 65;
        int centerX = GamePanel.PANEL_WIDTH / 2;
        int centerY = GamePanel.PANEL_HEIGHT / 2 - 80;
        int radius = 270;

        Collections.shuffle(animals);

        for (int i = 0; i < GamePanel.NUM_CARDS; i++) {
            double angle = 2 * Math.PI * i / GamePanel.NUM_CARDS;
            int x = centerX + (int) (radius * Math.sin(angle)) - cardWidth / 2;
            int y = centerY + (int) (radius * Math.cos(angle)) - cardHeight / 2;
            Animal animal = animals.get(i % animals.size());

            JLabel volcanoCard = CardFactory.createVolcanoCard(animal, cardWidth, cardHeight);
            volcanoCard.setBounds(x, y, cardWidth, cardHeight);
            panel.add(volcanoCard);
        }
    }

    public static void setupPlayerIndicators(GamePanel panel, String[] playerImages, int[] cardPositions) {
        int radius = 335;
        int indicatorSize = 50;

        for (int i = 0; i < playerImages.length; i++) {
            double angle = 2 * Math.PI * (cardPositions[i] - 1) / GamePanel.NUM_CARDS;
            int x = (int) (GamePanel.PANEL_WIDTH / 2 + radius * Math.cos(angle)) - indicatorSize / 2;
            int y = (int) ((GamePanel.PANEL_HEIGHT - 80) / 2 - radius * Math.sin(angle)) - indicatorSize / 2 - 30;

            // Adjusting position to ensure there is no overlap
            x += (55 - indicatorSize) / 2;  // Adjust centering horizontally
            y += (65 - indicatorSize) / 2;  // Adjust centering vertically

            PlayerIndicator indicator = CardFactory.createPlayerIndicator(playerImages[i], x, y);
            panel.add(indicator);
            panel.setComponentZOrder(indicator, 0); // This ensures indicators are always on top
        }
    }

    public static void setupPlayerTokens(GamePanel panel, int[] playerNumbers, int[] cardPositions) {
        int tokenSize = 30; // The size of the player token
        Font tokenFont = new Font("Arial", Font.BOLD, 20); // Font for token numbers

        for (int i = 0; i < playerNumbers.length; i++) {
            JLabel tokenLabel = new JLabel(String.valueOf(playerNumbers[i]));
            tokenLabel.setFont(tokenFont);
            tokenLabel.setForeground(Color.WHITE);
            tokenLabel.setHorizontalAlignment(SwingConstants.CENTER);
            tokenLabel.setVerticalAlignment(SwingConstants.CENTER);
            tokenLabel.setOpaque(true);
            tokenLabel.setBackground(Color.BLUE);

            // Adjusting position to match player indicators
            int radius = 305;
            double angle = 2 * Math.PI * (cardPositions[i] - 1) / GamePanel.NUM_CARDS;
            int x = (int) (GamePanel.PANEL_WIDTH / 2 + radius * Math.cos(angle)) - tokenSize / 2;
            int y = (int) ((GamePanel.PANEL_HEIGHT - 80) / 2 - radius * Math.sin(angle)) - tokenSize / 2 - 30;

            tokenLabel.setBounds(x, y, tokenSize, tokenSize);
            panel.add(tokenLabel);
            panel.setComponentZOrder(tokenLabel, 0); // This ensures tokens are always on top of other components
        }
    }

}
