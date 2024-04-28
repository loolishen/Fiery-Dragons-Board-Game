/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dizhen.game;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JPanel;

public class ChitCard extends JPanel {
    private final Image frontImage;
    private final Image backImage;
    private boolean isFrontVisible;
    private String imageName;
    private int imageId;
    
    //class level variable
    private static ChitCard lastClickedCard;
    private static ArrayList<Token> allTokens = Token.getAllTokens();
    private static ArrayList<ChitCard> nearestChitCards = new ArrayList<>();
    private static ArrayList<Integer> nearestCardIndex = new ArrayList<>();
    private static ArrayList<Integer> startChitIndex = new ArrayList<>();
    private static int currentTokenIndex = 0;
    private static ArrayList<ChitCard> volChitCards = new ArrayList<>();
    private static ArrayList<Cave> caves = Cave.getCaves();
    public static ArrayList<ChitCard> startChitCards = new ArrayList<>();

    // Define an interface for the click callback
    public interface ClickCallback extends dizhen.game.ClickCallback {
    }
    
    public ChitCard(String frontImagePath, String backImagePath, ClickCallback clickCallback) {
        // Load the images for both sides of the card
        frontImage = Toolkit.getDefaultToolkit().getImage(frontImagePath);
        backImage = Toolkit.getDefaultToolkit().getImage(backImagePath);
        // Set the preferred size if necessary
        setPreferredSize(new Dimension(50, 50));
        //make drawing transparent
        setOpaque(false);

        // Initially, the front image is visible
        isFrontVisible = true;
        // 使用Collections.nCopies()创建一个包含四个0的列表
        startChitIndex = new ArrayList<>(Collections.nCopies(4, 0));

        // Add a mouse listener to flip the card when clicked
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ChitCard.lastClickedCard = ChitCard.this;
                flipCard();
                
                // Notify other parts of the program that the card was clicked
                if (clickCallback != null) {
                    clickCallback.onCardClicked(ChitCard.this);
                }
//                System.out.println(allTokens.size());
//                System.out.println(nearestChitCards.size());
//                System.out.println(caves.size());
                Cave cave = caves.get(currentTokenIndex);
                Point caveLocation = cave.getLocation();
                Point tokenLocation = allTokens.get(currentTokenIndex).getLocation();
                double distance = Math.sqrt(Math.pow(caveLocation.x - tokenLocation.x, 2) + Math.pow(caveLocation.y - tokenLocation.y, 2));
                double offsetDist = distance + cave.getRoundSize()/2;
                //look which token in turn
                //move it to the the cloest chitcard when first time the chitcard align
                //with the token card in its turn
                //If the token is still in cave


                Token turnToken = allTokens.get(currentTokenIndex);
                if (offsetDist < cave.getRoundSize()){
                   
                    if (turnToken.getImageName().equals(lastClickedCard.getImageName())){
                        
                        turnToken.setLocation(nearestChitCards.get(currentTokenIndex).getLocation());
                        startChitIndex.set(currentTokenIndex, nearestCardIndex.get(currentTokenIndex));
                                // Print out the new location of the token

                        System.out.println("Token moved to: " + turnToken.getLocation());
                    }


                }
                //for rest of rounds for to the number of animals on the picture
                else{

                    int currentIndex = nearestCardIndex.get(currentTokenIndex);
                    int stepMove = ChitCard.lastClickedCard.getImageId();
                    if (turnToken.getImageName().equals(lastClickedCard.getImageName())){
                        //get the current index of the chitcard when token stands on

                        int updatedIndex = currentIndex + stepMove;
                        updatedIndex = updatedIndex % volChitCards.size();
                        //update the nearestCardIndex  correspondingly
                        nearestCardIndex.set(currentTokenIndex, updatedIndex);
                        ChitCard targetCard = volChitCards.get(updatedIndex);
                        turnToken.setLocation(targetCard.getLocation());
                    }

                    else if (lastClickedCard.getImageName().equals("skull")){
                        int updatedIndex = currentIndex - stepMove;
                        if (updatedIndex <= startChitIndex.get(currentTokenIndex)) {
                            turnToken.setLocation(
                                volChitCards.get(startChitIndex.get(currentTokenIndex))
                                    .getLocation());
                        }
                        else{
                            updatedIndex = updatedIndex % volChitCards.size();
                            nearestCardIndex.set(currentTokenIndex, updatedIndex);
                            ChitCard targetCard = volChitCards.get(updatedIndex);
                            turnToken.setLocation(targetCard.getLocation());
                        }
                    }


                }
                
                // Ensure the GUI is updated to reflect the new token location
                // This assumes there is a method in Token class to get the component that needs repainting
                turnToken.repaint();
                // Get the parent container of the token
                Container parent = turnToken.getParent();
                parent.setComponentZOrder(turnToken, 0);
                //update the for next token turn concurrently
                currentTokenIndex = currentTokenIndex + 1;
                currentTokenIndex = currentTokenIndex%allTokens.size();
                
                
            }
        });
    }

    public static ArrayList<ChitCard> getVolChitCards() {
        return volChitCards;
    }

    public static void setVolChitCards(ArrayList<ChitCard> volChitCards) {
        ChitCard.volChitCards = volChitCards;
    }

    public static ArrayList<Integer> getNearestCardIndex() {
        return nearestCardIndex;
    }

    public static void setNearestCardIndex(ArrayList<Integer> nearestCardIndex) {
        ChitCard.nearestCardIndex = nearestCardIndex;
    }

    public static int getCurrentTokenIndex() {
        return currentTokenIndex;
    }

    public static void setCurrentTokenIndex(int currentTokenIndex) {
        ChitCard.currentTokenIndex = currentTokenIndex;
    }
   
    
    public static ArrayList<ChitCard> getNearestChitCards() {
        return nearestChitCards;
    }

    public static void setNearestChitCards(ArrayList<ChitCard> nearestChitCards) {
        ChitCard.nearestChitCards = nearestChitCards;
    }
    
    // Method to flip the card
    public void flipCard() {
        isFrontVisible = !isFrontVisible;
        repaint(); // Repaint the component to update the visible side
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Create a circular clip
        Graphics2D g2d = (Graphics2D) g.create();
        Ellipse2D circle = new Ellipse2D.Double(0, 0, getWidth(), getHeight());
        g2d.setClip(circle);
        

        // Draw the visible side of the card within the circular clip
        if (isFrontVisible) {
            g2d.drawImage(frontImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g2d.drawImage(backImage, 0, 0, getWidth(), getHeight(), this);
        }
        g2d.dispose();
    }

    // Getters and setters
    public boolean isFrontVisible() {
        return isFrontVisible;
    }

    public void setFrontVisible(boolean frontVisible) {
        isFrontVisible = frontVisible;
        repaint();
    }
    
    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
    public String getImageName() {
        return this.imageName;
    }
    
    public String setImageName(String name) {
        return this.imageName = name;
    }
}


