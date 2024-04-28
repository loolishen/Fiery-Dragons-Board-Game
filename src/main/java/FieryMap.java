package main.java;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Collections;

public class FieryMap extends JPanel {

    private static final int SEGMENT_COUNT = 8;
    private static final int PART_COUNT = 3;
    private static final Color[] SEGMENT_COLORS = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA, Color.PINK};

    private Point[][] segmentCoordinates = new Point[SEGMENT_COUNT][PART_COUNT];
    private int centerX;
    private int centerY;
    private int radius;

    public FieryMap() {
        calculateCoordinates();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw outer circle
        g2d.setColor(Color.BLACK);
        g2d.drawOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);

        // Draw segments
        for (int i = 0; i < SEGMENT_COUNT; i++) {
            double startAngle = 2 * Math.PI * i / SEGMENT_COUNT;
            double endAngle = 2 * Math.PI * (i + 1) / SEGMENT_COUNT;
            g2d.setColor(SEGMENT_COLORS[i % SEGMENT_COLORS.length]);
            g2d.fillArc(centerX - radius, centerY - radius, 2 * radius, 2 * radius, (int) Math.toDegrees(startAngle), (int) Math.toDegrees(endAngle - startAngle));
        }

        // Draw dividing lines for parts
        for (int i = 0; i < SEGMENT_COUNT; i++) {
            for (int j = 0; j < PART_COUNT; j++) {
                Point point = getSegmentPartCoordinate(i, j);
                g2d.setColor(Color.BLACK);
                g2d.drawLine(centerX, centerY, point.x, point.y);
            }
        }

        // Draw inner circle
        int innerRadius = radius / 2; // Adjust the inner radius as needed
        // Draw inner circle with yellow fill color
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(centerX - innerRadius, centerY - innerRadius, 2 * innerRadius, 2 * innerRadius);
    }

    private void calculateCoordinates() {
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        radius = Math.min(getWidth(), getHeight()) / 2;

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
