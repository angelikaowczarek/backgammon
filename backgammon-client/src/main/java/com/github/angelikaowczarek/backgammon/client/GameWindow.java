package com.github.angelikaowczarek.backgammon.client;

import com.github.angelikaowczarek.backgammon.client.drawing.BoardDrawer;

import javax.swing.*;

public class GameWindow extends JFrame {
    private GameController gameController = new GameController();

    public GameWindow() {
        super("Backgammon - Angelika Owczarek");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(735, 522);
        setResizable(false);
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
