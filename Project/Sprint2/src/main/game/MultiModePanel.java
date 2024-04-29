package main.game;

import javax.swing.*;
import java.awt.*;

class MultiModePanel extends JPanel {
    public MultiModePanel() {
        setLayout(new BorderLayout());
        add(new JLabel("Play with Friends Mode"), BorderLayout.CENTER);
        // Add more components specific to Multi Player mode here
    }
}
