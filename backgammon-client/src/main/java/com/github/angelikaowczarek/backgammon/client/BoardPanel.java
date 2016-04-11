package com.github.angelikaowczarek.backgammon.client;

import com.github.angelikaowczarek.backgammon.client.drawing.BoardDrawer;
import com.github.angelikaowczarek.backgammon.client.drawing.ImageService;
import com.githum.angelikaowczarek.backgammon.game.GameState;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {
    private ImageService imageService = new ImageService();
    private BoardDrawer boardDrawer;
    private GameState gameState = new GameState();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D) g;
        boardDrawer = new BoardDrawer(canvas, gameState,imageService);
        boardDrawer.drawBoard();
    }
}
