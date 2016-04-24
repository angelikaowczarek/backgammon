package com.github.angelikaowczarek.backgammon.client;

import com.github.angelikaowczarek.backgammon.client.drawing.BoardDrawer;
import com.github.angelikaowczarek.backgammon.client.drawing.ImageService;
import com.githum.angelikaowczarek.backgammon.game.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BoardPanel extends JPanel{
    private ImageService imageService;
    private BoardDrawer boardDrawer;
    private GameState gameState;

    public BoardPanel(GameState gameState) {
        imageService = new ImageService();
        this.gameState = gameState;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D) g;
        boardDrawer = new BoardDrawer(canvas, gameState,imageService);
        boardDrawer.drawBoard();
    }

    public void updateGameState(GameState gameState) {
        this.gameState = gameState;
        this.repaint();
    }
}
