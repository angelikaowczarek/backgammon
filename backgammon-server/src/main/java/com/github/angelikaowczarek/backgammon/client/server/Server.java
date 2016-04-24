package com.github.angelikaowczarek.backgammon.client.server;

import com.githum.angelikaowczarek.backgammon.game.GameState;
import com.githum.angelikaowczarek.backgammon.game.StackColor;
import com.githum.angelikaowczarek.backgammon.game.StackState;
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

//TODO:
// wysyłanie GameState

public class Server {
    private static final int SERVER_PORT = 1342;
    private ServerSocket socket;
    private ArrayList<Socket> sockets = new ArrayList<>();
    private ArrayList<StackColor> socketsColors = new ArrayList<>();
    private StackColor currentColor = StackColor.WHITE;
    private boolean serverIsRunning;
    private boolean isAnyPopped = false;
    private int originStack = -1;
    private Executor clientsExecutor = Executors.newCachedThreadPool();
    private static final Logger log = LoggerFactory.getLogger(Server.class);
    private GameState gameState = new GameState();

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
            Socket clientSocket = socket.accept();
            log.info("New client has connected");
            serveClient(clientSocket);
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
                    performCommand(command, clientSocket);
                }
            } catch (IOException e) {
                log.error("Error while reading commands from client");
                throw new RuntimeException(e);
            }
        });
    }

    private void performCommand(String command, Socket clientSocket) {
        log.info("Received command " + command);
        if (command.equals("rollDice")) {
            performRollDice();
        } else if (!isAnyPopped) {
            performPop(command, clientSocket);
        } else if (isAnyPopped) {
            performPush(command, clientSocket);
        }
        sendGameStateToClients();
    }

    private void performPop(String command, Socket clientSocket) {
        if (gameState.getBeatenBlackCheckers() > 0 && currentColor == StackColor.BLACK) {
            for (int i = 1; i <= 6; i++) {
                if (new GameRules().canMoveTo(gameState.getStack(i), currentColor)) {
                    isAnyPopped = false;
                    return;
                }
            }
            isAnyPopped = true;
            return;
        }
        if (gameState.getBeatenWhiteCheckers() > 0 && currentColor == StackColor.WHITE) {
            for (int i = 24; i <= 18; i--) {
                if (new GameRules().canMoveTo(gameState.getStack(i), currentColor)) {
                    isAnyPopped = false;
                    return;
                }
            }
            isAnyPopped = true;
            return;
        }

        int stackIndex = Integer.parseInt(command);
        if (socketsColors.get(sockets.indexOf(clientSocket)).equals(currentColor)
                && gameState.getStack(stackIndex).getStackColor().equals(currentColor)
                && gameState.getIsDiceUsed().contains(false)) {
            System.out.println(gameState.getIsDiceUsed().toString());
            gameState.popStack(stackIndex);
            isAnyPopped = true;
            originStack = stackIndex;
        }
    }

    private void performPush(String command, Socket clientSocket) {
        int stackIndex = Integer.parseInt(command);
        if (new GameRules().canMoveTo(gameState.getStack(stackIndex), currentColor)) {
            tryToPush(stackIndex);
        } else {
            if ( !(gameState.getBeatenWhiteCheckers() > 0 && currentColor == StackColor.WHITE)
                && !(gameState.getBeatenBlackCheckers() > 0 && currentColor == StackColor.BLACK))
                gameState.pushStack(originStack);
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
        int diceNumberIndex = -1;
        boolean isFromBeatenQueue;
        int tempStackForQueue = originStack;
        isFromBeatenQueue = checkIsFromBeatenQueue();
        diceNumberIndex = getDiceNumberIndex(stackIndex, diceNumberIndex);

        if (diceNumberIndex == -1 ) {
            gameState.pushStack(tempStackForQueue);
            isAnyPopped = false;
            return;
        }

        gameState.getStack(stackIndex).setStackColor(currentColor);

        if (!new GameRules().hasSingleOpponentChecker(
                gameState.getStack(stackIndex), getOpponentColor(currentColor))) {
            gameState.pushStack(stackIndex);
        } else {
            if (currentColor == StackColor.WHITE) {
                gameState.addBeatenBlackCheckers();
                gameState.getStack(stackIndex).setStackColor(StackColor.WHITE);
            }
            else {
                gameState.addBeatenWhiteCheckers();
                gameState.getStack(stackIndex).setStackColor(StackColor.BLACK);
            }
        }

        if (isFromBeatenQueue) {
            if (currentColor == StackColor.WHITE)
                gameState.removeBeatenWhiteCheckers();
            else
                gameState.removeBeatenBlackCheckers();
        }

        gameState.setIsDiceUsed(diceNumberIndex);
        isAnyPopped = false;
    }

    private boolean checkIsFromBeatenQueue() {
        boolean isFromBeatenQueue;
        if (gameState.getBeatenBlackCheckers() > 0 && currentColor == StackColor.BLACK) {
            originStack = 25;
            isFromBeatenQueue = true;
            return isFromBeatenQueue;
        }
        if (gameState.getBeatenWhiteCheckers() > 0 && currentColor == StackColor.WHITE) {
            originStack = 0;
            isFromBeatenQueue = true;
        } else
            isFromBeatenQueue = false;
        return isFromBeatenQueue;
    }

    private int getDiceNumberIndex(int stackIndex, int diceNumberIndex) {
        for (int i = 0; i < gameState.getDice().size(); i++) {
            if (gameState.getDice().get(i).equals(stackIndex - originStack)
                    && !gameState.getIsDiceUsed().get(i)
                    && currentColor == StackColor.WHITE) {
                diceNumberIndex = i;
                break;
            } else if (gameState.getDice().get(i).equals(originStack - stackIndex)
                    && !gameState.getIsDiceUsed().get(i)
                    && currentColor == StackColor.BLACK) {
                diceNumberIndex = i;
                break;
            }
        }
        return diceNumberIndex;
    }

    private void performRollDice() {
        gameState.rollDice();
        gameState.setTimeToRollDice(false);
    }

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

    private StackColor getOpponentColor(StackColor color) {
        if (color == StackColor.BLACK) {
            return StackColor.WHITE;
        }
        return StackColor.BLACK;
    }

    public void shutdownServer() {
        serverIsRunning = false;
    }

}
