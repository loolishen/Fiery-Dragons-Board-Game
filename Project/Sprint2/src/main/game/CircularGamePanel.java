package main.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class CircularGamePanel extends JPanel {
    private static final int CARD_SIZE = 50; // Assuming 50 is the size you want
    private static final int INNER_CIRCLE_RADIUS = 200; // Radius for inner circle positioning
    private static final int OUTER_CIRCLE_RADIUS = 300; // Radius for outer circle positioning
    private static final int NUM_INNER_CARDS = 16; // Number of inner circle cards
    private static final int NUM_OUTER_CARDS = 24; // Number of outer circle cards
    private Card[] outerCards; // Array for outer circle cards
    private ChitCard[] innerCards; // Array for inner circle cards


    public CircularGamePanel() {
        setPreferredSize(new Dimension(900, 700));
        setLayout(null); // Absolute positioning

        // Initialize card arrays
        outerCards = new Card[NUM_OUTER_CARDS];
        innerCards = new ChitCard[NUM_INNER_CARDS];

        setupOuterCards();
        setupInnerCards();
    }

    private void setupOuterCards() {
        double angleStep = 2 * Math.PI / NUM_OUTER_CARDS;
        ImageIcon faceDownIcon = new ImageIcon("path/to/faceDownImage.png"); // Placeholder for your face-down image path
        ImageIcon[] faceUpIcons = loadCardImages(); // Load face-up images

        for (int i = 0; i < NUM_OUTER_CARDS; i++) {
            Point pos = getPosition(i * angleStep, OUTER_CIRCLE_RADIUS, CARD_SIZE);
            ImageIcon faceUpIcon = faceUpIcons[i % faceUpIcons.length];
            outerCards[i] = new Card(faceUpIcon, faceDownIcon);
            outerCards[i].setBounds(pos.x, pos.y, CARD_SIZE, CARD_SIZE);
            add(outerCards[i]);
        }
    }

    private void setupInnerCards() {
        double angleStep = 2 * Math.PI / NUM_INNER_CARDS;
        ImageIcon[] images = loadCardImages(); // This should load all your card images
        Collections.shuffle(Arrays.asList(images)); // This shuffles the images array

        ImageIcon faceDownIcon = ChitCard.createPlaceholderIcon(Color.ORANGE, CARD_SIZE, CARD_SIZE);

        for (int i = 0; i < NUM_INNER_CARDS; i++) {
            Point pos = getPosition(i * angleStep, INNER_CIRCLE_RADIUS, CARD_SIZE);
            innerCards[i] = new ChitCard(images[i], faceDownIcon); // Pass both face up and face down icons
            innerCards[i].setBounds(pos.x, pos.y, CARD_SIZE, CARD_SIZE);
            add(innerCards[i]);
        }
    }


    private Point getPosition(double angle, int radius, int size) {
        int x = (int) (getWidth() / 2 + radius * Math.cos(angle) - size / 2);
        int y = (int) (getHeight() / 2 + radius * Math.sin(angle) - size / 2);
        return new Point(x, y);
    }

    private ImageIcon[] loadCardImages() {
        String[] imageNames = {
                "bat1.png",
                "bat2.png",
                "bat3.png",
                "dragon1.png",
                "dragon2.png",
                "dragon3.png",
                "salamander1.png",
                "salamander2.png",
                "salamander3.png",
                "spider1.png",
                "spider2.png",
                "spider3.png",
                "skull1.png",
                "skull2.png",
                "skull3.png",
                "skull4.png"
        };
        ImageIcon[] icons = new ImageIcon[imageNames.length];
        for (int i = 0; i < imageNames.length; i++) {
            URL imageUrl = getClass().getClassLoader().getResource(imageNames[i]);
            if (imageUrl != null) {
                icons[i] = new ImageIcon(imageUrl);
            } else {
                System.err.println("Resource not found: " + imageNames[i]);
            }
        }
        return icons;
    }
}