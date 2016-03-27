package com.github.angelikaowczarek;

import org.junit.Test;

import static org.junit.Assert.*;

public class GameStateTest {
    @Test
    public void shouldBePossibleToGetPointsFromOneToTwentyFour() throws Exception {
        // given
        GameState gameState = new GameState();

        // when
        PointState firstPoint = gameState.getPoint(1);
        PointState twentyFourthPoint = gameState.getPoint(24);

        // then
        assertNotNull(firstPoint);
        assertNotNull(twentyFourthPoint);
    }
}