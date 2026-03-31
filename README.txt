=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 1200 Game Project README
PennKey: 45599176
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Arrays - I used this to implement the game board. A checkerboard is a fixed 8 by 8 grid, so 2D arrays
  were the perfect way to represent the game internally. Each spot of the array either held a checker or
  was null, which was updated as the game went on.

  2. JUnit Testable Component - This was necessary to validate the game logic. Between all the classes and rules of
  checkers, functions such as isValidMove and capture need to be tested thoroughly. I used most of the 10 tests to
  go through the CheckersGUI class, which contains most of the game logic. This component was necessary to debug all
  the little rules and edge cases that may be hard to debug through the playable game/user interface.

  3. File I/O - I used File I/O to save and load a game. When the save button is clicked, the current board and all
  past moves are saved into a file, which can then be reloaded at any point. I had to make sure all the past moves
  were contained in the file input so that the undo button could still be functional when a game was reloaded. File I/O
  is appropriate for this task as it can store a game for as long as the players wish. Even if the game tab is closed,
  the data within the file will still be maintained. If this functionality didn't use File I/O, the game logic wouldn't
  be able to fully reset every time the game was exited, which can lead to a whole host of problems.

  4. Collections - I used a LinkedList to hold all the states of the game. I needed a structure that was mutable
  in size and could hold duplicates, so a LinkedList was the right thing to use. Although unlikely, a checkers game
  could look the same at two different points in the game (for example, if one player moves their king forward and
  then the other player does the same, and they both move the kings back to the original position in the next two
  moves). As a result, I couldn't use a set or map. Also, I couldn't use an array as the size of an array is immutable.
  I only needed to access the last item in the list for the undo button, which was made quite simple with the built in
  functions of LinkedLists. When writing all the game states to a file, I only needed to parse the LinkedList in the
  order it was grown, so this choice of Collection worked well for my game.

===============================
=: File Structure Screenshot :=
===============================
- Include a screenshot of your project's file structure. This should include
  all of the files in your project, and the folders they are in. You can
  upload this screenshot in your homework submission to gradescope, named 
  "file_structure.png".

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
  The Checker class is the first, most basic class I have, which represents each individual checker. The Checker
  object has attributes such as isRed, isKing, row, and column. The CheckerBoard class is the next class up,
  which is a 2D array populated by Checker objects. It contains encapsulated methods to modify the array. Next,
  there is a GameHistory class which contains a LinkedList that stores all past game states. The undo functionality
  is implemented in GameHistory. CheckersGame is the class that unifies CheckerBoard and GameHistory to fully flesh
  out all the game logic. It has makeMove and capture methods, among other methods that make the game playable. This
  is the class that is majorly tested for my JUnit testing component. Then, I have a CSV class with BufferedReaders
  and BufferedWriters that can save the current game and load it at a later time. This is directly incorporated into
  CheckersGUI and kept separate from the game logic. RunCheckers contains the layout with all the buttons and board,
  and it connects to CheckersGUI.


- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
  I struggled with implementing the undo button. The undo button would repeatedly undo two moves,
  and I ended up having to save the board twice within one move to fix this issue. I also had trouble with
  undoing the first move, which was related to problems with empty linked lists. I'm not 100% clear on
  all the try/catch blocks and the exceptions I need to catch for File I/O, so that part was a problem.


- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
  I think the private state is mostly well encapsulated. There is a bit of repetitiveness
  between the CheckersGame class and the CheckerBoard class which I would rework if given the chance.
  The GUI class can't access/modify any of the game logic directly, so there is a clear separation.
  I think I could've reworked this project to have a few less classes, but otherwise, the logic is easy
  to follow throughout the project.


========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.
  I used Canva to create the checkerboard and the checkers.
