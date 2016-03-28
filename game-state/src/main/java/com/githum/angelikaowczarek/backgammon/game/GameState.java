package com.githum.angelikaowczarek.backgammon.game;

public class GameState {
    private StackState[] stacks;

    public StackState getStack(int numberOfStack) {
        return stacks[numberOfStack - 1];
    }
}

