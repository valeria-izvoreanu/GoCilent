package com.project.client.gui.newgame;

import com.project.client.gui.game.GoGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.PrintWriter;

public class SettingsPanel extends JPanel {
    final SettingsFrame frame;
    PrintWriter out;
    BufferedReader in;
    final static int W = 1000, H = 900;
    boolean mainPlayer;
    String opponentName;
    JLabel sizeLabel = new JLabel();
    JComboBox<String> sizeCombo;
    JComboBox<String> colourCombo;
    JButton startBtn = new JButton("Start");
    String playerName;

    public SettingsPanel(SettingsFrame frame, PrintWriter out, BufferedReader in, String playerName,
                         String opponentName, boolean mainPlayer) {
        this.frame = frame;
        this.out = out;
        this.in = in;
        this.mainPlayer = mainPlayer;
        this.playerName = playerName;
        this.opponentName = opponentName;
        init();
    }

    private void init() {
        setLayout(null);
        setTextLabel(new JLabel(), "Choose game settings:", 50, 250, 200);
        setTextLabel(sizeLabel, "Board Size:", 30, 250, 300);
        setBoardSize();
        setTextLabel(new JLabel(), "Choose colour:", 30, 250, 400);
        setPlayerColour();
        setStartBtn();
    }

    private void setTextLabel(JLabel label, String text, int textsize, int alignX, int alignY) {
        label.setText(text);
        label.setFont(new Font("Courier", Font.PLAIN, textsize));
        Dimension size = label.getPreferredSize();
        label.setBounds(alignX, alignY, size.width, size.height);
        add(label);
    }

    private void setBoardSize() {
        sizeCombo = new JComboBox<>();
        sizeCombo.addItem("7x7");
        sizeCombo.addItem("9x9");
        sizeCombo.addItem("13x13");
        sizeCombo.addItem("19x19");
        sizeCombo.setFont(new Font("Courier", Font.PLAIN, 25));
        sizeCombo.setBounds(620, 300, 100, 40);
        add(sizeCombo);
    }

    private void setPlayerColour() {
        colourCombo = new JComboBox<>();
        colourCombo.addItem("white");
        colourCombo.addItem("black");
        colourCombo.setFont(new Font("Courier", Font.PLAIN, 25));
        colourCombo.setBounds(620, 400, 100, 40);
        add(colourCombo);
    }

    private void setStartBtn() {
        startBtn.setFont(new Font("Courier", Font.PLAIN, 25));
        startBtn.setBounds(450, 500, 100, 40);
        startBtn.setBackground(Color.green);
        add(startBtn);
        startBtn.addActionListener(this::startAction);
    }

    private void startAction(ActionEvent e) {
        int size = computeSize();
        String colour = colourCombo.getSelectedItem().toString();
        out.println(size);
        out.println(colour);
        out.flush();
        new GoGame(out, in, playerName, opponentName, size, colour, mainPlayer).setVisible(true);
        this.frame.dispose();
    }


    private int computeSize() {
        String sizeText = sizeCombo.getSelectedItem().toString();
        int size = 0;
        switch (sizeText) {
            case "7x7":
                size = 7;
                break;
            case "9x9":
                size = 9;
                break;
            case "13x13":
                size = 13;
                break;
            case "19x19":
                size = 19;
                break;
        }
        return size;
    }
}
