package main.game;

import javax.swing.*;
import java.awt.*;

/**
 * Panel for the multiplayer mode of the game, containing the game interface for multiplayer gameplay.
 */
public class MultiModePanel extends JPanel {
    private GamePanel gamePanel;
    private JFrame parentFrame;

    public MultiModePanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());
        initUI();
    }

    private void initUI() {
        // Similar to SingleModePanel, we initialize the game panel for multiplayer mode
        gamePanel = new GamePanel();
        gamePanel.setPreferredSize(new Dimension(900, 700));
        add(gamePanel, BorderLayout.CENTER);

        JButton backButton = new JButton("End Game");
        backButton.addActionListener(e -> switchToBoardPanel());
        add(backButton, BorderLayout.SOUTH);
    }

    private void switchToBoardPanel() {
        parentFrame.getContentPane().removeAll();
        parentFrame.getContentPane().setLayout(new BorderLayout());
        parentFrame.getContentPane().add(new BoardPanel(), BorderLayout.CENTER);
        parentFrame.getContentPane().add(new ControlPanel(parentFrame), BorderLayout.SOUTH);
        parentFrame.pack();
        parentFrame.revalidate();
        parentFrame.repaint();
    }
}
