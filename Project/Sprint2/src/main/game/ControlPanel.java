package game;

import javax.swing.*;
import java.awt.*;

/**
 * Provides a user interface for navigating between different game modes in the application.
 */
public class ControlPanel extends JPanel {
    JFrame parentFrame;

    public ControlPanel(JFrame parent) {
        this.parentFrame = parent;

        JButton btnSinglePlayer = new JButton("Single Player");
        btnSinglePlayer.addActionListener(e -> switchPanel(new SingleModePanel(parentFrame)));
        add(btnSinglePlayer);

        JButton btnPlayFriends = new JButton("Play with Friends");
        btnPlayFriends.addActionListener(e -> switchPanel(new MultiModePanel(parentFrame)));
        add(btnPlayFriends);
    }

    private void switchPanel(JPanel newPanel) {
        parentFrame.getContentPane().removeAll();
        parentFrame.getContentPane().add(newPanel, BorderLayout.CENTER);
        parentFrame.pack();
        parentFrame.revalidate();
        parentFrame.repaint();
    }
}
