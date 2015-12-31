package com.gs.game;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by WangGenshen on 12/29/15.
 */
public class GameCanvas extends JFrame {

    private GameCore gameCore;
    private JPanel gamePanel;

    private JLabel scoreLbl;

    public GameCanvas() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        setSize(410, 450);
        setLocation(200, 200);
        setTitle("2048");
        getContentPane().setLayout(new BorderLayout());
        initGame();
        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public JPanel getGamePanel() {
        return gamePanel;
    }

    public JLabel getScoreLbl() {
        return scoreLbl;
    }

    public void initGame() {
        gameCore = new GameCore();
        gameCore.initGameTiles();
        JPanel topPanel = new JPanel(new FlowLayout());
        scoreLbl = new JLabel("0");
        topPanel.add(scoreLbl);
        gamePanel = new JPanel(null);
        initTiles();
        GameKeyListener gameKeyListener = new GameKeyListener();
        gameKeyListener.setGameCore(gameCore);
        gameKeyListener.setGameCanvas(this);
        gamePanel.addKeyListener(gameKeyListener);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();
        add(topPanel, BorderLayout.NORTH);
        add(gamePanel, BorderLayout.CENTER);
    }

    private void initTiles() {
        List<GameTile> tiles = gameCore.getTiles();
        for(int row = 0; row < 4; row++) {
            for(int col = 0; col < 4; col++) {
                int index = gameCore.getIndex(row, col);
                GameTile tile = tiles.get(index);
                JLabel lbl = new JLabel();
                lbl.setBorder(BorderFactory.createLineBorder(Color.WHITE, 4));
                lbl.setBounds(5 + col * Constants.GRID_SIZE, row * Constants.GRID_SIZE, Constants.GRID_SIZE, Constants.GRID_SIZE);
                gamePanel.add(lbl);
                JButton btn = new JButton("");
                btn.setOpaque(true);
                btn.setFont(new Font("微软雅黑", 1, 22));
                btn.setBounds(5 + col * Constants.GRID_SIZE + 4, row * Constants.GRID_SIZE + 4, Constants.GRID_SIZE - 8, Constants.GRID_SIZE - 8);
                if(tile.getNumber() == 2) {
                    btn.setBackground(tile.getBkColor());
                    btn.setText("" + tile.getNumber());
                    gamePanel.add(btn);
                }
                tile.setComponent(btn);
            }
        }
    }

    public static void main(String... args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new GameCanvas();
                } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
