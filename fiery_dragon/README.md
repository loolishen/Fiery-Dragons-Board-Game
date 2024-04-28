# Dizhen Game Project
Dizhen Game is a strategic board game that involves elements of chance and skill. The game is developed using Java and utilizes the Swing framework for its graphical user interface.

## Key Components

### BoardComponent
- A base class for all components on the game board.
- Manages size, angle, and painting of the board components.

### Tile
- Represents a tile on the game board.
- Manages a tile image and draws it within a specific arc segment.

### RoundComponent
- Represents a round element, such as a token or a cave.
- Manages an image and draws it within a circular clip.

### RoundGameBoard
- The main JPanel that holds and manages the entire game board.
- Sets up segments, caves, and chit cards on the board.
- Handles the positioning and interaction of tokens and chit cards.

### Token
- Represents a game piece that can be moved on the board.
- Implements the `Draggable` interface to allow for mouse-based movement.

### Volcano
- Represents a segment of the game board with a specific arc angle.
- Manages sub-components for each segment of the volcano.

### BoardComponent
- A generic component for the game board that handles painting and basic properties.

### ClickCallback Interface
- Defines a callback for when a chit card is clicked.

### Cave
- Represents a cave on the game board.
- Manages a list of all caves and their properties.

### Draggable Interface
- An interface that outlines methods for drag-and-drop functionality.

### ChitCard
- Represents a playing card in the game.
- Manages the front and back images and allows for flipping between them.

### ExtensiveTestCases
- Contains unit tests for various components of the game.

### ImageNameExtractor
- A utility class to extract the name and ID from an image file name.

## How to Run with executable JAR file
1. Double left-click on the Fiery_Dragon.jar file to run the game.
2. refers to the Design Rational summission document for more information.

## How to Run with source code
2. Ensure you have Java installed on your system.
3. Compile the Java files.
4. Run the `RoundGameBoard` class as the main class to start the game.

## Dependencies
- Java Swing for the graphical user interface.
- Java AWT and AWT Geom for geometric shapes and windowing.

## License
Please refer to the `license-default.txt` for licensing information.

## Contributing
If you wish to contribute to the project, please follow the standard Fork and Pull Request workflow. Ensure that your changes are well documented and do not break existing functionality.

## Authors
- DavidL (Project Lead)

## Acknowledgments
- Inspiration, some code snippets and all the used images are sourced and generated with windows 
- designer and chatgpt.