package com.github.angelikaowczarek.backgammon.client.server;

import com.githum.angelikaowczarek.backgammon.game.GameState;
import com.githum.angelikaowczarek.backgammon.game.StackColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;
import java.util.ArrayList;

public class CommandPerformer {
    private ArrayList<Socket> sockets = new ArrayList<>();
    private ArrayList<StackColor> socketsColors = new ArrayList<>();
    private StackColor currentColor = StackColor.WHITE;
    private boolean isAnyPopped = false;
    private int originStackNumber = -1;
    private StackColor originStackColor;
    private static final Logger log = LoggerFactory.getLogger(Server.class);
    private GameState gameState;
    private int diceInUsageIndex = -1;
    private boolean isFromBeatenQueue;
    private StackColor colorInBeatenQueue;

    public CommandPerformer() {
        socketsColors.add(StackColor.WHITE);
        socketsColors.add(StackColor.BLACK);
    }

    public void setSockets(ArrayList<Socket> sockets) {
        this.sockets = sockets;
    }

    public GameState performCommand(String command, Socket clientSocket, GameState gameState) {
        this.gameState = gameState;
        log.info("Received command " + command);
        if (command.equals("rollDice")) {
            performRollDice();
//        } else if (command.equals("START")) {
//            return gameState;
        } else if (!isAnyPopped) {
            pop(command, clientSocket);
        } else if (isAnyPopped) {
            push(command, clientSocket);
        }
        return gameState;
    }

    private void performRollDice() {
        gameState.rollDice();
        gameState.setTimeToRollDice(false);
    }

    private void pop(String command, Socket clientSocket) {
        isFromBeatenQueue = checkIsFromBeatenQueue();
        if (isFromBeatenQueue) {
            colorInBeatenQueue = currentColor;
            return;
        }

        int stackIndex = Integer.parseInt(command);

        if (socketsColors.get(sockets.indexOf(clientSocket)).equals(currentColor)
                && gameState.getStack(stackIndex).getStackColor().equals(currentColor)
                && gameState.getIsDiceUsed().contains(false)) {
            gameState.popStack(stackIndex);
            isAnyPopped = true;
            originStackNumber = stackIndex;
            originStackColor = gameState.getStack(stackIndex).getStackColor();
        }
    }

    private boolean checkIsFromBeatenQueue() {
        if (gameState.getBeatenBlackCheckers() > 0 && currentColor == StackColor.BLACK) {
            originStackNumber = 25;
            for (int i = 24; i >= 18; i--) {
                if (new GameRules().canMoveTo(gameState.getStack(i), currentColor)
                        && doesAnyDiceAllowToMove(i)) {
                    isAnyPopped = true;
                    return true;
                }
            }
            // If checker can't make any move, set all dices as used and finish the round
            for (int j = 0; j < 4; j++) {
                gameState.setIsDiceUsed(j);
            }
            letOtherUserMove();
            isAnyPopped = false;
            return true;
        }
        if (gameState.getBeatenWhiteCheckers() > 0 && currentColor == StackColor.WHITE) {
            originStackNumber = 0;
            for (int i = 1; i <= 6; i++) {
                if (new GameRules().canMoveTo(gameState.getStack(i), currentColor)
                        && doesAnyDiceAllowToMove(i)) {
                    isAnyPopped = true;
                    return true;
                }
            }
            // If checker can't make any move, set all dices as used and finish the round
            for (int j = 0; j < 4; j++) {
                gameState.setIsDiceUsed(j);
            }
            letOtherUserMove();
            isAnyPopped = false;
            return true;
        }
        return false;
    }

    private void push(String command, Socket clientSocket) {
        System.out.println(originStackNumber);
        System.out.println("Kolor: " + originStackColor);
        int stackIndex = Integer.parseInt(command);
        if (!doesAnyDiceAllowToMove(stackIndex)) {
            gameState.pushStack(originStackNumber, currentColor);
            return;
        }

        if (stackIndex == 0 && currentColor.equals(StackColor.BLACK)) {
            gameState.addBornOffBlack();
            isAnyPopped = false;
            gameState.setIsDiceUsed(diceInUsageIndex);
        } else if (stackIndex == 25 && currentColor.equals(StackColor.WHITE)) {
            gameState.addBornOffWhite();
            isAnyPopped = false;
            gameState.setIsDiceUsed(diceInUsageIndex);
        } else if (new GameRules().canMoveTo(gameState.getStack(stackIndex), currentColor)) {
            tryToPush(stackIndex);
        } else {
            if (!(gameState.getBeatenWhiteCheckers() > 0 && currentColor == StackColor.WHITE)
                    && !(gameState.getBeatenBlackCheckers() > 0 && currentColor == StackColor.BLACK))
                gameState.pushStack(originStackNumber, currentColor);
            isAnyPopped = false;
        }

        if (!gameState.getIsDiceUsed().contains(false)) {
            letOtherUserMove();
        }
    }

    private void letOtherUserMove() {
            if (currentColor.equals(StackColor.WHITE))
                currentColor = StackColor.BLACK;
            else
                currentColor = StackColor.WHITE;
            gameState.setTimeToRollDice(true);
    }

    private void tryToPush(int stackIndex) {

        if (!new GameRules().hasSingleOpponentChecker(
                gameState.getStack(stackIndex),
                currentColor)) {
            if (stackIndex == 0 && currentColor.equals(StackColor.BLACK)) {
                gameState
                        .getBornOffBlack()
                        .setNumberOfCheckers(gameState.getBornOffBlack()
                                .getNumberOfCheckers() + 1);
            } else if (stackIndex == 25 && currentColor.equals(StackColor.WHITE)) {
                gameState
                        .getBornOffWhite()
                        .setNumberOfCheckers(gameState.getBornOffWhite()
                                .getNumberOfCheckers() + 1);
            } else {
                gameState.pushStack(stackIndex, currentColor);
            }
        } else {
            beatTheChecker(stackIndex);
            gameState.getStack(stackIndex).setStackColor(currentColor);
        }

        if (isFromBeatenQueue) {
            popCheckerFromBeatenQueue();
        }

        isAnyPopped = false;
        gameState.setIsDiceUsed(diceInUsageIndex);
    }

    private boolean doesAnyDiceAllowToMove(int stackIndex) {
        int diceIndex = getAllowingDiceIndex(stackIndex);     // If move is possible; else -1

        if (diceIndex == -1) {
            if (isFromBeatenQueue && currentColor == colorInBeatenQueue)
                letOtherUserMove();
            isAnyPopped = false;
            return false;
        }

        diceInUsageIndex = diceIndex;
        return true;
    }

    private void popCheckerFromBeatenQueue() {
        if (currentColor == StackColor.WHITE)
            gameState.removeBeatenWhiteChecker();
        else
            gameState.removeBeatenBlackChecker();
    }

    private void beatTheChecker(int stackIndex) {
        if (currentColor == StackColor.WHITE) {
            gameState.addBeatenBlackCheckers();
            gameState.getStack(stackIndex).setStackColor(StackColor.WHITE);
        } else {
            gameState.addBeatenWhiteCheckers();
            gameState.getStack(stackIndex).setStackColor(StackColor.BLACK);
        }
    }

//    private StackColor getOpponentColor(StackColor color) {
//        if (color == StackColor.BLACK) {
//            return StackColor.WHITE;
//        }
//        return StackColor.BLACK;
//    }

    private int getAllowingDiceIndex(int stackIndex) {
        int diceNumberIndex = -1;
        for (int i = 0; i < gameState.getDice().size(); i++) {
            if (gameState.getDice().get(i).equals(stackIndex - originStackNumber)
                    && !gameState.getIsDiceUsed().get(i)
                    && currentColor == StackColor.WHITE) {
                diceNumberIndex = i;
                break;
            } else if (gameState.getDice().get(i).equals(originStackNumber - stackIndex)
                    && !gameState.getIsDiceUsed().get(i)
                    && currentColor == StackColor.BLACK) {
                diceNumberIndex = i;
                break;
            }
        }
        return diceNumberIndex;
    }
}
