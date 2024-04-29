package main.java;

import main.java.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class FieryMap extends JPanel {

    private static final int SEGMENT_COUNT = 8;
    private static final int PART_COUNT = 3;
    private static final Color[] SEGMENT_COLORS = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA, Color.PINK};

    private Point[][] segmentCoordinates = new Point[SEGMENT_COUNT][PART_COUNT];
    private int centerX;
    private int centerY;
    private int radius;
    private int circleRadius;
    private double zoomFactor = 0.8;

    private Player player;
    private Point playerPosition;
    private int adjustedRadius;

    public FieryMap() {
        calculateCoordinates();
        JButton rotateButton = new JButton("Rotate");
        add(rotateButton);
        generateRandomColors();
    }

    private void generateRandomColors() {
        // Shuffle the segment colors array
        List<Color> segmentColorsList = Arrays.asList(SEGMENT_COLORS);
        Collections.shuffle(segmentColorsList);
    }

    public void setPlayer(Player player, Point position, int adjustedRadius, int circleRadius) {
        // Remove the previous player if exists
        if (this.player != null) {
            remove(this.player);
        }

        this.player = player;
        this.playerPosition = position;
        this.adjustedRadius = adjustedRadius;
        this.circleRadius = circleRadius;

        repaint(); // Trigger repaint to update the component
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // adjust zoom factor so that the parts don't get covered from being too big
        double zoomFactor = 0.8; // Adjust the zoom factor as needed

        // shuffle the segment colors array
        List<Color> segmentColorsList = Arrays.asList(SEGMENT_COLORS);
        Collections.shuffle(segmentColorsList);

        // draw outer circle with adjusted radius
        int adjustedRadius = (int) (radius * zoomFactor);
        g2d.setColor(Color.BLACK);
        g2d.drawOval(centerX - adjustedRadius, centerY - adjustedRadius, 2 * adjustedRadius, 2 * adjustedRadius);

        // draw segments
        for (int i = 0; i < SEGMENT_COUNT; i++) {
            double startAngle = 2 * Math.PI * i / SEGMENT_COUNT;
            double endAngle = 2 * Math.PI * (i + 1) / SEGMENT_COUNT;
            g2d.setColor(segmentColorsList.get(i));
            g2d.fillArc(centerX - adjustedRadius, centerY - adjustedRadius, 2 * adjustedRadius, 2 * adjustedRadius, (int) Math.toDegrees(startAngle), (int) Math.toDegrees(endAngle - startAngle));
        }

        // draw dividing lines for parts
        for (int i = 0; i < SEGMENT_COUNT; i++) {
            for (int j = 0; j < PART_COUNT; j++) {
                Point point = getSegmentPartCoordinate(i, j);
                double adjustedZoomFactor = zoomFactor + 0.06; // Adjust the zoom factor for the lines to make them touch the circumference
                int scaledX = (int) (centerX + (point.x - centerX) * adjustedZoomFactor);
                int scaledY = (int) (centerY + (point.y - centerY) * adjustedZoomFactor);
                g2d.setColor(Color.BLACK);
                g2d.drawLine(centerX, centerY, scaledX, scaledY);
            }
        }

        // draw inner circle
        int innerRadius = (int) (radius * zoomFactor / 2); // Adjust the inner radius as needed
        g2d.setColor(Color.YELLOW);
        g2d.drawOval(centerX - innerRadius, centerY - innerRadius, 2 * innerRadius, 2 * innerRadius);
        g2d.fillOval(centerX - innerRadius, centerY - innerRadius, 2 * innerRadius, 2 * innerRadius);

        // adjust the radius of the circles based on the zoom factor
        int circleRadius = (int) (radius * zoomFactor / 5); // Adjust the radius of the circles

        // Draw player token at the specified position if it exists
        if (player != null && playerPosition != null) {
            int tokenRadius = circleRadius / 2; // Adjust the token radius
            int tokenX = centerX + playerPosition.x - tokenRadius - player.getWidth() / 2;
            int tokenY = centerY + playerPosition.y - tokenRadius - player.getHeight();
            player.setBounds(tokenX, tokenY, player.getWidth(), player.getHeight());
            player.paintComponent(g2d);

        }

        // draw circle half outside to the north
        g2d.setColor(Color.BLUE);
        g2d.drawOval(centerX - circleRadius, centerY - adjustedRadius - circleRadius, 2 * circleRadius, 2 * circleRadius);
        g2d.fillOval(centerX - circleRadius, centerY - adjustedRadius - circleRadius, 2 * circleRadius, 2 * circleRadius);
        // draw circle half outside to the south
        g2d.setColor(Color.RED);
        g2d.drawOval(centerX - circleRadius, centerY + adjustedRadius - circleRadius, 2 * circleRadius, 2 * circleRadius);
        g2d.fillOval(centerX - circleRadius, centerY + adjustedRadius - circleRadius, 2 * circleRadius, 2 * circleRadius);
        // draw circle half outside to the east
        g2d.setColor(Color.ORANGE);
        g2d.drawOval(centerX + adjustedRadius - circleRadius, centerY - circleRadius, 2 * circleRadius, 2 * circleRadius);
        g2d.fillOval(centerX + adjustedRadius - circleRadius, centerY - circleRadius, 2 * circleRadius, 2 * circleRadius);
        // draw circle half outside to the west
        g2d.setColor(Color.MAGENTA);
        g2d.drawOval(centerX - adjustedRadius - circleRadius, centerY - circleRadius, 2 * circleRadius, 2 * circleRadius);
        g2d.fillOval(centerX - adjustedRadius - circleRadius, centerY - circleRadius, 2 * circleRadius, 2 * circleRadius);
    }



    private void calculateCoordinates() {
        int halfCircleRadius = radius / 5;

        centerX = getWidth() / 2;
        centerY = getHeight() / 2 + halfCircleRadius;
        radius = Math.min(getWidth(), getHeight()) / 2 - halfCircleRadius;

        for (int i = 0; i < SEGMENT_COUNT; i++) {
            double angle = 2 * Math.PI * i / SEGMENT_COUNT;
            for (int j = 0; j < PART_COUNT; j++) {
                int x1 = centerX + (int) (radius * Math.cos(angle));
                int y1 = centerY + (int) (radius * Math.sin(angle));
                int x2 = centerX + (int) (radius * Math.cos(angle + 2 * Math.PI / SEGMENT_COUNT));
                int y2 = centerY + (int) (radius * Math.sin(angle + 2 * Math.PI / SEGMENT_COUNT));
                int x = x1 + (x2 - x1) * j / PART_COUNT;
                int y = y1 + (y2 - y1) * j / PART_COUNT;
                segmentCoordinates[i][j] = new Point(x, y);
            }
        }
    }

    private Point getSegmentPartCoordinate(int segmentIndex, int partIndex) {
        return segmentCoordinates[segmentIndex][partIndex];
    }

    @Override
    public void invalidate() {
        super.invalidate();
        calculateCoordinates();
    }
}
