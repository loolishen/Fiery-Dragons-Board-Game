/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dizhen.game;

/**
 *
 * @author DavidL
 */

import java.awt.*;
import java.awt.event.*;
public interface Draggable {
    void onDrag(MouseEvent e);
    void onPress(MouseEvent e);
    void onRelease(MouseEvent e);
    
}
