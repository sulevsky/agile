package org.sulevsky;

import java.awt.Rectangle;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DifferenceFinder {

    public List<Rectangle> createRectanglesOnDifferentPoints(BufferedImage originalImage, BufferedImage copyImage){
        List<Point> differentPoints = findDifferences(originalImage, copyImage);
        return createRectangles(differentPoints);
    }

    private List<Rectangle> createRectangles(List<Point> differentPoints) {
        return differentPoints
                .stream()
                .map(point -> new Rectangle((int) point.getX(), (int) point.getY(), 1, 1))
                .collect(Collectors.toList());
    }

    private List<Point> findDifferences(BufferedImage originalImage, BufferedImage copyImage) {
        List<Point> differentPoints = new ArrayList<>();
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int originalPointColor = originalImage.getRGB(j, i);
                int copyPointColor = copyImage.getRGB(j, i);
                if (isDifferent(originalPointColor, copyPointColor)) {
                    differentPoints.add(new Point(j, i));
                }
            }
        }
        return differentPoints;
    }

    private boolean isDifferent(int originalPointColor, int copyPointColor) {
        return originalPointColor != copyPointColor;
//        return Math.abs(originalPointColor * 1.0 - copyPointColor) / copyPointColor > COLOR_DIFFERENCE_THRESHOLD;
        //IMPORTANT do no understand requirement about 10% - color consists of 3 or 4 channels  what does it mean 10 % for 3 channels?  - has to be discussed
    }
}
