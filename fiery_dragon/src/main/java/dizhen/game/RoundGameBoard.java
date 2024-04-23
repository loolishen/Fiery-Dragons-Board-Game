/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dizhen.game;

/**
 *
 * @author DavidL
 */


import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ArrayList;

public class RoundGameBoard extends JPanel {
    private final int boardSize = 700; // Size of the game board
    private final int innerCircleSize = 400; // Size of the inner empty circle
    private final int caveSize = 100; // Size of each cave
    private final int tokenSize = 50;
    private final String imageFolderPath = "C:\\Users\\DavidL\\OneDrive\\CS\\FIT3077\\Project\\MA_Tuesday08am_Team123\\fiery_dragon\\src\\main\\resources"; // Path to the folder with images
    private ArrayList<ChitCard> chitCards; // List to hold the chit card
    private ArrayList<ChitCard> volChitCards = new ArrayList<>();
    private ArrayList<Token> tokens = Token.getAllTokens();
    private ArrayList<Cave> caves = Cave.getCaves();
    private ArrayList<ChitCard> nearestChitCards = ChitCard.getNearestChitCards();
    private ArrayList<Integer> nearestCardIndex = new ArrayList<>();
    private ChitCard.ClickCallback cardClickCallback = new ChitCard.ClickCallback() {
        @Override
        public void onCardClicked(ChitCard card) {
            // Handle the click event
            System.out.println("Card clicked: " + card);
            // Add additional logic as needed
        }
    };
    
    public RoundGameBoard() {
        setPreferredSize(new Dimension(boardSize + caveSize * 2, boardSize + caveSize * 2));
        setLayout(null); // Use null layout for absolute positioning

        // Create and add 8 segments to the board
        setupSegments();
        // Load images from the folder and add cave componenys
        setupCaves();
        
        // Initialize the list of chit cards
        chitCards = new ArrayList<>();

        // Load images from the folder and add chit cards
        setupChitCards();
        
        ChitCard.setVolChitCards(volChitCards);
        //find Closest VolChitCard ToCaves to move for the start
        findClosestVolChitCardToCaves();
        
        //tokenTracker
        int tokenTracker = 0;
        ChitCard.setCurrentTokenIndex(tokenTracker);
        
    }
    
    
    private void findClosestVolChitCardToCaves() {

        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            ChitCard closestCard = null;
            int cardIndex = 0;
            double minDistance = Double.MAX_VALUE; // Start with the largest value possible

            // Get the position of the cave
            Point tokenPosition = token.getLocation(); // Make sure your Cave class has a method to get its position
//            System.out.println(volChitCards.size());
            for (int j = 0; j < volChitCards.size(); j++) {
                ChitCard card = volChitCards.get(j);
                Point cardPosition = card.getLocation(); // Make sure your ChitCard class has a method to get its position

                // Calculate the distance between the cave and the volChitCard
                double distance = Math.sqrt(Math.pow(tokenPosition.x - cardPosition.x, 2) + Math.pow(tokenPosition.y - cardPosition.y, 2));

                // Check if this distance is the smallest we've found so far
                if (distance < minDistance) {
                    minDistance = distance;
                    closestCard = card;
                    cardIndex = j;
                }
            }

            // At this point, 'closestCard' is the nearest volChitCard to the current cave
            nearestChitCards.add(closestCard);      
            nearestCardIndex.add(cardIndex);
        }
//        System.out.println(nearestChitCards.size());
        ChitCard.setNearestChitCards(nearestChitCards);
        //Indices of the cloese chit card to each token/cave in the
        //original volcano lists
        ChitCard.setNearestCardIndex(nearestCardIndex);
        
    }

    
    private void setupChitCards() {
        int numberOfChitCards = 16; // Define the number of chit cards
        int numberOfVolcano = 3 * 8;
        double angleStep = 360.0 / numberOfChitCards; // Calculate the angle step
        double volStep = 360.0 / numberOfVolcano;
        int radius = (innerCircleSize - tokenSize) / 2; // Calculate the radius
        //chitcards on Volcano 
        int volRadius = (innerCircleSize+100) / 2;
        //Movement of tokens next to chitcards on Volcano
        int tokenRadius = (innerCircleSize+200) / 2;

        String chitPath = imageFolderPath + "\\chitcards";
        File[] imageFiles = loadImageFiles(chitPath);
        String bachChitPath = imageFolderPath + "\\chitback";
        File[] chitBack = loadImageFiles(bachChitPath);
        

        //randomised positioning of the chit cards
        ArrayList<Double> angles = new ArrayList<>();
        for (int i = 0; i < numberOfChitCards; i++){
            angles.add(i*angleStep);
        }
        Collections.shuffle(angles);
        
        String frontImagePath = chitBack[0].getAbsolutePath();
        ImageNameExtractor extractor = new ImageNameExtractor();
        
        for (int i = 0; i < numberOfChitCards; i++) {   
            Point position = circularPos(angles.get(i), radius); // Get the position for the current card

            String backImagePath = imageFiles[i].getAbsolutePath();
            String imageName = imageFiles[i].getName(); // Get the image name
            ImageNameExtractor.Result result = extractor.extractNameAndNumber(imageName);
            imageName = result.getName();
            int imageId = result.getId();
            
            if (imageId > 1000){
                imageId -= 1000;
            }

            ChitCard cc= new ChitCard(frontImagePath, backImagePath, cardClickCallback);
            cc.setImageName(imageName);
            cc.setImageId(imageId);
            
            cc.setBounds(position.x, position.y, cc.getPreferredSize().width, cc.getPreferredSize().height);
            
            chitCards.add(cc);
            add(cc);
            setComponentZOrder(cc, 0);
        }
        
        //Set chitCards on volcano
        String volImaPath = imageFolderPath + "\\volcano";
        File[] chitVol = loadImageFiles(volImaPath);
                // Create and add 8 segments to the board
          
        for (int i = 0; i < 8; i++) {
            
            //Bind volcano components to each segment
            for (int j = 0; j < 3; j++){
                int actCount = 3 * i + j;
                Point position = circularPos(volStep*actCount, volRadius); // Get the position for the current card

                String backImagePath = chitVol[(i+j)%chitVol.length].getAbsolutePath();

                ChitCard cc= new ChitCard(backImagePath, backImagePath, cardClickCallback);

                cc.setBounds(position.x, position.y, cc.getPreferredSize().width, cc.getPreferredSize().height);
                add(cc);
                setComponentZOrder(cc, 0);
                volChitCards.add(cc);
                

            }
        }
    }
        //______________________________
    //Segment Component
    private void setupSegments() {
        // Create and add 8 segments to the board
        int startAngle = 360 / (3*8);
        for (int i = 0; i < 8; i++) {
            //Color segmentColor = (i % 2 == 0) ? Color.RED : Color.BLUE;
            // segment = new SegmentComponent(startAngle, boardSize, innerCircleSize);
            //segment.setBounds(caveSize, caveSize, boardSize, boardSize); // Position inside the caves area
            //add(segment);
//            String volImaPath = imageFolderPath + "\\volcano";
//            File[] imageFiles = loadImageFiles(volImaPath);
         
            //Bind volcano components to each segment
            for (int j = 0; j < 3; j++){
                //Color volcanoColor = (j % 2 == 0) ? Color.RED : Color.BLUE;
                //each vocalno take 1/3 spaces of segment
                int actCount = i * 3 + j;
                int volAngle = startAngle * actCount;

//                //System.out.println(imageFiles != null);
//                if (imageFiles != null){
//                    //loop through concurrently make sure every volcano can be start and end
//                    String actImagePath = imageFiles[(i+j)%imageFiles.length].getAbsolutePath();
                Tile volcomp = new Tile(volAngle, boardSize, innerCircleSize);
                //segment.setSubComponent(volcomp, j);
                //set bound to offset from top-left boundary from frame
                //to leave space for cave
                //boardSize for coverage
                volcomp.setBounds(caveSize, caveSize, boardSize, boardSize);
                add(volcomp);
                
//                }
                
            }
        }
    }


    private Point circularPos(double ranAngle, int radius) {
        double angle = Math.toRadians(ranAngle); // Convert angle to radians
        int x = (int) (Math.cos(angle) * radius + boardSize / 2 + caveSize - tokenSize / 2);
        int y = (int) (Math.sin(angle) * radius + boardSize / 2 + caveSize - tokenSize / 2);
        return new Point(x, y); // Return the calculated position as a Point
    }

    
    private File[] loadImageFiles(String folderPath) {
        File dir = new File(folderPath);
        return dir.listFiles((File dir1, String name) -> 
            name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png"));
    }
    //________________________________
    //Cave COmponents
    private void setupCaves() {
        String caveImaPath = imageFolderPath+ "\\cave";
        File[] imageFiles = loadImageFiles(caveImaPath);
        if (imageFiles != null && imageFiles.length >= 4) {
            createAndPositionCaves(imageFiles);
        } else {
            System.out.println("Not enough images in the folder to set to the caves.");
        }
    }

    private void createAndPositionCaves(File[] imageFiles) {
        ImageNameExtractor extractor = new ImageNameExtractor();
            
        for (int i = 0; i < imageFiles.length; i++) {
            int[] xy = startPos(i);
            //token align with cave
            int centerOffset = (caveSize - tokenSize)/2;
            Token token = new Token(tokenSize, imageFiles[i].getAbsolutePath());
            String imageName = imageFiles[i].getName(); // Get the image name
            ImageNameExtractor.Result result = extractor.extractNameAndNumber(imageName);
            imageName = result.getName();
            int imageId = result.getId();
            
            if (imageId > 1000){
                imageId -= 1000;
            }
//            token.setImageName(imageName);
            token.setBounds(xy[0]+centerOffset, xy[1]+centerOffset, tokenSize, tokenSize);
            this.add(token);
            tokens.add(token);
            token.setImageName(imageName);
            token.setImageId(imageId);
            Cave cave = new Cave(caveSize, imageFiles[i].getAbsolutePath());
            cave.setBounds(xy[0], xy[1], caveSize, caveSize);
            this.add(cave);
            caves.add(cave);

        }
    }

    private int[] startPos(int index) {
        int[] xy = new int[2];
        int x = (index % 2 == 0) ? 100 : boardSize - caveSize + 100;
        xy[0] = x;
        int y = (index < 2) ? 100 : boardSize - caveSize + 100;
        xy[1] = y;
  
        return xy;
        
    }
   
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw the outer circle (game board)
        Ellipse2D.Double outerCircle = new Ellipse2D.Double(caveSize, caveSize, boardSize, boardSize);
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fill(outerCircle);

        // Draw the inner circle (empty space)
        int offset = (boardSize - innerCircleSize) / 2 + caveSize;
        Ellipse2D.Double innerCircle = new Ellipse2D.Double(offset, offset, innerCircleSize, innerCircleSize);
        g2d.setColor(getBackground());
        g2d.fill(innerCircle);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Round Game Board");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(new RoundGameBoard());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

