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

class BoardComponent extends JComponent {
    protected final int startAngle;
    protected final int arcAngle;
    protected final int outerDiameter;
    protected final int innerDiameter;
    private String imageName;
    private int imageId;

    public BoardComponent(int startAngle, int outerDiameter, int innerDiameter, int arcAngle) {
        this.startAngle = startAngle;
        this.outerDiameter = outerDiameter;
        this.innerDiameter = innerDiameter;
        this.arcAngle = arcAngle;
        
        setOpaque(false); // Make the component transparent
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Enable anti-aliasing for smoother edges
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Calculate the size and position of the segment
        int x = (getWidth() - outerDiameter) / 2;
        int y = (getHeight() - outerDiameter) / 2;
        int innerRadius = innerDiameter / 2;
        int outerRadius = outerDiameter / 2;

        // Create a clipping area that excludes the inner circle
        Area clippingArea = new Area(new Ellipse2D.Double(0, 0, getWidth(), getHeight()));
        Area innerCircle = new Area(new Ellipse2D.Double(x + (outerRadius - innerRadius), y + (outerRadius - innerRadius), innerDiameter, innerDiameter));
        clippingArea.subtract(innerCircle);

        // Set the clipping area to the graphics object
        g2d.setClip(clippingArea);

        // Draw the segment
        Arc2D.Float arc = new Arc2D.Float(x, y, outerDiameter, outerDiameter, startAngle, arcAngle, Arc2D.PIE);
        //g2d.setColor(segmentColor); // Uncomment and set the desired color
        g2d.fill(arc);

        // Reset the clipping area to draw the white inner circle
        //like a switch, switch to all component to paint
        g2d.setClip(null);

        // Draw the white inner circle on top of the segments
        g2d.setColor(Color.WHITE);
        g2d.fillOval(x + (outerRadius - innerRadius), y + (outerRadius - innerRadius), innerDiameter, innerDiameter);

        g2d.dispose();
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



