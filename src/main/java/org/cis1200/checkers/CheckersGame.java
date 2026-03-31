package org.cis1200.checkers;

import java.util.ArrayList;

public class CheckersGame {

    private CheckerBoard board;
    private GameHistory history;
    private boolean playerOneTurn;

    public CheckersGame() {
        this.board = new CheckerBoard();
        this.history = new GameHistory();
        this.playerOneTurn = true;
        history.add(new CheckerBoard());
    }

    public CheckersGame(CheckerBoard board, GameHistory history, boolean playerOneTurn) {
        this.board = board;
        this.history = history;
        this.playerOneTurn = playerOneTurn;
    }

    public boolean isPlayerOne() {
        return playerOneTurn;
    }

    public GameHistory getHistory() {
        return history;
    }

    public boolean isValidMove(Checker checker, int row, int col) {
        if (row > 7 || row < 0 || col > 7 || col < 0) {
            //cannot move off board
            return false;
        }
        if (checker.isRed() && playerOneTurn && !checker.isKing()) {
            if (checker.getRow() >= row) {
                //cannot move backward
                return false;
            }
            if (row - checker.getRow() != 1 || Math.abs(col - checker.getCol()) != 1) {
                return false;
            }
            return board.getChecker(row, col) == null;
        }
        if (!checker.isRed() && !playerOneTurn && !checker.isKing()) {
            if (checker.getRow() <= row) {
                return false;
            }
            if (checker.getRow() - row != 1 || Math.abs(checker.getCol() - col) != 1) {
                return false;
            }
            return board.getChecker(row, col) == null;
        }
        if (checker.isKing() && ((checker.isRed() && playerOneTurn) ||
                (!checker.isRed() && !playerOneTurn))) {
            if (Math.abs(checker.getRow() - row) != 1 || Math.abs(checker.getCol() - col) != 1) {
                return false;
            }
            return board.getChecker(row, col) == null;
        }
        return false;
    }

    public void moveChecker(int startRow, int startCol, int row, int col) {
        Checker checker = board.getChecker(startRow, startCol);
        if (checker != null) {
            if (isValidMove(checker, row, col)) {
                CheckerBoard old = new CheckerBoard(board);
                history.add(old);
                board.setChecker(row, col, checker);
                board.setChecker(startRow, startCol, null);
                tryMakeKing(row, col);
                playerOneTurn = !playerOneTurn;
                CheckerBoard newie = new CheckerBoard(board);
                history.add(newie);
            }
        }
    }

    private boolean isValidJump(Checker checker, int row, int col) {
        if (row > 7 || row < 0 || col > 7 || col < 0) {
            return false;
            //cannot move off board
        }
        if (checker.isRed() && playerOneTurn && !checker.isKing()) {
            if (checker.getRow() >= row) {
                return false;
                //cannot move backward
            }
            if (row - checker.getRow() != 2 || Math.abs(col - checker.getCol()) != 2) {
                return false;
            }
            return board.getChecker(row, col) == null;
        }
        if (!checker.isRed() && !playerOneTurn && !checker.isKing()) {
            if (checker.getRow() <= row) {
                return false;
            }
            if (checker.getRow() - row != 2 || Math.abs(checker.getCol() - col) != 2) {
                return false;
            }
            return board.getChecker(row, col) == null;
        }
        if (checker.isKing() && (checker.isRed() == playerOneTurn)) {
            if (Math.abs(checker.getRow() - row) != 2 || Math.abs(checker.getCol() - col) != 2) {
                return false;
            }
            return board.getChecker(row, col) == null;
        }
        return false;
    }

    public boolean canCapture(int startRow, int startCol, int row, int col) {
        Checker checker = board.getChecker(startRow, startCol);
        if (isValidJump(checker, row, col)) {
            int midRow = (row + checker.getRow()) / 2;
            int midCol = (col + checker.getCol()) / 2;
            if (board.getChecker(midRow, midCol) == null) {
                return false;
            }
            return board.getChecker(midRow, midCol).isRed() != checker.isRed();
        }
        return false;
    }

    public void capture(int startRow, int startCol, int row, int col) {
        Checker checker = board.getChecker(startRow, startCol);
        if (checker != null) {
            if (canCapture(startRow, startCol, row, col)) {
                CheckerBoard old = new CheckerBoard(board);
                int midRow = (row + checker.getRow()) / 2;
                int midCol = (col + checker.getCol()) / 2;
                board.setChecker(row, col, checker);
                board.setChecker(midRow, midCol, null);
                board.setChecker(startRow, startCol, null);
                tryMakeKing(row, col);
                history.add(old);
                playerOneTurn = !playerOneTurn;
            }
        }
    }

    public void undoMove() {
        if (history.size() >= 1) {
            board = new CheckerBoard(history.undo());
            playerOneTurn = !playerOneTurn;
        }
    }

    private void tryMakeKing(int row, int col) {
        Checker checker = board.getChecker(row, col);
        if (checker != null) {
            if (!checker.isKing()) {
                if (checker.isRed() && row == 7) {
                    checker.makeKing();
                }
                if (!checker.isRed() && row == 0) {
                    checker.makeKing();
                }
            }
        }
    }

    public void newGame() {
        board.reset();
        playerOneTurn = true;
        history.reset();
    }

    public boolean checkWin() {
        ArrayList<Checker> red = new ArrayList<Checker>();
        ArrayList<Checker> black = new ArrayList<Checker>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Checker temp = board.getChecker(i, j);
                if (temp != null) {
                    if (temp.isRed()) {
                        red.add(temp);
                    }
                    if (!temp.isRed()) {
                        black.add(temp);
                    }
                }
            }
        }
        return red.isEmpty() || black.isEmpty();
    }

    public Checker getChecker(int row, int col) {
        return board.getChecker(row, col);
    }

    public boolean printBoardState(String message) {
        System.out.println(message);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Checker c = board.getChecker(i, j);
                if (c != null) {
                    if (c.isRed()) {
                        System.out.print("R");
                    }
                    if (!c.isRed()) {
                        System.out.print("B");
                    }
                } else {
                    System.out.print("- ");
                }
            }
            System.out.println();
        }
        System.out.println();
        return false;
    }

}
