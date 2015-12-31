package com.gs.game;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

/**
 * Created by WangGenshen on 12/29/15.
 */
public class GameKeyListener implements KeyListener {

    private GameCanvas gameCanvas;
    private GameCore gameCore;

    public void setGameCanvas(GameCanvas gameCanvas) {
        this.gameCanvas = gameCanvas;
    }

    public void setGameCore(GameCore gameCore) {
        this.gameCore = gameCore;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case Constants.KEY_UP:
                mergeUp();
                break;
            case Constants.KEY_DOWN:
                mergeDown();
                break;
            case Constants.KEY_LEFT:
                mergeLeft();
                break;
            case Constants.KEY_RIGHT:
                mergeRight();
                break;
            default:
                break;
        }
        if(gameCore.gameOver()) {
            if(e.getKeyCode() != Constants.KEY_ENTER) {
                JOptionPane.showMessageDialog(gameCanvas.getContentPane(), "游戏结束,您的分数为: " + gameCore.getScore(), "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    /**
     * 把当前的btn从gamePanel中移除，并更新被合并的button为新内容,包括新的number和color
     * @param fromRow
     * @param fromCol
     * @param key
     * @param tiles
     * @param gamePanel
     */
    private void merge(int fromRow, int fromCol, int key, List<GameTile> tiles, JPanel gamePanel) {
        int toRow = 0, toCol = 0;
        GameTile fromTile = tiles.get(gameCore.getIndex(fromRow, fromCol));
        int moveTime = fromTile.getMoveTime();
        if (moveTime > 0) {
            if (key == Constants.KEY_UP) {
                toRow = fromRow - moveTime;
                toCol = fromCol;
            } else if (key == Constants.KEY_LEFT) {
                toRow = fromRow;
                toCol = fromCol - moveTime;
            } else if (key == Constants.KEY_DOWN) {
                toRow = fromRow + moveTime;
                toCol = fromCol;
            } else if (key == Constants.KEY_RIGHT) {
                toRow = fromRow;
                toCol = fromCol + moveTime;
            }
            GameTile toTile = tiles.get(gameCore.getIndex(toRow, toCol));
            JButton toBtn = (JButton) toTile.getComponent();
            toBtn.setText("" + toTile.getNumber());
            toBtn.setBackground(toTile.getBkColor());
            JButton fromBtn = (JButton) fromTile.getComponent();
            fromBtn.setText("");
            gamePanel.remove(fromBtn);
            gamePanel.add(toBtn);
        }
    }

    public void mergeUp() {
        if (gameCore.canMoveUp()) {
            gameCore.toMergeUp();
            List<GameTile> tiles = gameCore.getTiles();
            JPanel gamePanel = gameCanvas.getGamePanel();
            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 4; col++) {
                    merge(row, col, Constants.KEY_UP, tiles, gamePanel);
                }
            }
            updateGame(tiles, gamePanel);
        }
    }

    public void mergeLeft() {
        if (gameCore.canMoveLeft()) {
            gameCore.toMergeLeft();
            List<GameTile> tiles = gameCore.getTiles();
            JPanel gamePanel = gameCanvas.getGamePanel();
            for (int col = 0; col < 4; col++) {
                for (int row = 0; row < 4; row++) {
                    merge(row, col, Constants.KEY_LEFT, tiles, gamePanel);
                }
            }
            updateGame(tiles, gamePanel);
        }
    }

    public void mergeDown() {
        if (gameCore.canMoveDown()) {
            gameCore.toMergeDown();
            List<GameTile> tiles = gameCore.getTiles();
            JPanel gamePanel = gameCanvas.getGamePanel();
            for (int row = 3; row >= 0; row--) {
                for (int col = 0; col < 4; col++) {
                    merge(row, col, Constants.KEY_DOWN, tiles, gamePanel);
                }
            }
            updateGame(tiles, gamePanel);
        }
    }

    public void mergeRight() {
        if (gameCore.canMoveRight()) {
            gameCore.toMergeRight();
            List<GameTile> tiles = gameCore.getTiles();
            JPanel gamePanel = gameCanvas.getGamePanel();
            for (int col = 3; col >= 0; col--) {
                for (int row = 0; row < 4; row++) {
                    merge(row, col, Constants.KEY_RIGHT, tiles, gamePanel);
                }
            }
            updateGame(tiles, gamePanel);
        }
    }

    /**
     * 每次移动后,需要更新分数,并重新初始化所有GameTile的移动次数为0, isMerged设置为false,并重绘整个gamePanel
     * @param tiles
     * @param gamePanel
     */
    private void updateGame(List<GameTile> tiles, JPanel gamePanel) {
        JLabel scoreLbl = gameCanvas.getScoreLbl();
        if (Integer.valueOf(scoreLbl.getText()) != gameCore.getScore()) {
            scoreLbl.setText("" + gameCore.getScore());
        }
        GameTile newTile = gameCore.newTile();
        if (newTile != null) {
            JButton newTileBtn = (JButton) newTile.getComponent();
            newTileBtn.setBackground(newTile.getBkColor());
            newTileBtn.setText("" + newTile.getNumber());
            tiles.set(newTile.getIndex(), newTile);
            gamePanel.add(newTileBtn);
        }
        //scoreLbl.repaint();
        gamePanel.repaint();
        gameCore.initMerge();
        gameCore.initMoveTime();
    }

}
