package main.game;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
class PlayerIndicator extends JLabel {
    public PlayerIndicator(int playerNumber) {
        super("Player " + playerNumber, SwingConstants.CENTER);
        // Customize appearance if needed
        setOpaque(true);
        setBackground(Color.LIGHT_GRAY); // Example background color
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }
}
