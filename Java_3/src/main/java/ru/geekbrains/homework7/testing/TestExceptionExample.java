package ru.geekbrains.homework7.testing;

import ru.geekbrains.homework7.testing.annotations.BeforeSuite;

public class TestExceptionExample {
    @BeforeSuite
    public void init() {
        System.out.println("Инициализация объектов тестирования");
    }

    @BeforeSuite
    public void init1() {
        System.out.println("Инициализация объектов тестирования");
    }
}
