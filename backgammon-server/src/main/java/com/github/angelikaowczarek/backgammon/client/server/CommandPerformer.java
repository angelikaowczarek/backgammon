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
        if (checkIsFromBeatenQueue()) return;

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
            for (int i = 24; i <= 18; i--) {
                if (new GameRules().canMoveTo(gameState.getStack(i), currentColor)) {
                    isAnyPopped = true;
                    return true;
                }
            }
            // If checker can't make any move, set all dices as used and finish the round
            for (int j = 0; j < 4; j++) {
                gameState.setIsDiceUsed(j);
            }
            isAnyPopped = false;
            return true;
        }
        if (gameState.getBeatenWhiteCheckers() > 0 && currentColor == StackColor.WHITE) {
            originStackNumber = 0;
            for (int i = 1; i <= 6; i++) {
                if (new GameRules().canMoveTo(gameState.getStack(i), currentColor)) {
                    isAnyPopped = true;
                    return true;
                }
            }
            // If checker can't make any move, set all dices as used and finish the round
            for (int j = 0; j < 4; j++) {
                gameState.setIsDiceUsed(j);
            }
            isAnyPopped = false;
            return true;
        }
        return false;
    }

    private void push(String command, Socket clientSocket) {
        System.out.println(originStackNumber);
        System.out.println("Kolor: " + originStackColor);
        int stackIndex = Integer.parseInt(command);
        if (new GameRules().canMoveTo(gameState.getStack(stackIndex), currentColor)) {
            tryToPush(stackIndex);
        } else {
            if ( !(gameState.getBeatenWhiteCheckers() > 0 && currentColor == StackColor.WHITE)
                    && !(gameState.getBeatenBlackCheckers() > 0 && currentColor == StackColor.BLACK))
                gameState.pushStack(originStackNumber, currentColor);
            System.out.println("odkladam");
            isAnyPopped = false;
        }
        if (!gameState.getIsDiceUsed().contains(false)) {
            if (currentColor.equals(StackColor.WHITE))
                currentColor = StackColor.BLACK;
            else
                currentColor = StackColor.WHITE;
            gameState.setTimeToRollDice(true);
        }
    }

    private void tryToPush(int stackIndex) {
        int diceIndex;
        boolean isFromBeatenQueue;
        System.out.println(originStackColor +" "+ originStackNumber);
        int tempStackForQueue = originStackNumber;
        isFromBeatenQueue = isFromBeatenQueue();
        diceIndex = getDiceIndex(stackIndex);     // If move is possible; else -1

        if (diceIndex == -1 ) {
            gameState.pushStack(tempStackForQueue, currentColor);
            isAnyPopped = false;
            return;
        }

        if (!new GameRules().hasSingleOpponentChecker(
                gameState.getStack(stackIndex),
                currentColor)) {
            System.out.println("single opponent" + currentColor.toString());
            gameState.pushStack(stackIndex, currentColor);
        }
        else {
            System.out.println("beat it" + currentColor.toString());
            System.out.println("Stack: " + gameState.getStack(stackIndex).getStackColor());
            System.out.println(new GameRules().hasSingleOpponentChecker(
                    gameState.getStack(stackIndex),
                    getOpponentColor(currentColor)));
            beatTheChecker(stackIndex);
        }

        gameState.getStack(stackIndex).setStackColor(currentColor);

        if (isFromBeatenQueue) {
            popCheckerFromBeatenQueue();
        }

        gameState.setIsDiceUsed(diceIndex);
        isAnyPopped = false;
    }

    private void popCheckerFromBeatenQueue() {
        if (currentColor == StackColor.WHITE)
            gameState.removeBeatenWhiteCheckers();
        else
            gameState.removeBeatenBlackCheckers();
    }

    private void beatTheChecker(int stackIndex) {
        if (currentColor == StackColor.WHITE) {
            gameState.addBeatenBlackCheckers();
            gameState.getStack(stackIndex).setStackColor(StackColor.WHITE);
        }
        else {
            gameState.addBeatenWhiteCheckers();
            gameState.getStack(stackIndex).setStackColor(StackColor.BLACK);
        }
    }

    private boolean isFromBeatenQueue() {
        boolean isFromBeatenQueue;
        if (gameState.getBeatenBlackCheckers() > 0 && currentColor == StackColor.BLACK) {
            originStackNumber = 25;
            isFromBeatenQueue = true;
            return isFromBeatenQueue;
        }
        if (gameState.getBeatenWhiteCheckers() > 0 && currentColor == StackColor.WHITE) {
            originStackNumber = 0;
            isFromBeatenQueue = true;
        } else
            isFromBeatenQueue = false;
        return isFromBeatenQueue;
    }

    private StackColor getOpponentColor(StackColor color) {
        if (color == StackColor.BLACK) {
            return StackColor.WHITE;
        }
        return StackColor.BLACK;
    }

    private int getDiceIndex(int stackIndex) {
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
