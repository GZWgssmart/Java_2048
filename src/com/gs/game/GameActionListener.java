package com.gs.game;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by WangGenshen on 12/31/15.
 */
public class GameActionListener implements ActionListener {

    private GameCanvas gameCanvas;

    public void setGameCanvas(GameCanvas gameCanvas) {
        this.gameCanvas = gameCanvas;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source instanceof JButton) {
            JButton btn = (JButton) source;
            if (btn.getName().equals("newGame")) {
                gameCanvas.reInitGamePanel();
            }
        }
    }

}
