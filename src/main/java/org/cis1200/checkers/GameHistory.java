package org.cis1200.checkers;

import java.util.LinkedList;

public class GameHistory {

    private LinkedList<CheckerBoard> history;

    public GameHistory() {
        history = new LinkedList<>();
    }

    public LinkedList<CheckerBoard> getHistory() {
        return new LinkedList<>(history);
    }

    public void add(CheckerBoard board) {
        history.add(new CheckerBoard(board));
    }

    public CheckerBoard undo() {
        if (history.isEmpty()) {
            throw new IllegalStateException("No game states to undo");
        }
        if (history.size() == 1) {
            return history.getFirst();
        }
        history.removeLast();
        CheckerBoard toReturn = history.getLast();
        history.removeLast();
        return toReturn;
    }

    public void reset() {
        history.clear();
    }

    public int size() {
        return history.size();
    }

    public CheckerBoard getLast() {
        return history.getLast();
    }
}
