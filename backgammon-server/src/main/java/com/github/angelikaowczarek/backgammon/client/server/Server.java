package com.github.angelikaowczarek.backgammon.client.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Server {
    private static final int SERVER_PORT = 8789;
    private ServerSocket socket;
    private boolean serverIsRunning;
    private Executor clientsExecutor = Executors.newCachedThreadPool();
    private static final Logger log = LoggerFactory.getLogger(Server.class);

    public Server() {
        try {
            log.info("Creating server socket on {}", SERVER_PORT);
            socket = new ServerSocket(SERVER_PORT);
            serverIsRunning = true;
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
        log.info("Starting new thread for client");
        clientsExecutor.execute(() -> {
            try {
                log.info("Reading commands from client");
                Scanner scanner = new Scanner(clientSocket.getInputStream());
                while (scanner.hasNext()) {
                    String command = scanner.next();
                    performCommand(command);
                }
            } catch (IOException e) {
                log.error("Error while reading commands from client");
                throw new RuntimeException(e);
            }
        });
    }

    private void performCommand(String command) {
        log.info("Received command" + command);
        // TODO
    }

    public void shutdownServer() {
        serverIsRunning = false;
    }

}
