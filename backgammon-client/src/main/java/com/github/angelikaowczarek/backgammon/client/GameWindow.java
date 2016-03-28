package com.github.angelikaowczarek.backgammon.client;

import javax.swing.*;

public class GameWindow extends JFrame {
    private GameController gameController = new GameController();

    public GameWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1024, 796);
        add(getBoardPanel());
    }

    private BoardPanel getBoardPanel() {
        BoardPanel boardPanel = new BoardPanel();
        boardPanel.addMouseListener(getMouseClickListener());
        return boardPanel;
    }

    private GameClickListener getMouseClickListener() {
        return new GameClickListener();
    }
}
