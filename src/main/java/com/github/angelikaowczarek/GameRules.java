package com.github.angelikaowczarek;

public class GameRules {
    public boolean canMoveTo(PointState point, PointColor color) {
        return colorMatches(point, color) ||
                colorMatches(point, PointColor.EMPTY) ||
                hasSingleOpponentChecker(point, color);
    }

    private boolean hasSingleOpponentChecker(PointState point, PointColor color) {
        return isSingle(point, getOpponentColor(color));
    }

    private PointColor getOpponentColor(PointColor color) {
        if (color == PointColor.BLACK) {
            return PointColor.WHITE;
        }
        return PointColor.BLACK;
    }

    private boolean isSingle(PointState point, PointColor color) {
        return colorMatches(point, color) && hasOneChecker(point);
    }

    private boolean hasOneChecker(PointState point) {
        return point.getNumberOfCheckers() == 1;
    }

    private boolean colorMatches(PointState point, PointColor color) {
        return point.getPointColor().equals(color);
    }
}
