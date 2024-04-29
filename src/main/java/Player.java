package main.java;

import main.java.characters.CharacterToken;
import main.java.characters.CharacterTypes;

import java.awt.*;

public class Player extends CharacterToken {
    public String playerName;

    public Player(Image image, String playerName) {
        super(image);
        this.playerName = playerName;
        this.addCapability(CharacterTypes.PLAYER);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw circle
        g.setColor(Color.WHITE);
        g.fillOval(0, 0, getWidth(), getHeight());

        // Draw token circle at the north outer circle
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(getWidth(), getHeight()) / 2;

        int circleRadius = radius / 5; // Adjust the radius of the token circle

        // Draw image inside the circle
        if (image != null) {
            int imageSize = Math.min(getWidth(), getHeight()) * 3 / 5; // Use a portion of the circle size for the image
            int x = (getWidth() - imageSize) / 2;
            int y = (getHeight() - imageSize) / 2;
            g.drawImage(image, x, y, imageSize, imageSize, this); // Draw the image with the correct size
        }
    }


    // Draw token circle at the south outer circle
    // g.setColor(Color.RED);
    // g.fillOval(centerX - circleRadius, centerY + radius - circleRadius, 2 * circleRadius, 2 * circleRadius);

    // Draw token circle at the west outer circle
    // g.setColor(Color.MAGENTA);
    // g.fillOval(centerX - radius - circleRadius, centerY - circleRadius, 2 * circleRadius, 2 * circleRadius);

    // Draw token circle at the east outer circle
    // g.setColor(Color.ORANGE);
    // g.fillOval(centerX + radius - circleRadius, centerY - circleRadius, 2 * circleRadius, 2 * circleRadius);

}
