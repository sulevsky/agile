package org.vsulevskyi;

import org.junit.Assert;
import org.junit.Test;
import org.sulevsky.ImageProcessor;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageProcessorTest {

    @Test
    public void shouldMergeTouchingRectangles() {
        Rectangle f = new Rectangle(1, 1, 1, 1);
        Rectangle s = new Rectangle(1, 2, 1, 1);
        List<Rectangle> recs = Arrays.asList(f, s);
        ImageProcessor imageProcessor = new ImageProcessor();

        List<Rectangle> merged = imageProcessor.mergeRectangles(recs);
        Assert.assertTrue(merged.size() == 1);
        Assert.assertTrue(merged.get(0).equals(new Rectangle(1, 1, 1, 2)));
    }

    @Test
    public void shouldNotMergeRectanglesIfTheyDoNotHaveSameTouchingNeighbour() {
        Rectangle f = new Rectangle(1, 1, 1, 1);
        Rectangle s = new Rectangle(1, 3, 1, 1);
        List<Rectangle> recs = Arrays.asList(f, s);
        ImageProcessor imageProcessor = new ImageProcessor();

        List<Rectangle> merged = imageProcessor.mergeRectangles(recs);
        Assert.assertTrue(merged.size() == 2);
    }

    @Test
    public void shouldMergeTouchingRectanglesOrderDoesNotMatter() {
        Rectangle r1 = new Rectangle(1, 1, 1, 1);
        Rectangle r2 = new Rectangle(1, 2, 1, 1);
        Rectangle r3 = new Rectangle(1, 3, 1, 1);
        List<List<Rectangle>> combinations = new ArrayList<>();
        combinations.add(Arrays.asList(r1, r2, r3));
        combinations.add(Arrays.asList(r1, r3, r2));
        combinations.add(Arrays.asList(r2, r1, r3));
        combinations.add(Arrays.asList(r2, r3, r1));
        combinations.add(Arrays.asList(r3, r1, r2));
        combinations.add(Arrays.asList(r3, r2, r1));
        ImageProcessor imageProcessor = new ImageProcessor();

        for (List<Rectangle> combination : combinations) {
            List<Rectangle> merged = imageProcessor.mergeRectangles(combination);
            Assert.assertTrue(merged.size() == 1);
            Assert.assertTrue(merged.get(0).equals(new Rectangle(1, 1, 1, 3)));

        }
    }

}
