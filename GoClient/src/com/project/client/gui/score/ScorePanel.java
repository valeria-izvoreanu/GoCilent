package com.project.client.gui.score;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.awt.Color.WHITE;

public class ScorePanel extends JPanel {
    String winnerName;
    ScoreFrame frame;
    Graphics2D graphics;
    BufferedImage background;

    public ScorePanel(ScoreFrame frame) {
        this.frame = frame;
        this.winnerName = frame.winnerName;
        init();
    }

    private void init() {
        String boardPath = new File("img/final.jpg").getAbsolutePath();
        background = new BufferedImage(600, 500, BufferedImage.TYPE_INT_ARGB);

        try {
            background = ImageIO.read(new File(boardPath));
            graphics = background.createGraphics();
            graphics.setColor(WHITE);
            graphics.setFont(new Font("Monaco", Font.PLAIN, 30));
            graphics.drawString("Winner: " + winnerName, 200, 200);

        } catch (IOException e) {
            System.out.println("Couldn't open background image..." + e);
        }
    }

    @Override
    public void update(Graphics g) {
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, this);
    }

}
