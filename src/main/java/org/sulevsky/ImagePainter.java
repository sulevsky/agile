package org.sulevsky;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

public class ImagePainter {

    public void paintRectangles(BufferedImage image, List<Rectangle> rectangles) {
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.RED);

        for (Rectangle rectangle : rectangles) {
            graphics.draw3DRect((int) rectangle.getX(),
                    (int) rectangle.getY(),
                    (int) rectangle.getWidth(),
                    (int) rectangle.getHeight(), false);
        }
    }
}
