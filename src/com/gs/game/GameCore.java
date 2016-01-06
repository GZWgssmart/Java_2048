package com.gs.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by WangGenshen on 12/29/15.
 */
public class GameCore {

    private List<GameTile> tiles;

    private int score;

    public List<GameTile> getTiles() {
        return tiles;
    }

    public int getScore() {
        return score;
    }

    public GameCore() {
        tiles = new ArrayList<>();
    }

    /**
     * 初始化所有格子,并只需要初始化两个为2的格子
     */
    public void initGameTiles() {
        score = 0;
        tiles.clear();
        Random random = new Random();
        int first = random.nextInt(16);
        int second = random.nextInt(16);
        while (first == second) {
            second = random.nextInt(16);
        }
        for(int i = 0; i < 16; i++) {
            GameTile tile = new GameTile();
            tile.setIndex(i);
            if(i == first || i == second) {
                tile.setNumber(2);
            }
            tiles.add(tile);
        }
    }

    public int getIndex(int row, int col) {
        return row * 4 + col;
    }

    public int getRow(int index) {
        return index / 4 - 1;
    }

    public int getCol(int index) {
        return index % 4 - 1;
    }

    /**
     * 把所有GameTile设置为未合并状态
     */
    public void initMerge() {
        for(int i = 0; i < 16; i++) {
            tiles.get(i).setMerged(false);
        }
    }

    /**
     * 把所有GameTile的移动次数设置为0
     */
    public void initMoveTime() {
        for (int i = 0; i < 16; i++) {
            tiles.get(i).setMoveTime(0);
        }
    }

    /**
     * 对GameTile进行合并,合并规则如下:
     * 1. 当向上移动时,从第1行开始，与上一行同一列的数进行比较,如果相待，则合并，如果上一个数为0,则直接移动到上一个数,当移动合,上一个格子需要设置为已合并状态isMerged返回true,
     * 并计算移动的次数
     * @param fromIndex
     * @param toIndex
     * @param oTile
     */
    private void merge(int fromIndex, int toIndex, GameTile oTile) {
        GameTile fromTile = tiles.get(fromIndex);
        GameTile toTile = tiles.get(toIndex);
        int fromNum = fromTile.getNumber();
        int toNum = toTile.getNumber();
        if (fromNum == toNum && fromNum != 0 && !fromTile.isMerged() && !toTile.isMerged()) {
            toTile.setNumber(2 * fromNum);
            toTile.setMerged(true);
            fromTile.setNumber(0);
            oTile.setMoveTime(oTile.getMoveTime() + 1);
            score += 2 * fromNum;
        } else if (toNum == 0 && fromNum != toNum) {
            toTile.setNumber(fromNum);
            fromTile.setNumber(0);
            oTile.setMoveTime(oTile.getMoveTime() + 1);
        }
    }

    /**
     * 往上合并时,第一行需要合并的计数是该行到第0行的次数
     * @param row
     * @param col
     */
    private void toMergeUp(int row, int col) {
        GameTile oTile = tiles.get(getIndex(row, col));
        for (int i = row; i > 0; i--) {
            int toIndex = getIndex(i - 1, col); // 需要被合并的索引号
            int fromIndex = getIndex(i, col);
            merge(fromIndex, toIndex, oTile);
        }
    }

    /**
     * 往上合并时,从第一行开始与上一行进行合并,每一行需要合并的次数行该行与第0行之间的行数差
     */
    public void toMergeUp() {
        for (int row = 1; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                toMergeUp(row, col);
            }
        }
    }

    private void toMergeLeft(int row, int col) {
        GameTile oTile = tiles.get(getIndex(row, col));
        for (int i = col; i > 0; i--) {
            int toIndex = getIndex(row, i - 1);
            int fromIndex = getIndex(row, i);
            merge(fromIndex, toIndex, oTile);
        }
    }

    public void toMergeLeft() {
        for (int col = 1; col < 4; col++) {
            for (int row = 0; row < 4; row++) {
                toMergeLeft(row, col);
            }
        }
    }

    private void toMergeDown(int row, int col) {
        GameTile oTile = tiles.get(getIndex(row, col));
        for(int i = 3 - row; i > 0; i--) {
            int toIndex = getIndex(4 - i, col);
            int fromIndex = getIndex(3 - i, col);
            merge(fromIndex, toIndex, oTile);
        }
    }

    public void toMergeDown() {
        for (int row = 2; row >= 0; row--) {
            for(int col = 0; col < 4; col++) {
                toMergeDown(row, col);
            }
        }
    }

    private void toMergeRight(int row, int col) {
        GameTile oTile = tiles.get(getIndex(row, col));
        for(int i = 3 - col; i > 0; i--) {
            int toIndex = getIndex(row, 4 - i);
            int fromIndex = getIndex(row, 3 - i);
            merge(fromIndex, toIndex, oTile);
        }
    }

    public void toMergeRight() {
        for (int col = 2; col >= 0; col--) {
            for(int row = 0; row < 4; row++) {
                toMergeRight(row, col);
            }
        }
    }

    /**
     * 当按了方向键后,所有的GameTile会进行重置，此时需要在空白格子内找一个格子生成新的GameTile
     * @return
     */
    public GameTile newTile() {
        List<Integer> empties = new ArrayList<>();
        for(int i = 0; i < 16; i++) {
            if(tiles.get(i).getNumber() == 0) {
                empties.add(i);
            }
        }
        Random random = new Random();
        int index = empties.get(random.nextInt(empties.size()));
        GameTile tile = tiles.get(index);
        tile.setNumber(2);
        return tile;
    }

    /**
     * 判断是否能移动，当往上移动时,如果当前格子数大于0,则判断如下：如果每个格子的上方与当前格子相同,则可以合并，如果每个格子的上方为0,则可以合并
     * 不同的移动方向,当前格子的上一个格子的索引号不一样,则需要根据方向来获取上一个格子的行列
     * @param fromRow
     * @param fromCol
     * @param key
     * @return
     */
    private boolean canMove(int fromRow, int fromCol, int key) {
        int toRow = 0, toCol = 0;
        switch(key) {
            case Constants.KEY_UP:
                toRow = fromRow - 1;
                toCol = fromCol;
                break;
            case Constants.KEY_LEFT:
                toRow = fromRow;
                toCol = fromCol - 1;
                break;
            case Constants.KEY_DOWN:
                toRow = fromRow + 1;
                toCol = fromCol;
                break;
            case Constants.KEY_RIGHT:
                toRow = fromRow;
                toCol = fromCol + 1;
                break;
        }
        int fromNum = tiles.get(getIndex(fromRow, fromCol)).getNumber();
        if(fromNum > 0) {
            int toNum = tiles.get(getIndex(toRow, toCol)).getNumber();
            if(toNum == 0 || fromNum == toNum) {
                return true;
            }
        }
        return false;
    }

    public boolean canMoveUp() {
        for(int row = 3; row > 0; row--) {
            for(int col = 0; col < 4; col++) {
                if(canMove(row, col, Constants.KEY_UP)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canMoveLeft() {
        for(int col = 3; col > 0; col--) {
            for(int row = 0; row < 4; row++) {
                if(canMove(row, col, Constants.KEY_LEFT)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canMoveDown() {
        for(int row = 0; row < 3; row++) {
            for(int col = 0; col < 4; col++) {
                if(canMove(row, col, Constants.KEY_DOWN)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canMoveRight() {
        for(int col = 0; col < 3; col++) {
            for(int row = 0; row < 4; row++) {
                if(canMove(row, col, Constants.KEY_RIGHT)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean gameOver() {
        for(int i = 0; i < 16; i++) {
            if(tiles.get(i).getNumber() > 0) {
                if(canMoveUp() || canMoveLeft() || canMoveDown() || canMoveRight()) {
                    return false;
                }
            }
        }
        return true;
    }

}
