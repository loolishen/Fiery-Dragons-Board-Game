package main.game;

import javax.swing.*;

public class ChitCardFlipManager {
    private static ChitCard currentlyFlipped = null;

    // Ensures that only one card can be flipped at a time
    public static void flipCard(ChitCard card) {
        if (card.isFlipped()) {
            return; // If the card is already flipped, do nothing
        }
        if (currentlyFlipped != null && currentlyFlipped != card) {
            currentlyFlipped.unflip();  // Unflip the currently flipped card
        }
        card.setFlipped(true);  // Flip the current card
        currentlyFlipped = card;  // Update the currently flipped card reference
    }

    public static void unflipAll() {
        if (currentlyFlipped != null) {
            currentlyFlipped.unflip();
            currentlyFlipped = null;
        }
    }
}
