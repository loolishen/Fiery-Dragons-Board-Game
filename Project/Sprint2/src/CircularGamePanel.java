//import javax.swing.*;
//import java.awt.*;
//
//public class CircularGamePanel extends JPanel {
//    private static final int OUTER_DIAMETER = 400;
//    private static final int INNER_DIAMETER = 200;
//    private static final int NUM_SPACES = 24; // Or however many spaces there are
//
//    public CircularGamePanel() {
//        setOpaque(false);
//        setPreferredSize(new Dimension(OUTER_DIAMETER, OUTER_DIAMETER));
//    }
//
//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        drawOuterRing(g);
//        drawInnerCircle(g);
//    }
//
//    private void drawOuterRing(Graphics g) {
//        int centerX = getWidth() / 2;
//        int centerY = getHeight() / 2;
//
//        // Draw the outer ring
//        g.setColor(new Color(205, 133, 63)); // A brown color for the ring
//        g.fillOval(centerX - OUTER_DIAMETER / 2, centerY - OUTER_DIAMETER / 2,
//                OUTER_DIAMETER, OUTER_DIAMETER);
//
//        // Draw spaces
//        for (int i = 0; i < NUM_SPACES; i++) {
//            double angle = 2 * Math.PI * i / NUM_SPACES;
//            int spaceX = (int) (centerX + (OUTER_DIAMETER / 2 - 20) * Math.cos(angle));
//            int spaceY = (int) (centerY + (OUTER_DIAMETER / 2 - 20) * Math.sin(angle));
//            g.setColor(Color.BLACK); // Color for the spaces
//            g.fillOval(spaceX - 15, spaceY - 15, 30, 30);
//        }
//    }
//
//    private void drawInnerCircle(Graphics g) {
//        int centerX = getWidth() / 2;
//        int centerY = getHeight() / 2;
//        g.setColor(Color.WHITE); // Color for the inner circle
//        g.fillOval(centerX - INNER_DIAMETER / 2, centerY - INNER_DIAMETER / 2,
//                INNER_DIAMETER, INNER_DIAMETER);
//    }
//}
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import java.awt.geom.AffineTransform;


public class CircularGamePanel extends JPanel {
    private static final int OUTER_DIAMETER = 600;
    private static final int INNER_DIAMETER = 400;
    private static final int NUM_OUTER_SEGMENTS = 24;
    private static final int NUM_INNER_CIRCLES = 16;

    public CircularGamePanel() {
        setOpaque(false);
        setPreferredSize(new Dimension(OUTER_DIAMETER, OUTER_DIAMETER));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawSegmentedOuterCircle(g2d);
        drawInnerCircles(g2d);
    }

    private void drawSegmentedOuterCircle(Graphics2D g2d) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        double segmentAngle = 360.0 / NUM_OUTER_SEGMENTS;
        int segmentRadius = OUTER_DIAMETER / 2;

        for (int i = 0; i < NUM_OUTER_SEGMENTS; i++) {
            double startAngle = i * segmentAngle;

            // Transform for the segment image
            AffineTransform transform = new AffineTransform();
            transform.translate(centerX, centerY);
            transform.rotate(Math.toRadians(startAngle + segmentAngle / 2)); // Rotate to the segment's center angle
            transform.translate(-segmentRadius, -segmentRadius / 2); // Translate so the image is centered on the segment

        }
    }

    private void drawInnerCircles(Graphics2D g2d) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = INNER_DIAMETER / 8; // Adjust the radius for the chit cards
        double angleIncrement = 2 * Math.PI / NUM_INNER_CIRCLES;

        for (int i = 0; i < NUM_INNER_CIRCLES; i++) {
            double angle = i * angleIncrement;
            int x = (int) (centerX + (INNER_DIAMETER / 2) * Math.cos(angle) - radius);
            int y = (int) (centerY + (INNER_DIAMETER / 2) * Math.sin(angle) - radius);
        }
    }
}
