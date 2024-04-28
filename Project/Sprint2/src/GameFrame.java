import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    public GameFrame() {
        setTitle("Fiery Dragons Game");
        setSize(700, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        add(new BoardPanel(), BorderLayout.CENTER);
        add(new ControlPanel(this), BorderLayout.SOUTH);

    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            GameFrame frame = new GameFrame();
            frame.setVisible(true);
        });
    }
}
