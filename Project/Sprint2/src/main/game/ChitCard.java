package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

/**
 * Represents an interactive game card that can be flipped to reveal an image, with visual and interactive properties tailored for the game's UI.
 */
public class ChitCard extends JLabel {
    private Card card;
    private boolean isFlipped;
    private static ChitCard currentlyFlippedCard;

    /**
     * Constructs a new ChitCard with the specified card image.
     */
    public ChitCard(Card card) {
        this.card = card;
        this.isFlipped = false;
        setPreferredSize(new Dimension(60, 60));
        setBackground(Color.ORANGE);
        setOpaque(false);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.ORANGE),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (!isFlipped && currentlyFlippedCard == null) {
                    setFlipped(true);
                    currentlyFlippedCard = ChitCard.this;
                } else if (isFlipped && currentlyFlippedCard == ChitCard.this) {
                    unflip();
                    currentlyFlippedCard = null;
                }
            }
        });
    }

    /**
     * Checks if the card is currently flipped.
     */
    public boolean isFlipped() {
        return isFlipped;
    }

    /**
     * Sets the flipped state of the card.
     */
    public void setFlipped(boolean flipped) {
        this.isFlipped = flipped;
        repaint();
    }

    /**
     * Unflips the card, resetting its flipped state to false.
     */
    public void unflip() {
        setFlipped(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Shape clip = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
        g2.setClip(clip);
        g2.setColor(getBackground());
        g2.fill(clip);

        if (isFlipped) {
            ImageIcon icon = new ImageIcon(getClass().getResource("/" + card.getImageName()));
            g2.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }
}
