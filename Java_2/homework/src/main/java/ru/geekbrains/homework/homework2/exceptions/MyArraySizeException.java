package ru.geekbrains.homework.homework2.exceptions;

public class MyArraySizeException extends Exception {
    public MyArraySizeException() {
        super("Incorrect array size");
    }
}