package com.github.angelikaowczarek.backgammon.client.drawing;

import com.githum.angelikaowczarek.backgammon.game.GameState;
import com.githum.angelikaowczarek.backgammon.game.StackColor;
import com.githum.angelikaowczarek.backgammon.game.StackState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.ImageObserver;

@NoArgsConstructor
@AllArgsConstructor
public class BoardDrawer {
    private static final ImageObserver NO_IMAGE_OBSERVER = null;
    private static final int MAX_NUMBER_OF_STACKS = 24;
    private static final int NUMBER_OF_FIRST_STACK_IN_UPPER_ROW = 13;
    private static final int FIRST_DICE_X_LOCATION = 13;
    private Graphics2D canvas;
    private GameState gameState;
    private ImageService imageService;

    public void drawBoard() {
        drawBackground();
        //drawCheckersOnStacks();
        drawDice(4, 1);
        drawDice(4, 2);
        drawDice(4, 3);
        drawDice(4, 4);
    }

    public void drawDice(int numberOnDice, int diceNumber) {
        canvas.drawImage(
                imageService.getDiceImage(numberOnDice),
                (int)getDiceLocation(diceNumber).getX(),
                (int)getDiceLocation(diceNumber).getY(),
                NO_IMAGE_OBSERVER);
    }

    private Point getDiceLocation(int diceNumber) {
        Point diceLocation = new Point();
        if ( diceNumber ==  1 )
            diceLocation.setLocation(445, 226);
        else if ( diceNumber ==  2 )
            diceLocation.setLocation(525, 226);
        else if ( diceNumber ==  3 )
            diceLocation.setLocation(365, 226);
        else
            diceLocation.setLocation(605, 226);
        return diceLocation;
    }

    private void drawCheckersOnStacks() {
        for (int stackNumber = 1; stackNumber <= MAX_NUMBER_OF_STACKS; stackNumber++) {
            //drawCheckersOnPoint(gameState.getStack(stackNumber), stackNumber);
        }
    }

//    private void drawCheckersOnPoint(StackState point, int pointNumber) {
//        Point pointLocation = getStackLocation(pointNumber);
//        boolean pointInTopRow = isPointInTopRow(pointNumber);
//
//        for (int checkerNumber = 1; checkerNumber <= point.getNumberOfCheckers(); checkerNumber++) {
//            drawCheckerOnPoint(
//                    pointLocation,
//                    getRelativeCheckerLocation(checkerNumber, pointInTopRow),
//                    point.getStackColor());
//        }
//    }
//
//    private void drawCheckerOnPoint(Point pointLocation, Point relativeCheckerLocation, StackColor stackColor) {
//        drawChecker(getAbsoluteCheckerLocation(pointLocation, relativeCheckerLocation), stackColor);
//    }
//
//    private void drawChecker(Point absoluteCheckerLocation, StackColor stackColor) {
//        canvas.drawImage(
//                imageService.getCheckerImage(stackColor),
//                (int) absoluteCheckerLocation.getX(),
//                (int) absoluteCheckerLocation.getY(),
//                NO_IMAGE_OBSERVER);
//    }
//
//    private void drawCheckerSide(Point absoluteCheckerSideLocation, StackColor stackColor) {
//        canvas.drawImage(
//                imageService.getCheckerSideImage(stackColor),
//                (int) absoluteCheckerSideLocation.getX(),
//                (int) absoluteCheckerSideLocation.getY(),
//                NO_IMAGE_OBSERVER);
//    }
//
//    private Point getAbsoluteCheckerLocation(Point pointLocation, Point relativeCheckerLocation) {
//        return new Point(
//                (int) (pointLocation.getX() + relativeCheckerLocation.getX()),
//                (int) (pointLocation.getY() + relativeCheckerLocation.getY()));
//    }
//
//    private Point getAbsoluteCheckerSideLocation(Point pointLocation, Point relativeCheckerSideLocation) {
//        return new Point(
//                (int) (pointLocation.getX() + relativeCheckerSideLocation.getX()),
//                (int) (pointLocation.getY() + relativeCheckerSideLocation.getY()));
//    }
//
//    private Point getRelativeCheckerLocation(int checkerNumber, boolean pointInTopRow) {
//        // TODO
//        return null;
//    }
//
//    private Point getStackLocation(int pointNumber) {
//        return new Point(getStackXLocation(pointNumber), getStackYLocation(pointNumber));
//    }
//
//    private int getStackXLocation(int pointNumber) {
//        if (isPointInTopRow(pointNumber)) {
//            return getPointWidth() * (pointNumber - 13);
//        }
//        return getBoardWidth() - (pointNumber * getPointWidth());
//    }
//
//    private int getBoardWidth() {
//        // TODO
//        return 0;
//    }
//
//    private int getPointWidth() {
//        // TODO
//        return 0;
//    }
//
//    private boolean isPointInTopRow(int pointNumber) {
//        return pointNumber > NUMBER_OF_FIRST_STACK_IN_UPPER_ROW;
//    }
//
//    private int getStackYLocation(int pointNumber) {
//        if (isPointInTopRow(pointNumber)) {
//            return 0;
//        }
//        return (getBoardHeight() / 2) + (getCenterPadding() / 2);
//    }
//
//    private int getCenterPadding() {
//        // TODO
//        return 0;
//    }
//
//    private int getBoardHeight() {
//        // TODO
//        return 0;
//    }

    private void drawBackground() {
        canvas.drawImage(
                imageService.getBackgroundImage(),
                0,
                0,
                NO_IMAGE_OBSERVER);
    }


}
