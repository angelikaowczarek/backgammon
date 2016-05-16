package com.github.angelikaowczarek.backgammon.client;

import com.github.angelikaowczarek.backgammon.client.drawing.BoardDrawer;
import com.github.angelikaowczarek.backgammon.client.drawing.ImageService;
import com.githum.angelikaowczarek.backgammon.game.GameState;
import com.githum.angelikaowczarek.backgammon.game.StackColor;

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
//        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
//                "Gratulacje! Wygrał gracz biały!",
//                "Koniec gry",
//                JOptionPane.PLAIN_MESSAGE);

        if (gameState.getWinner() == StackColor.WHITE) {
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                    "Gratulacje! Wygrał gracz biały!",
                    "Koniec gry",
                    JOptionPane.PLAIN_MESSAGE);
            System.exit(0);
        }

        if (gameState.getWinner() == StackColor.BLACK) {
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                    "Gratulacje! Wygrał gracz czarny!",
                    "Koniec gry",
                    JOptionPane.PLAIN_MESSAGE);
            System.exit(0);
        }
    }
}
