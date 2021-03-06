package com.github.angelikaowczarek.backgammon.client.server;

import com.githum.angelikaowczarek.backgammon.game.StackColor;
import com.githum.angelikaowczarek.backgammon.game.StackState;

public class GameRules {
    // TODO:
    // - skąd pochodzi pionek
    // - o ile pól chce się przemieścić
    // - zasady postępowania, gdy wszystkie pionki są w domu
    // OSOBNO:
    // - odkładanie z linii do dworu

    public boolean canMoveTo(StackState stack, StackColor color) {
        return colorMatches(stack, color) ||
                colorMatches(stack, StackColor.EMPTY) ||
                hasSingleOpponentChecker(stack, color);
    }

    public boolean hasSingleOpponentChecker(StackState stack, StackColor color) {
        return isSingle(stack, getOpponentColor(color));
    }

    private StackColor getOpponentColor(StackColor color) {
        if (color == StackColor.BLACK) {
            return StackColor.WHITE;
        }
        return StackColor.BLACK;
    }

    private boolean isSingle(StackState stack, StackColor color) {
        return colorMatches(stack, color) && hasOneChecker(stack);
    }

    private boolean hasOneChecker(StackState stack) {
        return stack.getNumberOfCheckers() == 1;
    }

    private boolean colorMatches(StackState stack, StackColor color) {
        return stack.getStackColor().equals(color);
    }
}
