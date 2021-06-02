package com.project.client.gui.newgame;

import com.project.client.GoClient;

import javax.swing.*;
import javax.swing.plaf.LayerUI;
import java.awt.*;
import java.io.BufferedReader;
import java.io.PrintWriter;

public class NewGameFrame extends JFrame {
    NewGamePanel newGamePanel;
    PrintWriter out;
    BufferedReader in;
    boolean mainPlayer;

    public NewGameFrame(PrintWriter out, BufferedReader in, boolean mainPlayer) {
        super("GoClient");
        this.out = out;
        this.in = in;
        this.mainPlayer = mainPlayer;
        init();
    }

    private void init() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        out.println(mainPlayer);
        out.flush();

        //create the components
        newGamePanel = new NewGamePanel(this, out, in, mainPlayer);
        LayerUI<JComponent> layerUI = new BackgroundLayer();
        JLayer<JComponent> jLayer = new JLayer<>(newGamePanel, layerUI);

        add(jLayer);

        setPreferredSize(new Dimension(1000, 900));
        setResizable(false);

        //invoke the layout manager
        pack();
        if (!mainPlayer) {
            GoClient.setLocationToTopRight(this);
        }
    }
}
