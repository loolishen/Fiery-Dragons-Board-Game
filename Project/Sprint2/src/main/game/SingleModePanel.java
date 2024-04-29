package main.game;
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
        gameCircle.setPreferredSize(new Dimension(900, 700));
        add(gameCircle, BorderLayout.CENTER);

        JButton backButton = new JButton("End Game");
        backButton.addActionListener(e -> switchToBoardPanel());
        add(backButton, BorderLayout.SOUTH);

        // After adding or removing components, revalidate and repaint.
        revalidate();
        repaint();
    }

    private void switchToBoardPanel() {
        parentFrame.getContentPane().removeAll();
        parentFrame.getContentPane().add(new BoardPanel(), BorderLayout.CENTER);
        parentFrame.pack(); // Adjust size to fit the contents
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // If you need any additional background drawing, do it here.
    }
}
