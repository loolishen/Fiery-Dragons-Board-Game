package game;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the Board Panel for the main starting frame.
 */
public class BoardPanel extends JPanel {
    private Image dragonImage;

    /**
     * Constructor for the Board Panel. Sets the preferred size, layout, and loads images.
     */
    public BoardPanel() {
        setPreferredSize(new Dimension(900, 700));
        setLayout(new BorderLayout());
        loadImages();
    }

    /**
     * Loads the dragon image from the resources directory.
     */
    private void loadImages() {
        // Get the class loader for the current class
        ClassLoader classLoader = getClass().getClassLoader();

        // Load the image as a resource using the class loader
        ImageIcon ii = new ImageIcon(classLoader.getResource("dragon.png"));
        dragonImage = ii.getImage();
    }

    /**
     * Overrides the paintComponent method to draw the board, dragon image, and title on the panel.
     * @param g The Graphics object used for painting.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        if (dragonImage != null) {
            int imageWidth = 250;
            int imageHeight = 300;
            int boardWidth = 800;
            int boardHeight = 700;

            int x = 50 + (boardWidth - imageWidth) / 2;
            int y = 50 + (boardHeight - imageHeight) / 2;

            g.drawImage(dragonImage, x, y, imageWidth, imageHeight, this);

            drawTitle(g);

        }
    }

    /**
     * Draws the title "Fiery Dragons" at the center of the board.
     * @param g The Graphics object used for painting.
     */
    private void drawTitle(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Times", Font.BOLD, 48));
        FontMetrics fm = g.getFontMetrics();
        String title = "Fiery Dragons";
        int x = 50 + (800 - fm.stringWidth(title)) / 2; // Center the title horizontally within the board
        int y = 100;
        g.drawString(title, x, y);
    }

    /**
     * Draws the game board with a specified color.
     * @param g The Graphics object used for painting.
     */
    private void drawBoard(Graphics g) {
        // Draw the game board
        Color boardColor = new Color(0xD9, 0xD9, 0xD9);
        g.setColor(boardColor);
        g.fillRect(50, 50, 800, 700);
    }
}
