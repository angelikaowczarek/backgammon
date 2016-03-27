package com.github.angelikaowczarek;

public class PointState {
    private PointColor pointColor;
    private int numberOfCheckers;

    public PointState(PointColor pointColor, int numberOfCheckers) {
        this.pointColor = pointColor;
        this.numberOfCheckers = numberOfCheckers;
    }

    public PointState() {
        this.pointColor = PointColor.EMPTY;
        this.numberOfCheckers = 0;
    }

    public PointColor getPointColor() {
        return pointColor;
    }

    public void setPointColor(PointColor pointColor) {
        this.pointColor = pointColor;
    }

    public int getNumberOfCheckers() {
        return numberOfCheckers;
    }

    public void setNumberOfCheckers(int numberOfCheckers) {
        this.numberOfCheckers = numberOfCheckers;
    }

    public void pop() {
        numberOfCheckers--;
        if (numberOfCheckers == 0) {
            pointColor = PointColor.EMPTY;
        }
    }

    public void push(PointColor checkersPointColor) {
        numberOfCheckers++;
        pointColor = checkersPointColor;
    }
}
