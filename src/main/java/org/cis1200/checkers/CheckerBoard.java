package org.cis1200.checkers;

public class CheckerBoard {

    private Checker[][] board;

    public CheckerBoard() {
        reset();
    }

    public CheckerBoard(CheckerBoard other) {
        this.board = new Checker[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (other.board[i][j] != null) {
                    this.board[i][j] = new Checker(other.board[i][j]);
                }
            }
        }
    }

    public void reset() {
        board = new Checker[8][8];
        for (int i = 0; i < board.length; i++) {
            for (int j = (i % 2) ; j < board[0].length; j += 2) {
                if (i < 3) {
                    Checker temp = new Checker(true, i, j);
                    board[i][j] = temp;
                }
                if (i > 4) {
                    Checker temp = new Checker(false, i, j);
                    board[i][j] = temp;
                }
            }
        }
    }


    public Checker getChecker(int row, int col) {
        if (board[row][col] == null) {
            return null;
        }
        return board[row][col];
    }

    public void setChecker(int row, int col, Checker checker) {
        board[row][col] = checker;
        if (checker != null) {
            checker.setRow(row);
            checker.setCol(col);
        }
    }

}
