package com.project.client.gui.score;

import javax.swing.*;
import java.awt.*;

import static java.awt.BorderLayout.CENTER;

public class ScoreFrame extends JFrame {
    ScorePanel scorePanel;
    String winnerName;

    public ScoreFrame(String winnerName) {
        super("GoClient");
        this.winnerName = winnerName;
        init();
    }

    private void init() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(600, 500));
        setResizable(false);
        //create the components
        scorePanel = new ScorePanel(this);

        //JFrame uses a BorderLayout by default
        add(scorePanel, CENTER);
        //invoke the layout manager
        pack();
        setLocationRelativeTo(null);
    }
}
