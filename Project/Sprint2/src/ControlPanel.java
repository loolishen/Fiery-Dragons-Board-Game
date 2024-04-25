import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//public class ControlPanel extends JPanel {
//    JFrame parentFrame; // Reference to the main application window
//
//    public ControlPanel(JFrame parent) {
//        this.parentFrame = parent;
//
//        JButton btnSinglePlayer = new JButton("Single Player");
//        btnSinglePlayer.addActionListener(e -> openDialog(new SingleModePanel(), "Single Player"));
//        add(btnSinglePlayer);
//
//        JButton btnPlayFriends = new JButton("Play with Friends");
//        btnPlayFriends.addActionListener(e -> openDialog(new MultiModePanel(), "Play with Friends"));
//        add(btnPlayFriends);
//    }
//
//    private void openDialog(JPanel panel, String title) {
//        JDialog dialog = new JDialog(parentFrame, title, true);
//        dialog.setLayout(new BorderLayout());
//        dialog.add(panel, BorderLayout.CENTER);
//        dialog.pack();  // Adjusts size to fit the contents
//        dialog.setLocationRelativeTo(parentFrame);
//        dialog.setVisible(true);
//    }
//}
public class ControlPanel extends JPanel {
    JFrame parentFrame; // Reference to the main application window

    public ControlPanel(JFrame parent) {
        this.parentFrame = parent;

        JButton btnSinglePlayer = new JButton("Single Player");
        btnSinglePlayer.addActionListener(e -> switchPanel(new SingleModePanel()));
        add(btnSinglePlayer);

        JButton btnPlayFriends = new JButton("Play with Friends");
        btnPlayFriends.addActionListener(e -> switchPanel(new MultiModePanel()));
        add(btnPlayFriends);
    }

    private void switchPanel(JPanel newPanel) {
        parentFrame.getContentPane().removeAll(); // Remove all current panels
        parentFrame.getContentPane().add(newPanel, BorderLayout.CENTER); // Add the new panel
        parentFrame.revalidate(); // Revalidate the frame's layout
        parentFrame.repaint(); // Repaint to update the UI
    }
}
