package main.game;

import javax.swing.*;
import java.awt.*;


public class BoardPanel extends JPanel {
    private Image dragonImage;


    public BoardPanel() {
        setPreferredSize(new Dimension(900, 700));
        setLayout(new BorderLayout()); // Use BorderLayout to manage components
        loadImages();
    }

    private void loadImages() {
        ImageIcon ii = new ImageIcon("Project/Sprint2/src/main/resources/dragon.png");
        dragonImage = ii.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        // Ensure the image scales correctly over the desired area
        if (dragonImage != null) {
            int imageWidth = 250;
            int imageHeight = 300;
            int boardWidth = 800;
            int boardHeight = 700;

            // Calculate the center position
            int x = 50 + (boardWidth - imageWidth) / 2;
            int y = 50 + (boardHeight - imageHeight) / 2;

            g.drawImage(dragonImage, x, y, imageWidth, imageHeight, this);

            drawTitle(g);

        }
    }

    private void drawTitle(Graphics g) {
        g.setColor(Color.BLACK); // Choose a color that stands out
        g.setFont(new Font("Serif", Font.BOLD, 48)); // Set font size and style
        FontMetrics fm = g.getFontMetrics();
        String title = "Fiery Dragons";
        int x = 50 + (800 - fm.stringWidth(title)) / 2; // Center the title horizontally within the board
        int y = 100; // Position the title vertically within the board
        g.drawString(title, x, y);
    }

    private void drawBoard(Graphics g) {
        // Draw the game board
        Color boardColor = new Color(0xD9, 0xD9, 0xD9);
        g.setColor(boardColor);
        g.fillRect(50, 50, 800, 700); // Draw the rectangle as board
    }
}
