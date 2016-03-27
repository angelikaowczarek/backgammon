package com.github.angelikaowczarek;

public class GameState {
    private PointState[] points;

    public PointState getPoint(int numberOfPoint) {
        return points[numberOfPoint - 1];
    }
}

