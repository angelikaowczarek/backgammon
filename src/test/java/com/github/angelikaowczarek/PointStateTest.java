package com.github.angelikaowczarek;

import org.junit.Test;

import static org.junit.Assert.*;

public class PointStateTest {
    private static final int RANDOM_NUMBER_OF_CHECKERS = 5;
    private static final int ONE_CHECKER = 1;
    private static final int MORE_THAN_ONE_CHECKER = 2;

    @Test
    public void numberOfCheckersShouldDecreaseWhenWePop() throws Exception {
        // given
        PointState pointState = new PointState(PointColor.BLACK, RANDOM_NUMBER_OF_CHECKERS);

        // when
        pointState.pop();

        // then
        assertEquals(RANDOM_NUMBER_OF_CHECKERS - 1, pointState.getNumberOfCheckers());
    }

    @Test
    public void colorOfPointShouldNotChangeWhenWePopAndAtLeastOneCheckerLeft() throws Exception {
        // given
        PointColor initialPointColor = PointColor.BLACK;
        PointState pointState = new PointState(initialPointColor, MORE_THAN_ONE_CHECKER);

        // when
        pointState.pop();

        // then
        assertEquals(initialPointColor, pointState.getPointColor());
    }

    @Test
    public void pointShouldBeEmptyAfterPoppingLastChecker() throws Exception {
        // given
        PointState pointState = new PointState(PointColor.WHITE, ONE_CHECKER);

        // when
        pointState.pop();

        // then
        assertEquals(PointColor.EMPTY, pointState.getPointColor());
    }

    @Test
    public void numberOfCheckersShouldIncreaseWhenWePush() throws Exception {
        // given
        PointState pointState = new PointState(PointColor.BLACK, RANDOM_NUMBER_OF_CHECKERS);

        // when
        pointState.push(PointColor.BLACK);

        // then
        assertEquals(RANDOM_NUMBER_OF_CHECKERS + 1, pointState.getNumberOfCheckers());
    }

    @Test
    public void pointShouldBeColouredWhiteWhenWePushFirstWhiteChecker() throws Exception {
        // given
        PointState pointState = new PointState();

        // when
        pointState.push(PointColor.WHITE);

        // then
        assertEquals(PointColor.WHITE, pointState.getPointColor());
    }

    @Test
    public void pointShouldBeColouredBlackWhenWePushFirstBlackChecker() throws Exception {
        // given
        PointState pointState = new PointState();

        // when
        pointState.push(PointColor.BLACK);

        // then
        assertEquals(PointColor.BLACK, pointState.getPointColor());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenWePushWhiteCheckerOnBlackPoint() throws Exception {
        // given
        PointState pointState = new PointState(PointColor.BLACK, RANDOM_NUMBER_OF_CHECKERS);

        // when
        pointState.push(PointColor.WHITE);
    }
}