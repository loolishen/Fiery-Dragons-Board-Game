package main.game;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Provides factory methods for creating various types of card-related UI components in the game.
 */
public class CardFactory {

    /**
     * Creates a chit card with the specified image name.
     */
    public static ChitCard createChitCard(String imageName) {
        return new ChitCard(new Card(imageName));
    }

    /**
     * Creates a volcano card with the specified animal, width, and height.
     */
    public static JLabel createVolcanoCard(Animal animal, int width, int height) {
        // Get the image name from the Animal enum
        String imageName = animal.getImageName();

        // Load the image URL
        URL imageUrl = CardFactory.class.getResource("/" + imageName);
        if (imageUrl == null) {
            System.err.println("Resource not found: " + imageName);
            return new JLabel();  // Return an empty label in case of error
        }

        // Scale and create the ImageIcon
        ImageIcon originalIcon = new ImageIcon(imageUrl);
        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // Create and configure the JLabel for the volcano card
        JLabel cardLabel = new JLabel(scaledIcon);
        cardLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return cardLabel;
    }

    /**
     * Creates a player indicator card with the specified image name, x-coordinate, and y-coordinate.
     */
    public static PlayerIndicator createPlayerIndicator(String imageName, int x, int y) {
        return new PlayerIndicator(imageName, x, y);
    }
}
