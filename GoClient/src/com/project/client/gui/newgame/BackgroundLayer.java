package com.project.client.gui.newgame;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.LayerUI;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BackgroundLayer extends LayerUI<JComponent> {
    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);

        Graphics2D g2 = (Graphics2D) g.create();

        int W = c.getWidth();
        int H = c.getHeight();
        g2.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, .3f));

        String backgroundPath = new File("img/welcome.jpg").getAbsolutePath();
        BufferedImage background = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        try {
            background = ImageIO.read(new File(backgroundPath));
        } catch (IOException e) {
            System.out.println("Couldn't open background image" + e);
        }
        g2.drawImage(background, 0, 0, W, H, null);

        g2.dispose();
    }
}