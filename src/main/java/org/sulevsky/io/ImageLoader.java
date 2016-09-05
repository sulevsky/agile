package org.sulevsky.io;

import org.sulevsky.io.exceptions.LoadingException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageLoader {
    public BufferedImage loadImageFromPath(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new LoadingException("Failed to load file from path:" + path, e);
        }
    }
}
