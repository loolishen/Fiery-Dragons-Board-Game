package main.game;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Provides factory methods for creating various types of card-related UI components in the game.
 */
public class CardFactory {

    // Factory method for creating a general chit card
    public static ChitCard createChitCard(String imageName) {
        return new ChitCard(new Card(imageName));
    }

    // Factory method for creating a volcano card with specific dimensions and tooltip
    public static JLabel createVolcanoCard(String imageName, int width, int height) {
        URL imageUrl = CardFactory.class.getResource("/" + imageName);
        if (imageUrl == null) {
            System.err.println("Resource not found: " + imageName);
            return new JLabel();  // Return an empty label in case of error
        }
        ImageIcon originalIcon = new ImageIcon(imageUrl);
        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel cardLabel = new JLabel(scaledIcon);
        cardLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return cardLabel;
    }

    // Factory method for creating player indicator cards with specific attributes
    public static PlayerIndicator createPlayerIndicator(String imageName, int x, int y) {
        return new PlayerIndicator(imageName, x, y);
    }
}
