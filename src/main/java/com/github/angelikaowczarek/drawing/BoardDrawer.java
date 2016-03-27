package com.github.angelikaowczarek.drawing;

import com.github.angelikaowczarek.GameState;
import com.github.angelikaowczarek.PointColor;
import com.github.angelikaowczarek.PointState;
import lombok.AllArgsConstructor;

import java.awt.*;
import java.awt.image.ImageObserver;

@AllArgsConstructor
public class BoardDrawer {
    private static final ImageObserver NO_IMAGE_OBSERVER = null;
    private static final int MAX_NUMBER_OF_POINTS = 24;
    private static final int NUMBER_OF_FIRST_POINT_IN_UPPER_ROW = 13;
    private Graphics2D canvas;
    private GameState gameState;
    private ImageService imageService;

    public void drawBoard() {
        drawBackground();
        drawCheckersOnPoints();
    }

    private void drawCheckersOnPoints() {
        for (int pointNumber = 1; pointNumber <= MAX_NUMBER_OF_POINTS; pointNumber++) {
            drawCheckersOnPoint(gameState.getPoint(pointNumber), pointNumber);
        }
    }

    private void drawCheckersOnPoint(PointState point, int pointNumber) {
        Location pointLocation = getPointLocation(pointNumber);
        boolean pointInTopRow = isPointInTopRow(pointNumber);

        for (int checkerNumber = 1; checkerNumber <= point.getNumberOfCheckers(); checkerNumber++) {
            drawCheckerOnPoint(
                    pointLocation,
                    getRelativeCheckerLocation(checkerNumber, pointInTopRow),
                    point.getPointColor());
        }

//        IntStream
//                .range(1, point.getNumberOfCheckers())
//                .forEach(checkerNumber -> {
//                    drawCheckerOnPoint(
//                            pointLocation,
//                            getRelativeCheckerLocation(checkerNumber, pointInTopRow),
//                            point.getPointColor());
//                });
    }

    private void drawCheckerOnPoint(Location pointLocation, Location relativeCheckerLocation, PointColor pointColor) {
        drawChecker(getAbsoluteCheckerLocation(pointLocation, relativeCheckerLocation), pointColor);
    }

    private void drawChecker(Location absoluteCheckerLocation, PointColor pointColor) {
        canvas.drawImage(
                imageService.getCheckerImage(pointColor),
                absoluteCheckerLocation.getX(),
                absoluteCheckerLocation.getY(),
                NO_IMAGE_OBSERVER);
    }

    private Location getAbsoluteCheckerLocation(Location pointLocation, Location relativeCheckerLocation) {
        return new Location(
                pointLocation.getX() + relativeCheckerLocation.getX(),
                pointLocation.getY() + relativeCheckerLocation.getY());
    }

    private Location getRelativeCheckerLocation(int checkerNumber, boolean pointInTopRow) {
        return null;
    }

    private Location getPointLocation(int pointNumber) {
        return new Location(getPointXLocation(pointNumber), getPointYLocation(pointNumber));
    }

    private int getPointXLocation(int pointNumber) {
        if (isPointInTopRow(pointNumber)) {
            return getPointWidth() * (pointNumber - 13);
        }
        return getBoardWidth() - (pointNumber * getPointWidth());
    }

    private int getBoardWidth() {
        // TODO
        return 0;
    }

    private int getPointWidth() {
        // TODO
        return 0;
    }

    private boolean isPointInTopRow(int pointNumber) {
        return pointNumber > NUMBER_OF_FIRST_POINT_IN_UPPER_ROW;
    }

    private int getPointYLocation(int pointNumber) {
        if (isPointInTopRow(pointNumber)) {
            return 0;
        }
        return (getBoardHeight() / 2) + (getCenterPadding() / 2);
    }

    private int getCenterPadding() {
        // TODO
        return 0;
    }

    private int getBoardHeight() {
        // TODO
        return 0;
    }

    private void drawBackground() {
        canvas.drawImage(
                imageService.getBackgroundImage(),
                0,
                0,
                NO_IMAGE_OBSERVER);
    }



}
