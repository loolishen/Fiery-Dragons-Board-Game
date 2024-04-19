/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dizhen.game;

import java.util.ArrayList;

/**
 *
 * @author DavidL
 */
public class Cave extends RoundComponent{
    private static ArrayList<Cave> caves = new ArrayList<>();
    
    
    public Cave(int size, String imagePath) {
        super(size, imagePath);
    }

    public static ArrayList<Cave> getCaves() {
        return caves;
    }

    public static void setCaves(ArrayList<Cave> caves) {
        Cave.caves = caves;
    }
    

}
