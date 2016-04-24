package com.githum.angelikaowczarek.backgammon.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameState implements Serializable{
    private StackState[] stacks;
    private boolean isTimeToRollDice;
    private boolean isTimeToPop;
    private List<Integer> dice;
    private List<Boolean> isDiceUsed;
    private int beatenBlackCheckers;
    private int beatenWhiteCheckers;

    // TODO:
    // przechowywanie pionów w dworze.
    // Gdzie losowanie kostek?

    public GameState() {
        stacks = new StackState[24];
        for (int i = 0; i < 24; i++) {
            stacks[i] = new StackState();
        }
        setNewGameState();
        isTimeToRollDice = true;
        isTimeToPop = false;
        dice = new ArrayList<>();
        isDiceUsed = new ArrayList<>();
        beatenBlackCheckers = 0;
        beatenWhiteCheckers = 0;
    }

    public void rollDice() {
        dice.clear();
        isDiceUsed.clear();
        Random generator = new Random();
        dice.add(generator.nextInt(6)+1);
        dice.add(generator.nextInt(6)+1);
        isDiceUsed.add(false);
        isDiceUsed.add(false);
        if (dice.get(0) == dice.get(1)) {
            dice.add(dice.get(0));
            dice.add(dice.get(0));
            isDiceUsed.add(false);
            isDiceUsed.add(false);
        }
        else {
            dice.add(0);
            dice.add(0);
            isDiceUsed.add(true);
            isDiceUsed.add(true);
        }
    }

    public List<Integer> getDice() {
        return dice;
    }

    public List<Boolean> getIsDiceUsed() {
        return isDiceUsed;
    }

    public void setIsDiceUsed(int diceIndex) {
        isDiceUsed.set(diceIndex, true);
    }

    public void setNewGameState() {
        stacks[0].setStackColor(StackColor.WHITE);
        stacks[0].setNumberOfCheckers(2);
        stacks[5].setStackColor(StackColor.BLACK);
        stacks[5].setNumberOfCheckers(5);
        stacks[7].setStackColor(StackColor.BLACK);
        stacks[7].setNumberOfCheckers(3);
        stacks[11].setStackColor(StackColor.WHITE);
        stacks[11].setNumberOfCheckers(5);
        stacks[12].setStackColor(StackColor.BLACK);
        stacks[12].setNumberOfCheckers(5);
        stacks[16].setStackColor(StackColor.WHITE);
        stacks[16].setNumberOfCheckers(3);
        stacks[18].setStackColor(StackColor.WHITE);
        stacks[18].setNumberOfCheckers(5);
        stacks[23].setStackColor(StackColor.BLACK);
        stacks[23].setNumberOfCheckers(2);
    }

    public boolean isTimeToRollDice() {
        return isTimeToRollDice;
    }

    public boolean isTimeToPop() { return isTimeToPop; }

    public void setTimeToRollDice(boolean timeToRollDice) {
        isTimeToRollDice = timeToRollDice;
    }

    public StackState getStack(int numberOfStack) {
        return stacks[numberOfStack - 1];
    }

    public void popStack(int numberOfStack) {
        stacks[numberOfStack - 1]
                    .setNumberOfCheckers(stacks[numberOfStack - 1].getNumberOfCheckers() - 1);
    }

    public void pushStack(int numberOfStack) {
        stacks[numberOfStack - 1]
                    .setNumberOfCheckers(stacks[numberOfStack - 1].getNumberOfCheckers() + 1);
    }

    public StackState[] getStacks() {
        return stacks;
    }

    public void setStacks(StackState[] stacks) {
        this.stacks = stacks;
    }

    public int getBeatenBlackCheckers() {
        return beatenBlackCheckers;
    }

    public int getBeatenWhiteCheckers() {
        return beatenWhiteCheckers;
    }

    public void addBeatenBlackCheckers() {
        beatenBlackCheckers++;
    }

    public void addBeatenWhiteCheckers() {
        beatenWhiteCheckers++;
    }

    public void removeBeatenBlackCheckers() {
        beatenBlackCheckers--;
    }

    public void removeBeatenWhiteCheckers() {
        beatenWhiteCheckers--;
    }
}

