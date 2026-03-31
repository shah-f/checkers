package org.cis1200.checkers;

import javax.swing.*;
import java.awt.*;

public class RunCheckers implements Runnable {

    @Override
    public void run() {
        final JFrame frame = new JFrame("Checkers");
        frame.setLocation(300, 300);

        final JPanel statusPanel = new JPanel();
        frame.add(statusPanel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        statusPanel.add(status);

        final CheckersGUI board = new CheckersGUI(status);
        frame.add(board, BorderLayout.CENTER);

        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        final JButton newGame = new JButton("New Game");
        newGame.addActionListener(e -> board.resetGame());
        control_panel.add(newGame);

        final JButton undo = new JButton("Undo Move");
        undo.addActionListener(e -> board.undoButton());
        control_panel.add(undo);

        final JButton save = new JButton("Save game");
        save.addActionListener(e -> board.saveGame());
        control_panel.add(save);

        final JButton load = new JButton("Load game");
        load.addActionListener(e -> board.loadGame());
        control_panel.add(load);

        final JButton instruct = new JButton("Instructions");
        instruct.addActionListener(e -> board.instructions());
        control_panel.add(instruct);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        board.resetGame();
    }
}
