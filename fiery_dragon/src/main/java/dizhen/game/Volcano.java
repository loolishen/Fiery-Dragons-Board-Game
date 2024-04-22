/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dizhen.game;

import java.awt.*;

/**
 *
 * @author DavidL
 */
public class Volcano extends BoardComponent{
    //Segment degreee
    //for the Segment it has to be 360/8 = 45 degree
    //since 8 segments
    private static int arcAngle = 360/8;
    //store 3 sub volcanoComponent for each segment component
    private Tile[] subComponentArr = new Tile[3];
    public Volcano(int startAngle, int outerDiameter, int innerDiameter){
        super(startAngle, outerDiameter, innerDiameter, arcAngle);
        
        //Since the volcano components in segment are fixedd
        //Just Segment -> 3 vocalno component
    }
    
    public Tile[] getSubComponent() {
        return subComponentArr;
    }

    // Setter method
    public void setSubComponent(Tile subComponent, int index) {
        this.subComponentArr[index] = subComponent;
    }
}
