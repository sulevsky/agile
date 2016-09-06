package org.sulevsky;

import org.sulevsky.io.ImageLoader;
import org.sulevsky.io.ImageSaver;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

public class ImageProcessor {

    public static final double COLOR_DIFFERENCE_THRESHOLD = 0.1;


    private final ImageLoader imageLoader;
    private final ImageSaver imageSaver;
    private final RectanglesMerger rectanglesMerger;
    private final DifferenceFinder differenceFinder;
    private final ImagePainter imagePainter;

    public ImageProcessor() {
        imageLoader = new ImageLoader();//TODO good place for spring
        imageSaver = new ImageSaver();
        rectanglesMerger = new RectanglesMerger();
        differenceFinder = new DifferenceFinder();
        imagePainter = new ImagePainter();
    }

    public void processImages(String pathToOriginal, String pathToCopy, String pathToResult) {

        BufferedImage originalImage = imageLoader.loadImageFromPath(pathToOriginal);
        BufferedImage copyImage = imageLoader.loadImageFromPath(pathToCopy);

        List<Rectangle> rectangles = differenceFinder.createRectanglesOnDifferentPoints(originalImage, copyImage);
        List<Rectangle> mergedRectangles = rectanglesMerger.mergeRectangles(rectangles);

        imagePainter.paintRectangles(copyImage, mergedRectangles);
        imageSaver.saveImageByPath(copyImage, "png", pathToResult);//TODO to get rid of hardcoded "png" we need to process image metadata - no 2 hours
    }


}
