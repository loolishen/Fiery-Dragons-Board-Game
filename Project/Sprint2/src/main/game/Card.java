package game;

/**
 * Represents a basic card with an image.
 */
public class Card {
    private String imageName;

    /**
     * Constructs a Card object with the specified image name.
     * @param imageName The name of the image associated with the card.
     */
    public Card(String imageName) {
        this.imageName = imageName;
    }

    /**
     * Gets the image name associated with the card.
     * @return The image name.
     */
    public String getImageName() {
        return imageName;
    }
}