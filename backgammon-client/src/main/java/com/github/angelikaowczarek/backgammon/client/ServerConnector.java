package com.github.angelikaowczarek.backgammon.client;

import com.githum.angelikaowczarek.backgammon.game.GameState;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.Observable;

public class ServerConnector extends Observable {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ServerConnector.class);
    private Socket socket;
    private Writer serverWriter;
    private ObjectInputStream ois;

    @Override
    public void notifyObservers(Object arg) {
        super.setChanged();
        super.notifyObservers(arg);
    }

    public void connect(String host, int port) {
        log.info("Connecting to {}:{}", host, port);
        try {
            socket = new Socket(host, port);
            serverWriter = createServerWriter(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread receivingThread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        ois = new ObjectInputStream(socket.getInputStream());
                        log.info("Receiving gameState from server");
                        notifyObservers(ois.readObject());
                    } catch (IOException ex) {
                        notifyObservers(ex);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        receivingThread.start();
    }

    public Socket getSocket() {
        return socket;
    }

    public void sendCommand(String command) {
        try {
            log.info("Sending command {} to server", command);
            serverWriter.write(command);
            serverWriter.flush();
        } catch (IOException e) {
            log.error("Error while sending command");
            throw new RuntimeException(e);
        }
    }

    public GameState receiveGameState() throws IOException, ClassNotFoundException {
        ois = new ObjectInputStream(socket.getInputStream());
        log.info("Receiving gameState from server");
        return (GameState) ois.readObject();
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
