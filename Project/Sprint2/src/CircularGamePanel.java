////import javax.swing.*;
////import java.awt.*;
////
////public class CircularGamePanel extends JPanel {
////    private static final int OUTER_DIAMETER = 400;
////    private static final int INNER_DIAMETER = 200;
////    private static final int NUM_SPACES = 24; // Or however many spaces there are
////
////    public CircularGamePanel() {
////        setOpaque(false);
////        setPreferredSize(new Dimension(OUTER_DIAMETER, OUTER_DIAMETER));
////    }
////
////    @Override
////    protected void paintComponent(Graphics g) {
////        super.paintComponent(g);
////        drawOuterRing(g);
////        drawInnerCircle(g);
////    }
////
////    private void drawOuterRing(Graphics g) {
////        int centerX = getWidth() / 2;
////        int centerY = getHeight() / 2;
////
////        // Draw the outer ring
////        g.setColor(new Color(205, 133, 63)); // A brown color for the ring
////        g.fillOval(centerX - OUTER_DIAMETER / 2, centerY - OUTER_DIAMETER / 2,
////                OUTER_DIAMETER, OUTER_DIAMETER);
////
////        // Draw spaces
////        for (int i = 0; i < NUM_SPACES; i++) {
////            double angle = 2 * Math.PI * i / NUM_SPACES;
////            int spaceX = (int) (centerX + (OUTER_DIAMETER / 2 - 20) * Math.cos(angle));
////            int spaceY = (int) (centerY + (OUTER_DIAMETER / 2 - 20) * Math.sin(angle));
////            g.setColor(Color.BLACK); // Color for the spaces
////            g.fillOval(spaceX - 15, spaceY - 15, 30, 30);
////        }
////    }
////
////    private void drawInnerCircle(Graphics g) {
////        int centerX = getWidth() / 2;
////        int centerY = getHeight() / 2;
////        g.setColor(Color.WHITE); // Color for the inner circle
////        g.fillOval(centerX - INNER_DIAMETER / 2, centerY - INNER_DIAMETER / 2,
////                INNER_DIAMETER, INNER_DIAMETER);
////    }
////}
//import javax.swing.*;
//import java.awt.*;
//import java.awt.geom.Arc2D;
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.IOException;
//import java.io.File;
//import java.awt.geom.AffineTransform;
//
//public class CircularGamePanel extends JPanel {
//    private static final int OUTER_DIAMETER = 600;
//    private static final int INNER_DIAMETER = 400;
//    private static final int NUM_OUTER_SEGMENTS = 24;
//    private static final int NUM_INNER_CIRCLES = 16;
//
//    public CircularGamePanel() {
//        setOpaque(false);
//        setPreferredSize(new Dimension(OUTER_DIAMETER, OUTER_DIAMETER));
//    }
//
//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        Graphics2D g2d = (Graphics2D) g;
//
//        // Draw the circular board and segments
//        drawCircularBoard(g2d);
//
//        // Additional drawing methods for tokens and chit cards
//        // ... (You can implement these based on your game logic)
//    }
//
//    private void drawCircularBoard(Graphics2D g2d) {
//        int centerX = getWidth() / 2;
//        int centerY = getHeight() / 2;
//        int thickness = 40; // The thickness of the board ring
//        int innerDiameter = Math.min(centerX, centerY) - (2 * thickness); // The inner diameter of the board ring
//
//        // Draw the board ring
//        g2d.setColor(new Color(139, 69, 19)); // A brown color for the board
//        g2d.fillOval(centerX - OUTER_DIAMETER / 2, centerY - OUTER_DIAMETER / 2, OUTER_DIAMETER, OUTER_DIAMETER); // The outer circle
//        g2d.setColor(getBackground()); // The color of the background for the inner circle
//        g2d.fillOval(centerX - innerDiameter / 2, centerY - innerDiameter / 2, innerDiameter, innerDiameter); // The inner circle
//
//        // Draw the segments
//        double segmentAngle = 360.0 / NUM_OUTER_SEGMENTS;
//        for (int i = 0; i < NUM_OUTER_SEGMENTS; i++) {
//            AffineTransform old = g2d.getTransform();
//            g2d.rotate(Math.toRadians(segmentAngle * i), centerX, centerY);
//            g2d.setColor(new Color(160, 82, 45)); // A different brown color for the segments
//            g2d.fillArc(centerX - OUTER_DIAMETER / 2, centerY - OUTER_DIAMETER / 2, OUTER_DIAMETER, OUTER_DIAMETER, (int)(i * segmentAngle), (int)segmentAngle);
//            g2d.setTransform(old);
//        }
//    }
//
//    // You need to implement these methods according to your game logic
//    private void drawTokens(Graphics2D g2d) {
//        // Method to draw player tokens on the board
//    }
//
//    private void drawChitCards(Graphics2D g2d) {
//        // Method to draw chit cards in the center of the board
//    }
//}

import javax.swing.*;
import java.awt.*;
import java.util.Random;


public class CircularGamePanel extends JPanel {
    private static final int OUTER_CIRCLE_DIAMETER = 600;
    private static final int BOARD_DIVISIONS = 24;
    private static final int CHIT_CARDS = 16;
    private static final int INNER_DIAMETER = 400;
    private static final int CHIT_CARD_DIAMETER = 50; // Adjust as needed

    // Random for chit card placement
    private final Random random = new Random();


    public CircularGamePanel() {
        setOpaque(false);
        setPreferredSize(new Dimension(OUTER_CIRCLE_DIAMETER, OUTER_CIRCLE_DIAMETER));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMainBoard(g);
        drawInnerCircle(g);
        drawPlayerStartPositions(g);
        drawChitCards(g);
    }
    private void drawInnerCircle(Graphics g) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int innerRadius = INNER_DIAMETER / 2 - CHIT_CARD_DIAMETER; // Make room for chit cards

        // Draw the inner circle to define the chit card area
        g.setColor(Color.YELLOW); // Color for the inner area
        g.drawOval(centerX - innerRadius, centerY - innerRadius, innerRadius * 2, innerRadius * 2);
    }



    private void drawMainBoard(Graphics g) {
        int x = (getWidth() - OUTER_CIRCLE_DIAMETER) / 2;
        int y = (getHeight() - OUTER_CIRCLE_DIAMETER) / 2;
        g.setColor(Color.WHITE);  // Main board color
        for (int i = 0; i < BOARD_DIVISIONS; i++) {
            ((Graphics2D) g).fillArc(x, y, OUTER_CIRCLE_DIAMETER, OUTER_CIRCLE_DIAMETER,
                    (int) (i * (360.0 / BOARD_DIVISIONS)), (int) (360.0 / BOARD_DIVISIONS));
        }
    }

    private void drawPlayerStartPositions(Graphics g) {
        int radius = OUTER_CIRCLE_DIAMETER / 3;  // Adjust radius for player start positions
        int[] startX = new int[] {
                getWidth() / 2 - radius,
                getWidth() / 2 + radius,
                getWidth() / 2 + radius,
                getWidth() / 2 - radius
        };
        int[] startY = new int[] {
                getHeight() / 2 - radius,
                getHeight() / 2 - radius,
                getHeight() / 2 + radius,
                getHeight() / 2 + radius
        };
        g.setColor(Color.YELLOW);  // Player start positions color
        for (int i = 0; i < 4; i++) {  // Draw four player positions
            g.fillOval(startX[i] - 50, startY[i] - 50, 100, 100);
        }
    }

    private void drawChitCardPositions(Graphics g) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int innerRadius = OUTER_CIRCLE_DIAMETER / 8;
        int cardDiameter = OUTER_CIRCLE_DIAMETER / 12;  // Adjust card diameter as needed
        g.setColor(Color.ORANGE);  // Chit card color
        for (int i = 0; i < CHIT_CARDS; i++) {
            double angle = 2 * Math.PI * i / CHIT_CARDS;
            int x = (int) (centerX + innerRadius * Math.cos(angle) - cardDiameter / 2);
            int y = (int) (centerY + innerRadius * Math.sin(angle) - cardDiameter / 2);
            g.fillOval(x, y, cardDiameter, cardDiameter);
        }
    }
    private void drawChitCards(Graphics g) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int innerRadius = INNER_DIAMETER / 2 - CHIT_CARD_DIAMETER; // Make room for chit cards

        for (int i = 0; i < CHIT_CARDS; i++) {
            int x, y;
            do {
                // Randomly position within the inner circle
                x = random.nextInt(INNER_DIAMETER) - INNER_DIAMETER / 2 + centerX;
                y = random.nextInt(INNER_DIAMETER) - INNER_DIAMETER / 2 + centerY;
            } while (!isWithinCircle(x, y, centerX, centerY, innerRadius - CHIT_CARD_DIAMETER / 2));

            // Draw the chit card
            g.setColor(Color.ORANGE); // Chit card color
            g.fillOval(x - CHIT_CARD_DIAMETER / 2, y - CHIT_CARD_DIAMETER / 2, CHIT_CARD_DIAMETER, CHIT_CARD_DIAMETER);
        }
    }

    private boolean isWithinCircle(int x, int y, int centerX, int centerY, int radius) {
        // Check if a point is within the inner circle
        int dx = x - centerX;
        int dy = y - centerY;
        return (dx * dx + dy * dy) <= (radius * radius);
    }

}
