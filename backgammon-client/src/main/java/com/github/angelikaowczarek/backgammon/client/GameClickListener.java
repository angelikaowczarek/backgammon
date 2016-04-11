package com.github.angelikaowczarek.backgammon.client;

import lombok.Getter;

import java.awt.*;
import java.awt.event.MouseAdapter;

public class GameClickListener extends MouseAdapter {
    private Point mouseLocation;
    private PointerInfo pointerInfo;

    // pomocny kod:
    // mouseMoved() - MouseAdapter
    // getLocation() - PointerInfo

    // zamiana lokalizacji na konkretne pola?

    public Point getMouseLocation() {
        //TODO
        return null;
    }
}
