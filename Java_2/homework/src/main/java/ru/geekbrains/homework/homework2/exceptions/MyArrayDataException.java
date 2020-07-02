package ru.geekbrains.homework.homework2.exceptions;

public class MyArrayDataException extends Exception {
    public MyArrayDataException(int i, int j) {
        super(String.format("Failed to get data from cell [%d , %d]", i, j));
    }
}
