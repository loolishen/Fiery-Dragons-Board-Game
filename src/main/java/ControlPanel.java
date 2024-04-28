package main.java;

import javax.swing.*;
import main.java.characters.Salamander;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ControlPanel {
    public static void main(String[] args) {
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

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Fiery Map");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            FieryMap fieryMap = new FieryMap();
            frame.add(fieryMap);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
