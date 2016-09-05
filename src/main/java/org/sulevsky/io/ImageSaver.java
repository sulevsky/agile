package org.sulevsky.io;

import org.sulevsky.io.exceptions.SavingException;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

public class ImageSaver {
    public void saveImageByPath(RenderedImage image, String imageType, String path) {
        try {
            ImageIO.write(image, imageType, new File(path));
        } catch (IOException e) {
            throw new SavingException("Failed to save file " + path, e);
        }
    }
}
