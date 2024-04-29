package main.game;


import javax.swing.*;

public class Card extends JButton {
    private ImageIcon faceUpIcon;
    private ImageIcon faceDownIcon;
    private boolean isFaceUp;

    public Card(ImageIcon faceUp, ImageIcon faceDown) {
        this.faceUpIcon = faceUp;
        this.faceDownIcon = faceDown;
        this.isFaceUp = false; // Cards start face down
        setIcon(faceDownIcon);
    }

    public void flipCard() {
        isFaceUp = !isFaceUp;
        setIcon(isFaceUp ? faceUpIcon : faceDownIcon);
    }
}

