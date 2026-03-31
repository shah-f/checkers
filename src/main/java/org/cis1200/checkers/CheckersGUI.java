package org.cis1200.checkers;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class CheckersGUI extends JPanel {

    private static final int BOARD_SIZE = 600;
    private static final int SQUARE_SIZE = BOARD_SIZE / 8;

    private final Image boardImage;
    private final Image redCheckerImage;
    private final Image blackCheckerImage;
    private final Image redKingImage;
    private final Image blackKingImage;

    private CheckersGame game;
    private JLabel status;

    public CheckersGUI(JLabel status) {
        this.game = new CheckersGame();
        this.status = status;
        boardImage = new ImageIcon(getClass().getResource("/checkerPattern.png")).getImage();
        redCheckerImage = new ImageIcon(getClass().getResource("/redChecker.png")).getImage();
        blackCheckerImage = new ImageIcon(getClass().getResource("/blackChecker.png")).getImage();
        redKingImage = new ImageIcon(getClass().getResource("/redKing.png")).getImage();
        blackKingImage = new ImageIcon(getClass().getResource("/blackKing.png")).getImage();
        setFocusable(true);

        setPreferredSize(new Dimension(BOARD_SIZE, BOARD_SIZE));

        addMouseListener(new MouseAdapter() {
            private int startRow = -1;
            private int startCol = -1;

            @Override
            public void mousePressed(MouseEvent e) {
                startCol = e.getX() / SQUARE_SIZE;
                startRow = e.getY() / SQUARE_SIZE;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int endCol = e.getX() / SQUARE_SIZE;
                int endRow = e.getY() / SQUARE_SIZE;
                if (startRow != -1 && startCol != -1) {
                    Checker selectedChecker = game.getChecker(startRow, startCol);
                    if (selectedChecker != null) {
                        // try capturing
                        if (game.canCapture(startRow, startCol, endRow, endCol)) {
                            game.capture(startRow, startCol, endRow, endCol);
                        } else if (game.isValidMove(selectedChecker, endRow, endCol)) {
                            //otherwise, regular move
                            game.moveChecker(startRow, startCol, endRow, endCol);
                        } else {
                            //invalid move
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Invalid move! Try again.",
                                    "Invalid Move",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                    }
                    startRow = -1;
                    startCol = -1;
                    repaint();
                    updateStatus();
                }
            }
        });
    }

    public void resetGame() {
        game.newGame();
        status.setText("Player one's turn (red)");
        repaint();
    }

    public void undoButton() {
        System.out.println("Undo button clicked");
        game.undoMove();
        if (game.isPlayerOne()) {
            status.setText("Player one's turn (red)");
        } else {
            status.setText("Player Two's turn (black)");
        }
        repaint();
    }

    private void updateStatus() {
        if (game.checkWin()) {
            int winner;
            if (game.isPlayerOne()) {
                status.setText("Player Two wins!");
                winner = 2;
            } else {
                status.setText("Player One wins!");
                winner = 1;
            }
            JOptionPane.showMessageDialog(null,
                    "Player " + winner + " wins!",
                    "Game Over",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            if (game.isPlayerOne()) {
                status.setText("Player One's turn (red)");
            } else {
                status.setText("Player Two's turn (black)");
            }
            repaint();
        }
    }

    public void saveGame() {
        String fileName = JOptionPane.showInputDialog(null,
                "Enter game1, game2, or game3 to store the game",
                "Save Game", JOptionPane.PLAIN_MESSAGE);

        if (fileName.equals("game1") || fileName.equals("game2") || fileName.equals("game3")) {
            try {
                CSV.writeFile("files/" + fileName, game.getHistory());
                JOptionPane.showMessageDialog(null, "Game saved successfully!");
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(
                        null, "Error saving the game: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Error. Please try again and only " +
                    "enter game1, game2, or game3.");
        }
    }

    public void loadGame() {
        String fileName = JOptionPane.showInputDialog(
                null, "Enter game1, game2, or game3 to load that game",
                "Load Game", JOptionPane.PLAIN_MESSAGE);
        if (fileName.equals("game1") || fileName.equals("game2") || fileName.equals("game3")) {
            try {
                CheckersGame loadedGame = CSV.loadFile("files/" + fileName);
                this.game = loadedGame;
                repaint();
                updateStatus();
                JOptionPane.showMessageDialog(null, "Game loaded successfully!");
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(
                        null, "Error loading the game: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Error. Please try again with a " +
                    "valid file name.");
        }
    }

    public void instructions() {
        JOptionPane.showMessageDialog(null, "This is a two player game. Assign one" +
                        " person to play red and another to play black.\n" +
                        "The player whose turn it is will be " +
                        "indicated below the game board.\n" +
                        "You may only move diagonally forward relative to your " +
                        "home side.\n" +
                        "If a checker reaches the last row of the opposite side, " +
                        "it becomes a king, in which case " +
                        "it can move backward or forward.\n" +
                        "To capture an opposing checker, you must jump over it" +
                        " diagonally onto an empty space on the " +
                        "board.\nTo win the game, you must capture " +
                        "all your opponent's checkers.\nYou can save up to " +
                        "three games at a time and reload " +
                        "any one of them to return to that game.\n" +
                        "Press new game to restart. Good luck!",
                "Instructions", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(boardImage, 0, 0, BOARD_SIZE, BOARD_SIZE, null);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Checker checker = game.getChecker(row, col);
                if (checker != null) {
                    if (checker.isKing() && checker.isRed()) {
                        g.drawImage(redKingImage, col * SQUARE_SIZE, row * SQUARE_SIZE, null);
                    } else if (checker.isKing() && !checker.isRed()) {
                        g.drawImage(blackKingImage, col * SQUARE_SIZE, row * SQUARE_SIZE, null);
                    } else if (checker.isRed()) {
                        g.drawImage(redCheckerImage, col * SQUARE_SIZE, row * SQUARE_SIZE, null);
                    } else {
                        g.drawImage(blackCheckerImage, col * SQUARE_SIZE, row * SQUARE_SIZE, null);
                    }
                }
            }
        }
    }
}
