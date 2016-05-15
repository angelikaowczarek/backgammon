package com.github.angelikaowczarek.backgammon.client;

import com.github.angelikaowczarek.backgammon.client.drawing.BoardDrawer;
import com.githum.angelikaowczarek.backgammon.game.GameState;
import com.githum.angelikaowczarek.backgammon.game.StackColor;
import com.sun.glass.events.MouseEvent;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Observer;

public class GameWindow extends JFrame implements Observer{
    private GameController gameController = new GameController();
    private GameState gameState;
    private BoardPanel boardPanel;
    private StackColor clientColor;
    private boolean isColorSet = false;

    public GameWindow(){
        super("Backgammon - Angelika Owczarek");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(735, 522);
        setResizable(false);
        add(getBoardPanel());
        gameController.getServerConnector().addObserver(this);
//        JOptionPane.showMessageDialog(this,
//                "Zaczynamy?",
//                "Start",
//                JOptionPane.PLAIN_MESSAGE);
//        gameController.getServerConnector().sendCommand("START");
    }

    private BoardPanel getBoardPanel() {
        gameState = new GameState();
        boardPanel = new BoardPanel(gameState);
        boardPanel.addMouseListener(getMouseClickListener());
        return boardPanel;

    }

    private GameClickListener getMouseClickListener() {
        return new GameClickListener() {
            public void mousePressed(java.awt.event.MouseEvent e) {
                setMouseLocation(e);
                if ( isOnRollDiceButton() ) {
                    gameController.getServerConnector().sendCommand("rollDice\n");
                }
                int clickedStack = getClickedStack();
                String command = clickedStack+"\n";
                if ( clickedStack != -1 ) {
                    gameController.getServerConnector().sendCommand(command);
                }
            }
        };
    }

    @Override
    public void update(Observable o, Object arg) {
        final Object finalArg = arg;
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    boardPanel.updateGameState((GameState) arg);
//                    if (gameState.getNumberOfConnectedUsers() == 1 && isColorSet == false)
//                        clientColor = StackColor.WHITE;
//                    else if (gameState.getNumberOfConnectedUsers() == 2 && isColorSet == false)
//                        clientColor = StackColor.BLACK;
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
