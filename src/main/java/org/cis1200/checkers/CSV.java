package org.cis1200.checkers;

import java.io.*;

public class CSV {
    private final static char COMMA = ',';

    public static void writeFile(String fileName, GameHistory history) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false))) {
            boolean isRed = true;
            int count = 0;
            StringBuilder stringBuilder = new StringBuilder();
            for (CheckerBoard state : history.getHistory()) {
                count++;
                stringBuilder.append("State\n");
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        Checker checker = state.getChecker(i, j);
                        if (checker == null) {
                            stringBuilder.append("-");
                        } else if (checker.isKing()) {
                            if (checker.isRed()) {
                                stringBuilder.append("D");
                            } else {
                                stringBuilder.append("K");
                            }
                        } else if (checker.isRed()) {
                            stringBuilder.append("R");
                        } else {
                            stringBuilder.append("B");
                        }
                        if (j < 7) {
                            stringBuilder.append(COMMA);
                        }
                    }
                    stringBuilder.append("\n");
                }
                if (isRed) {
                    stringBuilder.append("Red Turn\n");
                } else {
                    stringBuilder.append("Black Turn\n");
                }
                isRed = !isRed;
                stringBuilder.append("---\n");
            }
            stringBuilder.append("END");
            writer.write(stringBuilder.toString());
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File could not be found");
        } catch (IOException e) {
            throw new IllegalArgumentException("Error reading file");
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Null pointer exception");
        }
    }

    public static CheckersGame loadFile(String fileName) {
        GameHistory history = new GameHistory();
        boolean isRed = true;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("State")) {
                    char[][] chars = new char[8][8];
                    for (int i = 0; i < 8; i++) {
                        String row = reader.readLine();
                        if (row == null || row.split(",").length != 8) {
                            throw new RuntimeException("Malformed game state in file: " + fileName);
                        }
                        chars[i] = row.replace(",", "").toCharArray();
                    }
                    String turnLine = reader.readLine();
                    if (!turnLine.equals("Red Turn") && !turnLine.equals("Black Turn")) {
                        throw new RuntimeException("Invalid data in file: " + fileName);
                    }
                    isRed = turnLine.equals("Red Turn");
                    history.add(oneState(chars));
                }
            }
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File could not be found");
        } catch (IOException e) {
            throw new IllegalArgumentException("Error reading file");
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Null pointer exception");
        }
        return new CheckersGame(history.getLast(), history, !isRed);
    }

    private static CheckerBoard oneState(char[][] arr) {
        CheckerBoard toReturn = new CheckerBoard();
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if (arr[i][j] == 'R') {
                    Checker check = new Checker(true, i, j);
                    toReturn.setChecker(i, j, check);
                } else if (arr[i][j] == 'B') {
                    Checker check = new Checker(false, i, j);
                    toReturn.setChecker(i, j, check);
                } else if (arr[i][j] == 'D') {
                    Checker check = new Checker(true, i, j);
                    check.makeKing();
                    toReturn.setChecker(i, j, check);
                } else if (arr[i][j] == 'K') {
                    Checker check = new Checker(false, i, j);
                    check.makeKing();
                    toReturn.setChecker(i, j, check);
                } else {
                    toReturn.setChecker(i, j, null);
                }
            }
        }
        return toReturn;
    }
}
