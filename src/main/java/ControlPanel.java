package main.java;

import javax.swing.*;
import main.java.characters.Salamander;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;


// Load the image file
//        Image salamanderImage = null;
//        try {
//            salamanderImage = ImageIO.read(new File("C:\\Users\\Loo Li Shen\\Pictures\\green salam.jpg"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // Create a Salamander instance with the loaded image
//        Salamander salamander = new Salamander(salamanderImage);
//
//        // Create a JFrame to display the Salamander image
//        JFrame frame = new JFrame("Salamander");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.getContentPane().add(salamander); // Add the Salamander instance to the frame
//        frame.pack();
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);

public class ControlPanel {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Fiery Map");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            FieryMap fieryMap = new FieryMap();

            // Load the image file for the player
            Image playerImage = null;
            try {
                playerImage = ImageIO.read(new File("C:\\Users\\Loo Li Shen\\Pictures\\green salam.jpg")); // Specify the path to your player image
            } catch (IOException e) {
                e.printStackTrace();
            }

//            // Create a Player instance with the image and playerName
//            Player player = new Player(playerImage, "Player 1");
//
//            // Check if there is already a player added to the fieryMap
//            if (fieryMap.getPlayer() == null) {
//                // Calculate adjustedRadius and circleRadius based on FieryMap's properties
//                int adjustedRadius = (int) (fieryMap.getRadius() * fieryMap.getZoomFactor());
//                int circleRadius = (int) (fieryMap.getRadius() * fieryMap.getZoomFactor() / 5);
//
//                // Calculate player position considering the half circle outside to the north
//                int playerX = fieryMap.getCenterX() - circleRadius; // Align player horizontally with the left side of the circle
//                int playerY = fieryMap.getCenterY() - adjustedRadius - circleRadius - playerImage.getHeight(null); // Adjust vertically so that the bottom of the player's image aligns with the top of the circle
//
//
//                // Set player position
//                Point playerPosition = new Point(playerX, playerY);
//
//                // Set player and its position in the FieryMap
//                fieryMap.setPlayer(player, playerPosition, adjustedRadius, circleRadius);
//            }

            frame.add(fieryMap);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}



