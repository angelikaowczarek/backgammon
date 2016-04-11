package com.githum.angelikaowczarek.backgammon.game;

public class GameState {
    private StackState[] stacks;

    // TODO:
    // przechowywanie pionów w dworze.
    // Gdzie losowanie kostek?

    public GameState() {
        stacks = new StackState[24];
        for (StackState stack :
                stacks ) {
            stack = new StackState();
            //System.out.println(stack.getStackColor());

        }
    }

    public StackState getStack(int numberOfStack) {
        return stacks[numberOfStack - 1];
    }
}

