package com.project.client.gui.game;

import java.awt.geom.Ellipse2D;

public class GoPiece extends Ellipse2D.Double {
    public GoPiece(double x0, double y0, double radius) {
        super(x0 - radius / 2, y0 - radius / 2, radius, radius);
    }
}

