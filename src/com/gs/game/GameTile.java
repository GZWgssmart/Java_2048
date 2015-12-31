package com.gs.game;

import java.awt.*;

/**
 * Created by WangGenshen on 12/29/15.
 */
public class GameTile {

    private int index;
    private int number;
    private boolean isMerged;
    private int moveTime;
    private Object component;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isMerged() {
        return isMerged;
    }

    public void setMerged(boolean merged) {
        isMerged = merged;
    }

    public int getMoveTime() {
        return moveTime;
    }

    public void setMoveTime(int moveTime) {
        this.moveTime = moveTime;
    }

    public Object getComponent() {
        return component;
    }

    public void setComponent(Object component) {
        this.component = component;
    }

    public Color getBkColor() {
        Color bk = new Color(0xFFD155);
        switch(number) {
            case 2:
                bk = new Color(0xDFD3DF);
                break;
            case 4:
                bk = new Color(0xFFED9B);
                break;
            case 8:
                bk = new Color(0xFFBD83);
                break;
            case 16:
                bk = new Color(0xFFAA45);
                break;
            case 32:
                bk = new Color(0xFF663A);
                break;
            case 64:
                bk = new Color(0xFF2C31);
                break;
            case 128:
                bk = new Color(0xFFE517);
                break;
        }
        return bk;
    }
}
