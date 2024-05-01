package game;

import javax.swing.*;
import java.awt.*;

/**
 * The main frame of the "Fiery Dragons Game", which initializes and displays the primary game interface.
 */
public class GameFrame extends JFrame {
    public GameFrame() {
        setTitle("Fiery Dragons Game");
        setSize(900, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLocationRelativeTo(null);
        setResizable(false);

        add(new game.BoardPanel(), BorderLayout.CENTER);
        add(new game.ControlPanel(this), BorderLayout.SOUTH);
        pack();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameFrame frame = new GameFrame();
            frame.setVisible(true);
        });
    }
}
