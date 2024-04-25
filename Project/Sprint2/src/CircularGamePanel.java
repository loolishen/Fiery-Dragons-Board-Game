import javax.swing.*;
import java.awt.*;

public class CircularGamePanel extends JPanel {
    private static final int DIAMETER = 400; // Set a fixed diameter for the circle


    public CircularGamePanel() {
        setOpaque(false); // Make the panel transparent
        setPreferredSize(new Dimension(DIAMETER, DIAMETER)); // Adjust size based on the circle size
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGameCircle(g);
    }

    private void drawGameCircle(Graphics g) {
        g.setColor(Color.WHITE); // Set the color of the circle
        g.fillOval((getWidth() - DIAMETER) / 2, (getHeight() - DIAMETER) / 2, DIAMETER, DIAMETER);
    }
}
