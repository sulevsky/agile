package org.sulevsky;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RectanglesMerger {

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

    private boolean canMerge(Rectangle rectangleFirst, Rectangle rectangleSecond) {
        Rectangle rectangleWithMargin =
                new Rectangle((int) rectangleFirst.getX() - 1, (int) rectangleFirst.getY() - 1,
                        (int) rectangleFirst.getWidth() + 2, (int) rectangleFirst.getHeight() + 2);

        return rectangleWithMargin.intersects(rectangleSecond);//todo make configurable intersection +1 pixel
    }

    private Rectangle merge(Rectangle rectangleFirst, Rectangle rectangleSecond) {
        return rectangleFirst.union(rectangleSecond);
    }
}
