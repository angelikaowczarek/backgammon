package com.github.angelikaowczarek;

import com.github.angelikaowczarek.drawing.ImageService;

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
