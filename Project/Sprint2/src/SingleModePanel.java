import javax.swing.*;
import java.awt.*;

public class SingleModePanel extends JPanel {
    private CircularGamePanel gameCircle;
    private JFrame parentFrame;

    public SingleModePanel(JFrame parentFrame) {
        this.parentFrame = parentFrame; // Store the reference
        setLayout(new BorderLayout()); // Use BorderLayout to manage components
        initUI();
    }


    private void initUI() {
        gameCircle = new CircularGamePanel();
        add(gameCircle, BorderLayout.CENTER); // Add the circular game panel at the center

        // Adjust the size and position of the gameCircle if needed
        gameCircle.setBounds(50, 50, 300, 300);
        JButton backButton = new JButton("End Game");
        backButton.addActionListener(e -> switchToBoardPanel());
        add(backButton, BorderLayout.SOUTH); // Add the button at the bottom

    }
    private void switchToBoardPanel() {
        parentFrame.getContentPane().removeAll(); // Remove all components
        parentFrame.getContentPane().add(new BoardPanel(), BorderLayout.CENTER); // Add the BoardPanel back
        parentFrame.revalidate();
        parentFrame.repaint();
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        drawTitle(g);
    }


    private void drawTitle(Graphics g) {
        g.setColor(Color.BLACK); // Choose a color that stands out
        g.setFont(new Font("Serif", Font.BOLD, 28)); // Set font size and style
        FontMetrics fm = g.getFontMetrics();
        String title = "Single Player Mode"; // Adjust the title for single player mode
        int x = 50 + (600 - fm.stringWidth(title)) / 2; // Center the title horizontally within the board
        int y = 100; // Position the title vertically within the board
        g.drawString(title, x, y);
    }

    private void drawBoard(Graphics g) {
        // Draw the game board
        Color boardColor = new Color(0xD9, 0xD9, 0xD9);
        g.setColor(boardColor);
        g.fillRect(50, 50, 600, 700); // Draw the rectangle as board
    }
}
