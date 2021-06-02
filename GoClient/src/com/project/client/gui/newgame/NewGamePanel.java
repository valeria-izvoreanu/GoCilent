package com.project.client.gui.newgame;

import com.project.client.gui.game.GoGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class NewGamePanel extends JPanel {
    final NewGameFrame frame;
    PrintWriter out;
    BufferedReader in;
    JLabel title = new JLabel();
    JLabel playerLabel = new JLabel();
    JButton startBtn = new JButton("Start");
    JTextField playerName = new JTextField(18);
    boolean mainPlayer;
    String opponentName;

    public NewGamePanel(NewGameFrame frame, PrintWriter out, BufferedReader in, boolean mainPlayer) {
        this.frame = frame;
        this.out = out;
        this.in = in;
        this.mainPlayer = mainPlayer;
        init();
    }

    private void init() {
        setLayout(null);
        setTextLabel(title, "Welcome to GoClient!", 50, 450, 150);
        setTextLabel(playerLabel, "Player:", 30, 430, 300);
        addTextBox(playerName, 25, 555, 300);
        setStartBtn();
    }

    private void setTextLabel(JLabel label, String text, int textSize, int alignX, int alignY) {
        label.setText(text);
        label.setFont(new Font("Courier", Font.PLAIN, textSize));
        Dimension size = label.getPreferredSize();
        label.setBounds(alignX, alignY, size.width, size.height);
        add(label);
    }


    private void setStartBtn() {
        startBtn.setFont(new Font("Courier", Font.PLAIN, 25));
        startBtn.setBounds(680, 450, 100, 40);
        startBtn.setBackground(Color.green);
        add(startBtn);
        startBtn.addActionListener(this::startAction);
    }

    private void startAction(ActionEvent e) {
        out.println(playerName.getText());
        out.flush();

        startBtn.setEnabled(false);
        playerName.setEnabled(false);
        try {
            opponentName = in.readLine();
            if (mainPlayer) {
                new SettingsFrame(out, in, playerName.getText(), opponentName, mainPlayer).setVisible(true);
            } else {
                int boardSize = Integer.parseInt(in.readLine());
                String colour = in.readLine();
                new GoGame(out, in, playerName.getText(), opponentName, boardSize, colour, mainPlayer).setVisible(true);
            }
            this.frame.dispose();
        } catch (IOException err) {
            System.out.println("Couldn't read opponent name..." + err);
        }


    }

    private void addTextBox(JTextField field, int textSize, int alignX, int alignY) {
        field.setFont(new Font("Courier", Font.PLAIN, textSize));
        field.setBounds(alignX, alignY, field.getPreferredSize().width, field.getPreferredSize().height);
        add(field);
    }


}
