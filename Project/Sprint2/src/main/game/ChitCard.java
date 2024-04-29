package main.game;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;


class ChitCard extends JButton {
    private ImageIcon faceUpIcon;
    private ImageIcon faceDownIcon;
    private boolean isFaceUp = false;
    private static final int CARD_SIZE = 50; // Assuming 50 is the size you want

    public ChitCard(ImageIcon faceUpIcon, ImageIcon faceDownIcon) {
        this.faceUpIcon = faceUpIcon;
        this.faceDownIcon = faceDownIcon;

        setIcon(faceDownIcon);
        setPreferredSize(new Dimension(CARD_SIZE, CARD_SIZE));
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                flipCard();
            }
        });
    }

    private void flipCard() {
        isFaceUp = !isFaceUp;
        setIcon(isFaceUp ? faceUpIcon : faceDownIcon);
    }

    // This method is static because it does not use any instance variables of ChitCard
    public static ImageIcon createPlaceholderIcon(Color color, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(color);
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();
        return new ImageIcon(image);
    }
}
