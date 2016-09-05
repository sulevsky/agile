package org.sulevsky;

import org.sulevsky.io.ImageLoader;
import org.sulevsky.io.ImageSaver;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ImageProcessor {
    private final ImageLoader imageLoader;
    private final ImageSaver imageSaver;
    public static final double COLOR_THRESHOLD = 0.1;

    public ImageProcessor() {
        imageLoader = new ImageLoader();//place for spring
        imageSaver = new ImageSaver();
    }

    public void processImages(String pathToOriginal, String pathToCopy, String pathToResult) {
        BufferedImage originalImage = imageLoader.loadImageFromPath(pathToOriginal);
        BufferedImage copyImage = imageLoader.loadImageFromPath(pathToCopy);

        List<Point> differentPoints = findDifferences(originalImage, copyImage);
        List<Rectangle> rectangles = createRectangles(differentPoints);
        List<Rectangle> mergedRectangles = mergeRectangles(rectangles);
        Graphics graphics = copyImage.getGraphics();

        for (Rectangle rectangle : mergedRectangles) {
            graphics.setColor(Color.RED);

            graphics.draw3DRect((int) rectangle.getX(),
                    (int) rectangle.getY(),
                    (int) rectangle.getWidth(),
                    (int) rectangle.getHeight(), false);

        }
        imageSaver.saveImageByPath(copyImage, "png", pathToResult);

    }

    //TODO extract
    public List<Rectangle> mergeRectangles(List<Rectangle> rectangles) {
        List<Rectangle> firstTry = performMerging(rectangles);
        List<Rectangle> secondTry = performMerging(firstTry);

        while (!isConverged(firstTry, secondTry)) {
            firstTry = secondTry;
            secondTry = performMerging(firstTry);

        }
        return secondTry;
    }

    private boolean isConverged(List<Rectangle> firstTry, List<Rectangle> secondTry) {
        return firstTry.size() == secondTry.size();
    }

    private List<Rectangle> performMerging(List<Rectangle> rectangles) {
        List<Rectangle> mergedRectangles = new ArrayList<>();
        Set<Rectangle> mergedToDelete = new HashSet<>();

        for (int i = 0; i < rectangles.size(); i++) {
            Rectangle rectangleOuter = rectangles.get(i);
            if (mergedToDelete.contains(rectangleOuter)) {
                continue;
            }

            for (int j = i + 1; j < rectangles.size(); j++) {
                Rectangle rectangleInner = rectangles.get(j);
                if (mergedToDelete.contains(rectangleInner)) {
                    continue;
                }
                if (canMerge(rectangleOuter, rectangleInner)) {
                    Rectangle rectangle = merge(rectangleOuter, rectangleInner);
                    mergedRectangles.add(rectangle);
                    mergedToDelete.add(rectangleOuter);
                    mergedToDelete.add(rectangleInner);

                }

            }
        }
        List<Rectangle> unprocessedRectangles = rectangles
                .stream()
                .filter(r -> !mergedToDelete.contains(r))
                .collect(Collectors.toList());
        mergedRectangles.addAll(unprocessedRectangles);
        return mergedRectangles;
    }

    private Rectangle merge(Rectangle rectangleFirst, Rectangle rectangleSecond) {
        return rectangleFirst.union(rectangleSecond);
    }

    private boolean canMerge(Rectangle rectangleFirst, Rectangle rectangleSecond) {
        Rectangle rectangleWithMargin =
                new Rectangle((int) rectangleFirst.getX() - 1, (int) rectangleFirst.getY() - 1,
                        (int) rectangleFirst.getWidth() + 2, (int) rectangleFirst.getHeight() + 2);

        return rectangleWithMargin.intersects(rectangleSecond);//todo intersection +1 pixel
//        return true;
    }

    private List<Rectangle> createRectangles(List<Point> differentPoints) {
        return differentPoints
                .stream()
                .map(point -> new Rectangle((int)point.getX(), (int)point.getY(), 1, 1))
                .collect(Collectors.toList());
    }

    private List<Point> findDifferences(BufferedImage originalImage, BufferedImage copyImage) {
        List<Point> differentPoints = new ArrayList<Point>();
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int originalPointColor = originalImage.getRGB(j, i);//TODO check
                int copyPointColor = copyImage.getRGB(j, i);//TODO check
                if (isDifferent(originalPointColor, copyPointColor)) {
                    differentPoints.add(new Point(j, i));
                }
            }
        }
        return differentPoints;
    }

    private boolean isDifferent(int originalPointColor, int copyPointColor) {
        return originalPointColor != copyPointColor;
//        return Math.abs(originalPointColor * 1.0 - copyPointColor) / copyPointColor > COLOR_THRESHOLD;
    }
}
