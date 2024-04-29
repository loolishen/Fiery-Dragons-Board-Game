package main.game;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {
    JFrame parentFrame; // Reference to the main application window

    public ControlPanel(JFrame parent) {
        this.parentFrame = parent;

        JButton btnSinglePlayer = new JButton("Single Player");
        btnSinglePlayer.addActionListener(e -> switchPanel(new SingleModePanel(parentFrame)));
        add(btnSinglePlayer);

        JButton btnPlayFriends = new JButton("Play with Friends");
        btnPlayFriends.addActionListener(e -> switchPanel(new MultiModePanel()));
        add(btnPlayFriends);
    }
    private void switchPanel(JPanel newPanel) {
        parentFrame.getContentPane().removeAll();
        parentFrame.getContentPane().add(newPanel, BorderLayout.CENTER);
        parentFrame.pack(); // repack after switch
        parentFrame.revalidate();
        parentFrame.repaint();
    }

}
