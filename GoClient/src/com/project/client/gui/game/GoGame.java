package com.project.client.gui.game;

import com.project.client.GoClient;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.PrintWriter;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.EAST;

public class GoGame extends JFrame {
    GoBoard board;
    GoInfo infoPanel;
    int size;
    PrintWriter out;
    BufferedReader in;
    boolean mainPlayer;
    String playerName;
    String opponentName;
    String colour;

    public GoGame(PrintWriter out, BufferedReader in, String playerName,
                  String opponentName, int size, String colour, boolean mainPlayer) {
        super("GoClient");
        this.out = out;
        this.in = in;
        this.mainPlayer = mainPlayer;
        this.playerName = playerName;
        this.opponentName = opponentName;
        this.size = size;
        this.colour = colour;
        init();
    }

    private void init() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        //create the components
        board = new GoBoard(this, out, in);
        infoPanel = new GoInfo(this, out, in, playerName, opponentName, colour, mainPlayer);


        //JFrame uses a BorderLayout by default
        add(board, CENTER);
        add(infoPanel, EAST);
        //invoke the layout manager
        pack();
        //set second player to the right
        if (!mainPlayer) {
            GoClient.setLocationToTopRight(this);
        }
    }
}
