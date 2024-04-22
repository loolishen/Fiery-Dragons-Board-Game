/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// Define the package for this class as part of the 'dizhen.game' namespace.
package dizhen.game;

// Import the Swing library for building graphical user interfaces.
import javax.swing.*;
// Import the AWT (Abstract Window Toolkit) library for windowing, layout managers, and event handling.
import java.awt.*;
// Import the AWT Geom library for geometric shapes.
import java.awt.geom.*;
import java.io.File;

// Declare the Tile class which extends the BoardComponent class.
public class Tile extends BoardComponent {
    // Declare an Image object to hold the volcano image. It is final because it won't change once set.
    private Image volcanoImage;

    // Constructor for Tile.
    public Tile(int startAngle, int outerDiameter, int innerDiameter) {
        // Call the constructor of the superclass (BoardComponent) with the provided parameters.
        super(startAngle, outerDiameter, innerDiameter, 15); // Set arcAngle to 15 degrees
        // Make the component non-opaque, which means it will not paint every pixel within its bounds.
        setOpaque(false);
        
    }

    // Override the paintComponent method from the JComponent class.
    @Override
    protected void paintComponent(Graphics g) {
        // Call the superclass's paintComponent method to ensure proper painting of the component.
        super.paintComponent(g);
        // Create a Graphics2D object from the Graphics object to allow for advanced operations.
        Graphics2D g2d = (Graphics2D) g.create();

        // Calculate the center of the component for positioning the image and arc.
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // Create a clipping area that matches the arc of the volcano segment.
        Area clippingArea = new Area(new Arc2D.Double(centerX - outerDiameter / 2, centerY - outerDiameter / 2, outerDiameter, outerDiameter, startAngle, arcAngle, Arc2D.PIE));

        // Create a clipping area that excludes the inner circle to create a donut shape.
        Area innerCircle = new Area(new Ellipse2D.Double(centerX - innerDiameter / 2, centerY - innerDiameter / 2, innerDiameter, innerDiameter));
        // Subtract the inner circle from the clipping area to get the final shape for the arc segment.
        clippingArea.subtract(innerCircle);

        // Apply the clipping area to the Graphics2D object to restrict drawing to this area.
        g2d.setClip(clippingArea);

        // Draw the volcano image within the clipping area.
        if (volcanoImage != null){
            g2d.drawImage(volcanoImage, centerX - outerDiameter / 2, centerY - outerDiameter / 2, outerDiameter, outerDiameter, this);
        }

        // Dispose of the Graphics2D object to release system resources and clean up.
        g2d.dispose();
    }

    public Image getVolcanoImage() {
        return volcanoImage;
    }

    public void setVolcanoImage(String imagePath) {
        // Load the image for the volcano using the provided path.
        volcanoImage = Toolkit.getDefaultToolkit().getImage(imagePath);
        this.volcanoImage = volcanoImage;
    }
   

}
