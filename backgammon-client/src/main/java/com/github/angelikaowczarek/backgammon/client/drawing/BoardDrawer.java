package com.github.angelikaowczarek.backgammon.client.drawing;

import com.githum.angelikaowczarek.backgammon.game.GameState;
import com.githum.angelikaowczarek.backgammon.game.StackColor;
import com.githum.angelikaowczarek.backgammon.game.StackState;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.ImageObserver;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class BoardDrawer {
    private static final ImageObserver NO_IMAGE_OBSERVER = null;
    private static final int MAX_NUMBER_OF_STACKS = 24;
    private static final int NUMBER_OF_FIRST_STACK_IN_UPPER_ROW = 13;
    private Graphics2D canvas;
    private GameState gameState;
    private ImageService imageService;

    public void drawBoard() {
        drawBackground();
        if (gameState.isTimeToRollDice())
            drawRollDiceButton();
        else
            drawDice(gameState.getDice());
        drawCheckersOnStacks();
        drawBeatenCheckers();
    }

    public void drawDice(List<Integer> dice) {
        drawDiceImage(dice.get(0), 1);
        drawDiceImage(dice.get(1), 2);

        if ( dice.get(0).equals(dice.get(1)) ) {
            drawDiceImage(dice.get(2), 3);
            drawDiceImage(dice.get(3), 4);
        }
    }

    private void drawDiceImage(int numberOnDice, int diceNumber) {
        canvas.drawImage(
                imageService.getDiceImage(numberOnDice),
                (int)getDiceLocation(diceNumber).getX(),
                (int)getDiceLocation(diceNumber).getY(),
                NO_IMAGE_OBSERVER);
    }

    public void drawRollDiceButton() {
        canvas.drawImage(
                imageService.getRollDiceImage(),
                90, 241,
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
            drawCheckersOnPoint(gameState.getStack(stackNumber), stackNumber);
        }
    }

    private void drawCheckersOnPoint(StackState stack, int stackNumber) {
        Point pointLocation = getStackLocation(stackNumber, stack.getNumberOfCheckers());

        for (int checkerNumber = 1; checkerNumber <= stack.getNumberOfCheckers(); checkerNumber++) {
            drawChecker(
                    pointLocation,
                    stack.getStackColor());
            if (isPointInTopRow(stackNumber))
                pointLocation.setLocation(pointLocation.getX(), pointLocation.getY()-getCheckerHeight());
            else
                pointLocation.setLocation(pointLocation.getX(), pointLocation.getY()+getCheckerHeight());
        }
    }

    private void drawChecker(Point checkerLocation, StackColor stackColor) {
        canvas.drawImage(
                imageService.getCheckerImage(stackColor),
                (int) checkerLocation.getX(),
                (int) checkerLocation.getY(),
                NO_IMAGE_OBSERVER);
    }

    private void drawBeatenCheckers() {
        for (int i = 0; i < gameState.getBeatenWhiteCheckers(); i++) {
            Point point = new Point(getBeatenXLocation(), getBeatenWhiteYLocation(i));
            drawChecker(point, StackColor.WHITE);
        }

        for (int i = 0; i < gameState.getBeatenBlackCheckers(); i++) {
            Point point = new Point(getBeatenXLocation(), getBeatenBlackYLocation(i));
            drawChecker(point, StackColor.BLACK);
        }
    }

    private void drawCheckerSide(Point absoluteCheckerSideLocation, StackColor stackColor) {
        canvas.drawImage(
                imageService.getCheckerSideImage(stackColor),
                (int) absoluteCheckerSideLocation.getX(),
                (int) absoluteCheckerSideLocation.getY(),
                NO_IMAGE_OBSERVER);
    }

    private Point getStackLocation(int pointNumber, int numberOfCheckers) {
        return new Point(getStackXLocation(pointNumber), getStackYLocation(pointNumber, numberOfCheckers));
    }

    private int getStackXLocation(int pointNumber) {
        if (isPointInTopRow(pointNumber)) {
            if (pointNumber > 18 )
                return getSidePaddingWidth()
                        + getCenterPadding()
                        + getPointWidth() * (pointNumber - 13);
            return getSidePaddingWidth() + getPointWidth() * (pointNumber - 13);
        }
        if (pointNumber > 6 )
            return getRightBandXLocation()
                    - getCenterPadding()
                    - (pointNumber * getPointWidth());
        return getRightBandXLocation() - (pointNumber * getPointWidth());
    }

    private int getBeatenXLocation() {
        return 315;
    }

    private int getBeatenBlackYLocation(int numberOfBeaten) {
        return 15 + (numberOfBeaten) * getCheckerHeight();
    }

    private int getBeatenWhiteYLocation(int numberOfBeaten) {
        return 466 - (numberOfBeaten) * getCheckerHeight();
    }

    private int getPointWidth() {
        return 50;
    }

    private int getCheckerHeight() {
        return 41;
    }

    private int getSidePaddingWidth() {
        return 15;
    }

    private int getRightBandXLocation() {
        return 665;
    }

    private boolean isPointInTopRow(int pointNumber) {
        return pointNumber >= NUMBER_OF_FIRST_STACK_IN_UPPER_ROW;
    }

    private int getStackYLocation(int pointNumber, int numberOfCheckers) {
        if (isPointInTopRow(pointNumber)) {
            return 15 + getCheckerHeight() * (numberOfCheckers - 1);
        }
        return 466 - getCheckerHeight() * (numberOfCheckers - 1);
    }

    private int getCenterPadding() {
        return 50;
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
