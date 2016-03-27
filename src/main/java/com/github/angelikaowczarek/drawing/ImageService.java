package com.github.angelikaowczarek.drawing;

import com.github.angelikaowczarek.PointColor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ImageService {
    private static final String BACKGROUND_IMAGE_LOCATION = "background.png";
    private BufferedImage backgroundImage;
    private BufferedImage blackChecker;
    private BufferedImage whiteChecker;

    public ImageService() {
    }

    public BufferedImage getBackgroundImage() {
        if (backgroundImage == null) {
            backgroundImage = loadImage(BACKGROUND_IMAGE_LOCATION);
        }
        return backgroundImage;
    }

    public BufferedImage getCheckerImage(PointColor pointColor) {
        if (pointColor == PointColor.BLACK) {
            return blackChecker;
        }
        return whiteChecker;
    }

    private BufferedImage loadImage(String location) {
        try {
            return ImageIO.read(getResourceLocation(location));
        } catch (IOException e) {
            throw new RuntimeException("Error while reading image from " + location);
        }
    }

    private URL getResourceLocation(String location) {
        return getClass().getClassLoader().getResource(location);
    }
}
