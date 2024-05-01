package main.game;

import org.junit.Test;
import static org.junit.Assert.*;

public class GameTests {

    @Test
    public void testCardCreation() {
        Card card = new Card("dragon.png");
        assertNotNull("Card object should not be null", card);
        assertEquals("Image name should match", "dragon.png", card.getImageName());
    }

    @Test
    public void testBoardPanelCreation() {
        BoardPanel panel = new BoardPanel();
        assertNotNull("BoardPanel object should not be null", panel);
        // You might want to add assertions that check if the panel's properties like size and layout are correctly set
        assertEquals("Width should be 900", 900, panel.getPreferredSize().width);
        assertEquals("Height should be 700", 700, panel.getPreferredSize().height);
    }

    // Removing the mockGraphics test as it requires Mockito
    // Instead, add tests that check logical conditions or setup properties that do not involve Graphics directly.

    @Test
    public void testChitCardFlipping() {
        ChitCard card = new ChitCard(new Card("dragon.png"));
        assertFalse("Initially, card should not be flipped", card.isFlipped());
        card.setFlipped(true);
        assertTrue("Card should be flipped", card.isFlipped());
        card.unflip();
        assertFalse("Card should not be flipped after unflipping", card.isFlipped());
    }

    @Test
    public void testSingletonBehavior() {
        ChitCard firstCard = new ChitCard(new Card("first.png"));
        ChitCard secondCard = new ChitCard(new Card("second.png"));

        ChitCardFlipManager.flipCard(firstCard);
        assertTrue("First card should be flipped", firstCard.isFlipped());

        ChitCardFlipManager.flipCard(secondCard);
        assertTrue("Second card should be flipped", secondCard.isFlipped());
        assertFalse("First card should no longer be flipped", firstCard.isFlipped());
    }
}
