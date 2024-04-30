package main.game;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the Board Panel for the main starting frame.
 */
public class BoardPanel extends JPanel {
    private Image dragonImage;


    public BoardPanel() {
        setPreferredSize(new Dimension(900, 700));
        setLayout(new BorderLayout());
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

    private void drawTitle(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Times", Font.BOLD, 48));
        FontMetrics fm = g.getFontMetrics();
        String title = "Fiery Dragons";
        int x = 50 + (800 - fm.stringWidth(title)) / 2; // Center the title horizontally within the board
        int y = 100;
        g.drawString(title, x, y);
    }

    private void drawBoard(Graphics g) {
        // Draw the game board
        Color boardColor = new Color(0xD9, 0xD9, 0xD9);
        g.setColor(boardColor);
        g.fillRect(50, 50, 800, 700);
    }
}
