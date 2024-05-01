package game;

/**
 * Represents an animal enum for the volcano cards.
 */
public enum Animal {
    BABY_DRAGON("babydragon.png"),
    BAT("bat.png"),
    SALAMANDER("salamander.png"),
    SPIDER("spider.png");

    private final String imageName;

    /**
     * Constructs an Animal enum with the specified image name.
     * @param imageName The name of the image associated with the animal.
     */
    Animal(String imageName) {
        this.imageName = imageName;
    }

    /**
     * Gets the image name associated with the animal.
     * @return The image name.
     */
    public String getImageName() {
        return imageName;
    }
}
