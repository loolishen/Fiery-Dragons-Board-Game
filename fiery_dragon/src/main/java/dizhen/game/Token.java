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
import java.awt.event.*;
import java.util.ArrayList;

public class Token extends RoundComponent implements Draggable {
    // Variables to hold the x and y coordinates relative to the token component
    private int mouseX;
    private int mouseY;
    
    private static Token turnToken;
    private static ArrayList<Token> allTokens = new ArrayList<>();
   
    public Token(int size, String imagePath) {
        super(size, imagePath);
        
        
        // Add mouse listener to detect drag events
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                onPress(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                onRelease(e);
            }
        });

        // Add mouse motion listener to handle the drag
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                onDrag(e);
            }
        });
    }


    public static ArrayList<Token> getAllTokens() {
        return allTokens;
    }

    public static Token getTurnToken() {
        return turnToken;
    }

    public static void setTurnToken(Token turnToken) {
        Token.turnToken = turnToken;
    }

    // Implement the Draggable interface methods
    @Override
    public void onDrag(MouseEvent e) {
        // Calculate the new position of the token
        int deltaX = e.getX() - mouseX;
        int deltaY = e.getY() - mouseY;
        Point currentLocation = getLocation();
        setLocation(currentLocation.x + deltaX, currentLocation.y + deltaY);
    }

    @Override
    public void onPress(MouseEvent e) {
        // When the mouse is pressed, update the relative coordinates
        mouseX = e.getX();
        mouseY = e.getY();
        // Bring the token to the top when clicked
        Container parent = Token.this.getParent();
        if (parent != null) {
            parent.setComponentZOrder(Token.this, 0);
            parent.repaint();
        }
    }

    @Override
    public void onRelease(MouseEvent e) {
        // When the mouse is released, ensure the token remains on top
        Container parent = Token.this.getParent();
        if (parent != null) {
            parent.setComponentZOrder(Token.this, 0);
            parent.repaint();
        }
    }
}