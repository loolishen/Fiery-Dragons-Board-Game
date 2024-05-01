package main.game;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the game panel for single-player mode, containing the main game interface and a button to end the game.
 */
public class SingleModePanel extends JPanel {
    private GamePanel gameCircle;
    private JFrame parentFrame;

    public SingleModePanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());
        initUI();
    }

    private void initUI() {
        gameCircle = new GamePanel();
        gameCircle.setPreferredSize(new Dimension(900, 700));
        add(gameCircle, BorderLayout.CENTER);

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
