package main.game;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Represents a player indicator icon displayed on the game interface to indicate player starting positions.
 */
public class PlayerIndicator extends JLabel {
    private static final int INDICATOR_SIZE = 50;

    public PlayerIndicator(String imageName, int x, int y) {
        URL imageUrl = getClass().getResource("/" + imageName);
        if (imageUrl != null) {
            ImageIcon icon = new ImageIcon(new ImageIcon(imageUrl).getImage().getScaledInstance(INDICATOR_SIZE, INDICATOR_SIZE, Image.SCALE_SMOOTH));
            setIcon(icon);
        } else {
            System.err.println("Resource not found: " + imageName);
        }

        // Set size and position of the label
        setBounds(x, y, INDICATOR_SIZE, INDICATOR_SIZE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setToolTipText(imageName.substring(0, imageName.lastIndexOf('1')));
    }
}
