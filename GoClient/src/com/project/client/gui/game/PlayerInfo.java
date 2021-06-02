package com.project.client.gui.game;

import javax.swing.*;
import java.awt.*;

public class PlayerInfo extends JPanel {
    String name;
    String colour;
    JLabel player = new JLabel();
    JLabel colorLabel = new JLabel();
    JLabel captures = new JLabel();
    int capturesNr = 0;
    int nr;

    PlayerInfo(String name, String colour, int number) {
        this.name = name;
        this.colour = colour;
        this.nr = number;
        init();
    }

    private void init() {
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setPreferredSize(new Dimension(200, 150));
        setMaximumSize(new Dimension(200, 150));
        setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        player.setText("Player " + nr + ": " + name);
        player.setFont(new Font("Courier", Font.PLAIN, 30));
        add(player);

        colorLabel.setText("Color: " + colour);
        colorLabel.setFont(new Font("Courier", Font.PLAIN, 30));
        add(colorLabel);

        captures.setText("Captures: " + capturesNr);
        captures.setFont(new Font("Courier", Font.PLAIN, 30));
        add(captures);
    }

    public void updateCaptures(int piecesNr) {
        capturesNr += piecesNr;
        captures.setText("Captures: " + capturesNr);
    }
}
