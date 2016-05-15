package com.github.angelikaowczarek.backgammon.client.server;

import com.githum.angelikaowczarek.backgammon.game.GameState;
import com.githum.angelikaowczarek.backgammon.game.StackColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Server {
    private static final int SERVER_PORT = 1342;
    private ServerSocket socket;
    private ArrayList<Socket> sockets = new ArrayList<>();
    private ArrayList<StackColor> socketsColors = new ArrayList<>();
    private StackColor currentColor = StackColor.WHITE;
    private boolean serverIsRunning;
    private boolean isAnyPopped = false;
    private int originStackNumberOfCheckers = -1;
    private StackColor originStackColor;
    private Executor clientsExecutor = Executors.newCachedThreadPool();
    private static final Logger log = LoggerFactory.getLogger(Server.class);
    private GameState gameState = new GameState();
    private CommandPerformer commandPerformer = new CommandPerformer();

    public Server() {
        try {
            log.info("Creating server socket on {}", SERVER_PORT);
            socket = new ServerSocket(SERVER_PORT);
            serverIsRunning = true;
            socketsColors.add(StackColor.WHITE);
            socketsColors.add(StackColor.BLACK);
            acceptNewClients();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptNewClients() throws IOException {
        log.info("Accepting clients connections");
        while (serverIsRunning) {
            if (sockets.size() < 2) {
                Socket clientSocket = socket.accept();
                log.info("New client has connected");
                gameState.setNumberOfConnectedUsers(gameState.getNumberOfConnectedUsers() + 1);
                commandPerformer.setSockets(sockets);
                serveClient(clientSocket);
            }
            else {
                Socket clientSocket = socket.accept();
                ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                oos.writeObject(null);
                oos.flush();
            }
        }
    }

    private void serveClient(Socket clientSocket) {
        sockets.add(clientSocket);
        log.info("Starting new thread for client");
        clientsExecutor.execute(() -> {
            try {
                log.info("Reading commands from client");
                Scanner scanner = new Scanner(clientSocket.getInputStream());
                while (scanner.hasNext()) {
                    String command = scanner.next();
                    System.out.println(command);
                    gameState = commandPerformer.performCommand(command, clientSocket, gameState);
                    sendGameStateToClients();
                }
            } catch (IOException e) {
                log.error("Error while reading commands from client");
                throw new RuntimeException(e);
            }
        });
    }

//    private void performCommand(String command, Socket clientSocket) {
//        log.info("Received command " + command);
//        if (command.equals("rollDice")) {
//            performRollDice();
//        } else if (!isAnyPopped) {
//            performPop(command, clientSocket);
//        } else if (isAnyPopped) {
//            performPush(command, clientSocket);
//        }
//        sendGameStateToClients();
//    }

//    private void performPop(String command, Socket clientSocket) {
//        if (checkIsFromBeatenQueue()) return;
//
//        int stackIndex = Integer.parseInt(command);
//
//        if (socketsColors.get(sockets.indexOf(clientSocket)).equals(currentColor)
//                && gameState.getStack(stackIndex).getStackColor().equals(currentColor)
//                && gameState.getIsDiceUsed().contains(false)) {
//            gameState.popStack(stackIndex);
//            isAnyPopped = true;
//            originStackNumberOfCheckers = stackIndex;
//            originStackColor = gameState.getStack(stackIndex).getStackColor();
//        }
//    }

//    private boolean checkIsFromBeatenQueue() {
//        if (gameState.getBeatenBlackCheckers() > 0 && currentColor == StackColor.BLACK) {
//            originStackNumberOfCheckers = 25;
//            for (int i = 24; i <= 18; i--) {
//                if (new GameRules().canMoveTo(gameState.getStack(i), currentColor)) {
//                    isAnyPopped = true;
//                    return true;
//                }
//            }
//            // If checker can't make any move, set all dices as used and finish the round
//            for (int j = 0; j < 4; j++) {
//                gameState.setIsDiceUsed(j);
//            }
//            isAnyPopped = false;
//            return true;
//        }
//        if (gameState.getBeatenWhiteCheckers() > 0 && currentColor == StackColor.WHITE) {
//            originStackNumberOfCheckers = 0;
//            for (int i = 1; i <= 6; i++) {
//                if (new GameRules().canMoveTo(gameState.getStack(i), currentColor)) {
//                    isAnyPopped = true;
//                    return true;
//                }
//            }
//            // If checker can't make any move, set all dices as used and finish the round
//            for (int j = 0; j < 4; j++) {
//                gameState.setIsDiceUsed(j);
//            }
//            isAnyPopped = false;
//            return true;
//        }
//        return false;
//    }

//    private void performPush(String command, Socket clientSocket) {
//        System.out.println(originStackNumberOfCheckers);
//        System.out.println("Kolor: " + originStackColor);
//        int stackIndex = Integer.parseInt(command);
//        if (new GameRules().canMoveTo(gameState.getStack(stackIndex), currentColor)) {
//            tryToPush(stackIndex);
//        } else {
//            if ( !(gameState.getBeatenWhiteCheckers() > 0 && currentColor == StackColor.WHITE)
//                && !(gameState.getBeatenBlackCheckers() > 0 && currentColor == StackColor.BLACK))
//                gameState.pushStack(originStackNumberOfCheckers, originStackColor);
//            System.out.println("odkladam");
//            isAnyPopped = false;
//        }
//        if (!gameState.getIsDiceUsed().contains(false)) {
//            if (currentColor.equals(StackColor.WHITE))
//                currentColor = StackColor.BLACK;
//            else
//                currentColor = StackColor.WHITE;
//            gameState.setTimeToRollDice(true);
//        }
//    }

//    private void tryToPush(int stackIndex) {
//        int diceNumberIndex;
//        boolean isFromBeatenQueue;
//        int tempStackForQueue = originStackNumberOfCheckers;
//        isFromBeatenQueue = isFromBeatenQueue();
//        diceNumberIndex = getDiceNumberIndex(stackIndex);
//
//        if (diceNumberIndex == -1 ) {
//            gameState.pushStack(tempStackForQueue);
//            isAnyPopped = false;
//            return;
//        }
//
//
//        if (!new GameRules().hasSingleOpponentChecker(
//                gameState.getStack(stackIndex),
//                currentColor)) {
//            System.out.println("single opponent" + currentColor.toString());
//            gameState.pushStack(stackIndex, currentColor);
//        }
//        else {
//            System.out.println("beat it" + currentColor.toString());
//            System.out.println("Stack: " + gameState.getStack(stackIndex).getStackColor());
//            System.out.println(new GameRules().hasSingleOpponentChecker(
//                    gameState.getStack(stackIndex),
//                    getOpponentColor(currentColor)));
//            beatTheChecker(stackIndex);
//        }
//
//        gameState.getStack(stackIndex).setStackColor(currentColor);
//
//        if (isFromBeatenQueue) {
//            popCheckerFromBeatenQueue();
//        }
//
//        gameState.setIsDiceUsed(diceNumberIndex);
//        isAnyPopped = false;
//    }

//    private void popCheckerFromBeatenQueue() {
//        if (currentColor == StackColor.WHITE)
//            gameState.removeBeatenWhiteCheckers();
//        else
//            gameState.removeBeatenBlackCheckers();
//    }
//
//    private void beatTheChecker(int stackIndex) {
//        if (currentColor == StackColor.WHITE) {
//            gameState.addBeatenBlackCheckers();
//            gameState.getStack(stackIndex).setStackColor(StackColor.WHITE);
//        }
//        else {
//            gameState.addBeatenWhiteCheckers();
//            gameState.getStack(stackIndex).setStackColor(StackColor.BLACK);
//        }
//    }
//
//    private boolean isFromBeatenQueue() {
//        boolean isFromBeatenQueue;
//        if (gameState.getBeatenBlackCheckers() > 0 && currentColor == StackColor.BLACK) {
//            originStackNumberOfCheckers = 25;
//            isFromBeatenQueue = true;
//            return isFromBeatenQueue;
//        }
//        if (gameState.getBeatenWhiteCheckers() > 0 && currentColor == StackColor.WHITE) {
//            originStackNumberOfCheckers = 0;
//            isFromBeatenQueue = true;
//        } else
//            isFromBeatenQueue = false;
//        return isFromBeatenQueue;
//    }

//    private int getDiceNumberIndex(int stackIndex) {
//        int diceNumberIndex = -1;
//        for (int i = 0; i < gameState.getDice().size(); i++) {
//            if (gameState.getDice().get(i).equals(stackIndex - originStackNumberOfCheckers)
//                    && !gameState.getIsDiceUsed().get(i)
//                    && currentColor == StackColor.WHITE) {
//                diceNumberIndex = i;
//                break;
//            } else if (gameState.getDice().get(i).equals(originStackNumberOfCheckers - stackIndex)
//                    && !gameState.getIsDiceUsed().get(i)
//                    && currentColor == StackColor.BLACK) {
//                diceNumberIndex = i;
//                break;
//            }
//        }
//        return diceNumberIndex;
//    }


//    private StackColor getOpponentColor(StackColor color) {
//        if (color == StackColor.BLACK) {
//            return StackColor.WHITE;
//        }
//        return StackColor.BLACK;
//    }

//    private void performRollDice() {
//        gameState.rollDice();
//        gameState.setTimeToRollDice(false);
//    }

    private void sendGameStateToClients() {
        try {
            log.info("Sending gameState to clients");
            for (Socket clientSocket :
                    sockets) {
                ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                oos.writeObject(gameState);
                oos.flush();
            }
        } catch (IOException e) {
            log.error("Error while sending gameState");
            throw new RuntimeException(e);
        }
    }

    public void shutdownServer() {
        serverIsRunning = false;
    }

}
