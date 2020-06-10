package ru.gb.homework.homework3;

import java.util.Random;
import java.util.Scanner;

/**
 * 1. Полностью разобраться с кодом, попробовать переписать с нуля, стараясь не подглядывать в методичку.
 * 2. Переделать проверку победы, чтобы она не была реализована просто набором условий, например, с использованием циклов.
 * 3. * Попробовать переписать логику проверки победы, чтобы она работала для поля 5х5 и количества фишек 4. Очень желательно не делать это просто набором условий для каждой из возможных ситуаций;
 * 4. *** Доработать искусственный интеллект, чтобы он мог блокировать ходы игрока.
 */
public class TicTaeToe {

    // Заведение констант на допустимый ввод
    // Какими символами (фигками) играет игрок
    private static final char DOT_HUMAN = 'X';
    // Что вводит компьютер
    private static final char DOT_AI = 'O';
    // Символ пустой клетка
    private static final char DOT_EMPTY = ' ';

    // Упрощения вместо использования field.length
    private static int fieldSizeX;
    private static int fieldSizeY;
    // Игровое поля
    private static char[][] field;

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        int fieldSize = 5;
        while (true) {
            init(fieldSize);
            printField();
            playOnce();
            System.out.println("Играть сначала?");
            if (SCANNER.next().equals("no")) {
                break;
            }
        }
    }

    private static void playOnce() {
        while (true) {
            humanTurn();
            printField();
            if (isWinnerExists(DOT_HUMAN)) {
                System.out.println("Победил Игрок!");
                break;
            }
            if (isDraw()) {
                System.out.println("Ничья!");
                break;
            }

            aiTurn();
            printField();
            if (isWinnerExists(DOT_AI)) {
                System.out.println("Победил Компьютер!");
                break;
            }
            if (isDraw()) {
                System.out.println("Ничья!");
                break;
            }

        }
    }

    private static void init(int fieldSize) {
        fieldSizeX = fieldSize;
        fieldSizeY = fieldSize;

        field = new char[fieldSizeY][fieldSizeX];

        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                field[y][x] = DOT_EMPTY;
            }
        }
    }

    private static void printField() {
        System.out.println("=============");

        for (int y = 0; y < fieldSizeY; y++) {
            System.out.print("| ");
            for (int x = 0; x < fieldSizeX; x++) {
                System.out.print(field[y][x] + " | ");
            }
            System.out.println();
        }
    }

    // Проврека, что координаты находятся в допустимом диапазоне
    private static boolean isValidField(int x, int y) {
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    // Проврека, что поле занято (знак в поле не соответствует DOT_EMPTY);
    private static boolean isNotEmptyField(int x, int y) {
        return field[y][x] != DOT_EMPTY;
    }

    private static void humanTurn() {
        // 3 1
        int x;
        int y;

        do {
            System.out.print("Введите координаты хода X и Y (от 1 до " + fieldSizeY + ") через пробел >>> ");
            x = SCANNER.nextInt() - 1;
            y = SCANNER.nextInt() - 1;
        } while (!isValidField(x, y) || isNotEmptyField(x, y));
        field[y][x] = DOT_HUMAN;
    }

    private static void aiTurn() {
        int x;
        int y;
        if (!tryBlockUserWin()) {
            do {
                x = RANDOM.nextInt(fieldSizeX);
                y = RANDOM.nextInt(fieldSizeY);
            } while (isNotEmptyField(x, y));
            field[y][x] = DOT_AI;
        }
    }

    private static boolean isDraw() {
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (field[y][x] == DOT_EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    private static final int WINNER_COMBINATION = 4;

    private static boolean isWinnerExists(char symbol) {
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                boolean checkWinner = checkWinnerByHorizontal(symbol, WINNER_COMBINATION, y, x) || checkWinnerByVertical(symbol, WINNER_COMBINATION, y, x) ||
                        checkWinnerByDiagonalDown(symbol, WINNER_COMBINATION, y, x) || checkWinnerByDiagonalUp(symbol, WINNER_COMBINATION, y, x);

                if (checkWinner) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean checkWinnerByHorizontal(char symbol, int symbolCount, int startY, int startX) {
        while (symbolCount > 0) {
            if (startX > fieldSizeY - 1 || field[startY][startX] != symbol) {
                return false;
            }

            startX++;
            symbolCount--;
        }

        return true;
    }

    private static boolean checkWinnerByVertical(char symbol, int symbolCount, int startY, int startX) {
        while (symbolCount > 0) {
            if (startY > fieldSizeX - 1 || field[startY][startX] != symbol) {
                return false;
            }

            startY++;
            symbolCount--;
        }

        return true;
    }

    private static boolean checkWinnerByDiagonalDown(char symbol, int symbolCount, int startY, int startX) {
        while (symbolCount > 0) {
            if ((startX > fieldSizeX - 1 || startY > fieldSizeY - 1) || field[startX][startY] != symbol) {
                return false;
            }

            startY++;
            startX++;
            symbolCount--;
        }

        return true;
    }

    private static boolean checkWinnerByDiagonalUp(char symbol, int symbolCount, int startY, int startX) {
        while (symbolCount > 0) {
            if ((startX < 0 || startX > fieldSizeX - 1 || startY < 0 || startY > fieldSizeY - 1) || field[startX][startY] != symbol) {
                return false;
            }

            startY++;
            startX--;
            symbolCount--;
        }

        return true;
    }

    private static boolean tryBlockUserWin() {
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                boolean isBlockUserWin = tryBlockUserWinByHorizontal(DOT_HUMAN, WINNER_COMBINATION - 1, y, x) || tryBlockUserWinByVertical(DOT_HUMAN, WINNER_COMBINATION - 1, y, x)
                        || tryBlockUserWinByDiagonalDown(DOT_HUMAN, WINNER_COMBINATION - 1, y, x) || tryBlockUserWinByDiagonalUp(DOT_HUMAN, WINNER_COMBINATION - 1, y, x);

                if (isBlockUserWin)
                    return true;
            }
        }

        return false;
    }

    private static boolean tryBlockUserWinByHorizontal(char symbol, int symbolCount, int startY, int startX) {
        if (checkWinnerByHorizontal(symbol, symbolCount, startY, startX)) {
            if (startX - 1 >= 0 && field[startY][startX - 1] == DOT_EMPTY) {
                field[startY][startX - 1] = DOT_AI;
            } else if (startX + 1 < fieldSizeX && field[startY][startX + 1] == DOT_EMPTY) {
                field[startY][startX + 1] = DOT_AI;
            } else if (startX + symbolCount < fieldSizeX && field[startY][startX + symbolCount] == DOT_EMPTY) {
                field[startY][startX + symbolCount] = DOT_AI;
            } else if (startX - symbolCount > 0 && field[startY][startX - symbolCount] == DOT_EMPTY) {
                field[startY][startX - symbolCount] = DOT_AI;
            }

            return true;
        }

        return false;
    }

    private static boolean tryBlockUserWinByVertical(char symbol, int symbolCount, int startY, int startX) {
        if (checkWinnerByVertical(symbol, symbolCount, startY, startX)) {
            if (startY - 1 >= 0 && field[startY - 1][startX] == DOT_EMPTY) {
                field[startY - 1][startX] = DOT_AI;
            } else if (startY + 1 < fieldSizeY && field[startY + 1][startX] == DOT_EMPTY) {
                field[startY + 1][startX] = DOT_AI;
            } else if (startY + symbolCount < fieldSizeY && field[startY + symbolCount][startX] == DOT_EMPTY) {
                field[startY + symbolCount][startX] = DOT_AI;
            } else if (startY - symbolCount > 0 && field[startY - symbolCount][startX] == DOT_EMPTY) {
                field[startY - symbolCount][startX] = DOT_AI;
            }

            return true;
        }

        return false;
    }

    private static boolean tryBlockUserWinByDiagonalDown(char symbol, int symbolCount, int startY, int startX) {
        if (checkWinnerByDiagonalDown(symbol, symbolCount, startY, startX)) {
            if (startY - 1 >= 0 && startX - 1 >= 0 && field[startY - 1][startX - 1] == DOT_EMPTY) {
                field[startY - 1][startX - 1] = DOT_AI;
            } else if (startY + 1 < fieldSizeY && startX + 1 < fieldSizeX && field[startY + 1][startX + 1] == DOT_EMPTY) {
                field[startY + 1][startX + 1] = DOT_AI;
            } else if (startY + symbolCount < fieldSizeY && startX + symbolCount < fieldSizeX && field[startY + symbolCount][startX + symbolCount] == DOT_EMPTY) {
                field[startY + symbolCount][startX + symbolCount] = DOT_AI;
            } else if (startY - symbolCount > 0 && startX - symbolCount > 0 && field[startY - symbolCount][startX - symbolCount] == DOT_EMPTY) {
                field[startY - symbolCount][startX - symbolCount] = DOT_AI;
            }

            return true;
        }

        return false;
    }

    private static boolean tryBlockUserWinByDiagonalUp(char symbol, int symbolCount, int startY, int startX) {
        if (checkWinnerByDiagonalUp(symbol, symbolCount, startY, startX)) {
            if (startY - 1 >= 0 && startX + 1 >= 0 && field[startY - 1][startX + 1] == DOT_EMPTY) {
                field[startY - 1][startX + 1] = DOT_AI;
            } else if (startY + 1 < fieldSizeY && startX - 1 < fieldSizeX && field[startY + 1][startX - 1] == DOT_EMPTY) {
                field[startY + 1][startX - 1] = DOT_AI;
            } else if (startY - symbolCount < fieldSizeY && startX + symbolCount < fieldSizeX && field[startY - symbolCount][startX + symbolCount] == DOT_EMPTY) {
                field[startY - symbolCount][startX + symbolCount] = DOT_AI;
            } else if (startY - symbolCount > 0 && startX + symbolCount > 0 && field[startY - symbolCount][startX + symbolCount] == DOT_EMPTY) {
                field[startY - symbolCount][startX + symbolCount] = DOT_AI;
            }

            return true;
        }

        return false;
    }

}
