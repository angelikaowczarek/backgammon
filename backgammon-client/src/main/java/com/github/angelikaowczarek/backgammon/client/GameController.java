package com.github.angelikaowczarek.backgammon.client;

public class GameController {
    private ServerConnector serverConnector;

    public GameController() {
        serverConnector = new ServerConnector();
        serverConnector.connect("127.0.0.1", 1342);
    }
}
