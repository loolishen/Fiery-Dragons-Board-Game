/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dizhen.game;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import javax.swing.JComponent;

class RoundComponent extends JComponent {
    private final int size;
    private Image image;
    private String imageName;
    private int imageId;

    public RoundComponent(int size, String imagePath) {
    this.size = size;
        setOpaque(false);
        image = Toolkit.getDefaultToolkit().getImage(imagePath);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Create a circular clip
        Ellipse2D circle = new Ellipse2D.Double(0, 0, size, size);
        g2d.setClip(circle);

        // Draw the image within the circular clip
        //create the image at 0,0 and paint it before repoisitoning it
        g2d.drawImage(image, 0, 0, size, size, this);
        g2d.dispose();
    }
    
    public String getImageName() {
        return this.imageName;
    }
    
    public String setImageName(String name) {
        return this.imageName = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
   
        // Method to get the size of the cave
    public int getRoundSize() {
        return this.size; // Accessing the 'size' field from RoundComponent
    }
   

}
