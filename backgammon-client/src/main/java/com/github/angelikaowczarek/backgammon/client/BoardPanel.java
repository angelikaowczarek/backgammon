package com.github.angelikaowczarek.backgammon.client;

import com.github.angelikaowczarek.backgammon.client.drawing.ImageService;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {
    private ImageService imageService = new ImageService();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D) g;
        canvas.drawImage(imageService.getBackgroundImage(), 0, 0, null);
    }
}
