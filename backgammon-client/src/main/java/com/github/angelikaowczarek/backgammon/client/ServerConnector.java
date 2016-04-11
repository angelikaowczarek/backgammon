package com.github.angelikaowczarek.backgammon.client;

import com.githum.angelikaowczarek.backgammon.game.GameState;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

public class ServerConnector {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ServerConnector.class);
    private Socket socket;
    private Writer serverWriter;

    public void connect(String host, int port) {
        log.info("Connecting to {}:{}", host, port);
        try {
            socket = new Socket(host, port);
            serverWriter = createServerWriter(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendCommand(String command) {
        try {
            log.info("Sending command {} to server", command);
            serverWriter.write(command);
        } catch (IOException e) {
            log.error("Error while sending command");
            throw new RuntimeException(e);
        }
    }

    public void receiveGameState(GameState gameState) {
        // TODO
        log.info("Receiving gameState from server");
    }

    private OutputStreamWriter createServerWriter(Socket socket) {
        try {
            log.info("Creating writer to server");
            return new OutputStreamWriter(socket.getOutputStream());
        } catch (IOException e) {
            log.error("Error while creating server writer");
            throw new RuntimeException(e);
        }
    }
}
