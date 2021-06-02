package com.project.client.gui.newgame;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.PrintWriter;

public class SettingsFrame extends JFrame {
    SettingsPanel settingsPanel;
    PrintWriter out;
    BufferedReader in;
    boolean mainPlayer;
    String playerName;
    String opponentName;

    public SettingsFrame(PrintWriter out, BufferedReader in, String playerName, String opponentName, boolean mainPlayer) {
        super("GoClient");
        this.out = out;
        this.in = in;
        this.mainPlayer = mainPlayer;
        this.playerName = playerName;
        this.opponentName = opponentName;
        init();
    }

    private void init() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //create the components
        settingsPanel = new SettingsPanel(this, out, in, playerName, opponentName, mainPlayer);

        add(settingsPanel);

        setPreferredSize(new Dimension(1000, 900));
        setResizable(false);

        //invoke the layout manager
        pack();
    }
}

