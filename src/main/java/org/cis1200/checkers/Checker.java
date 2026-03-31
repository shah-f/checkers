package org.cis1200.checkers;

public class Checker {

    private final boolean isRed;
    private int row;
    private int col;
    private boolean isKing;

    public Checker(boolean isRed, int row, int col) {
        this.isRed = isRed;
        this.row = row;
        this.col = col;
        this.isKing = false;
    }

    public Checker(Checker other) {
        this.isRed = other.isRed;
        this.row = other.row;
        this.col = other.col;
        this.isKing = other.isKing;
    }

    public boolean isRed() {
        return isRed;
    }
    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
    public void setCol(int col) {
        this.col = col;
    }
    public void setRow(int row) {
        this.row = row;
    }
    public boolean isKing() {
        return isKing;
    }
    public void makeKing() {
        isKing = true;
    }
}
