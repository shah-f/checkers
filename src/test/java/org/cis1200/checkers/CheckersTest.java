package org.cis1200.checkers;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class CheckersTest {
    @Test
    public void testCreateChecker() {
        Checker checker = new Checker(true, 1, 1);
        assertTrue(checker.isRed());
        assertFalse(checker.isKing());

        //check if this constructor makes a deep copy
        Checker duplicate = new Checker(checker);
        assertNotEquals(checker, duplicate);

        //check makeKing method
        checker.makeKing();
        assertTrue(checker.isKing());
    }

    @Test
    public void testCreateCheckerBoard() {
        CheckerBoard board = new CheckerBoard();
        Checker checker = new Checker(true, 3, 5);
        board.setChecker(4, 4, checker);
        assertEquals(checker.getRow(), 4);
        assertEquals(checker.getCol(), 4);
        assertEquals(checker, board.getChecker(4, 4));

        board.reset();
        assertNull(board.getChecker(4, 4));
    }

    @Test
    public void testGameHistory() {
        GameHistory history = new GameHistory();
        CheckerBoard board = new CheckerBoard();
        Checker checker = new Checker(true, 3, 5);
        board.setChecker(4, 4, checker);
        history.add(board);
        assertEquals(history.size(), 1);

        //should create a deep copy
        assertNotEquals(history.getLast(), board);

        CheckerBoard board2 = new CheckerBoard(board);
        assertNotEquals(board, board2);
        history.add(board2);
        assertEquals(history.size(), 2);
        history.reset();
        assertEquals(history.size(), 0);
    }

    @Test
    public void testIsValidMove() {
        CheckersGame game = new CheckersGame();
        //test redChecker moves
        Checker checker = new Checker(true, 2, 4);
        assertTrue(game.isValidMove(checker, 3, 5));
        assertFalse(game.isValidMove(checker, 4, 5));
        //is valid move for black checker when not black turn
        Checker checker2 = new Checker(false, 5, 7);
        assertFalse(game.isValidMove(checker2, 4, 6));
    }

    @Test
    public void testMakeMove() {
        CheckersGame game = new CheckersGame();
        //test redChecker moves
        Checker checker = game.getChecker(2,4);
        game.moveChecker(2, 4, 3,5);
        assertNull(game.getChecker(2, 4));
        assertEquals(checker, game.getChecker(3, 5));
        assertEquals(checker.getRow(), 3);
        assertEquals(checker.getCol(), 5);
        assertFalse(game.isPlayerOne());
        //test blackChecker moves
        Checker checker2 = new Checker(false, 5, 7);
        assertTrue(game.isValidMove(checker2, 4, 6));
        //out of bounds test
        assertFalse(game.isValidMove(checker2, 4, 8));
    }

    @Test
    public void testCanCapture() {
        CheckersGame game = new CheckersGame();
        game.moveChecker(2, 4, 3,5);
        game.moveChecker(5, 7, 4,6);
        assertTrue(game.canCapture(3,5, 5, 7));
        assertFalse(game.canCapture(3,5, 5, 8));
        assertFalse(game.canCapture(3, 5, 6, 7));
    }

    @Test
    public void testBlackCapture() {
        CheckersGame game = new CheckersGame();
        game.moveChecker(2, 4, 3,5);
        game.moveChecker(5, 7, 4,6);
        game.capture(3,5, 5, 7);
        //black checker deleted
        assertNull(game.getChecker(4, 6));
        //red checker moved
        assertNull(game.getChecker(3, 5));

    }

    @Test
    public void testRedCapture() {
        CheckersGame game = new CheckersGame();
        game.moveChecker(2, 4, 3,5);
        game.moveChecker(5, 7, 4,6);
        game.moveChecker(2,2, 3,1);
        game.capture(5,7, 3, 5);
    }

    @Test
    public void testKing() {
        CheckersGame game = new CheckersGame();
        game.moveChecker(2, 2, 3, 3);
        game.moveChecker(5, 5, 4, 4);
        game.capture(3, 3, 5, 5);
        game.moveChecker(5, 3, 4, 2);
        game.moveChecker(2, 0, 3, 1);
        game.moveChecker(6, 2, 5, 3);
        game.moveChecker(3,1,4,0);
        game.moveChecker(7, 3, 6, 2);
        game.capture(5,5,7,3); // Should become a king
        assertTrue(game.getChecker(7,3).isKing());
        //can king move backward
        game.moveChecker(5, 7, 4,6);
        Checker king = game.getChecker(7,3);
        assertTrue(game.isValidMove(king, 6,4));
    }

    @Test
    public void testUndo() {
        CheckersGame game = new CheckersGame();
        game.moveChecker(2, 2, 3, 3);
        game.moveChecker(5, 5, 4, 4);
        assertEquals(5, game.getHistory().size());
        assertTrue(game.isPlayerOne());
        game.undoMove();
        assertFalse(game.isPlayerOne());
        assertEquals(3, game.getHistory().size());
        assertNull(game.getChecker(4,4));
    }
}
