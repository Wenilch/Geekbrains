package ru.geekbrains.homework7.testing;

import ru.geekbrains.homework7.testing.annotations.AfterSuite;
import ru.geekbrains.homework7.testing.annotations.BeforeSuite;
import ru.geekbrains.homework7.testing.annotations.Test;

public class TestExample {
    @BeforeSuite
    public void init() {
        System.out.println("Инициализация объектов тестирования");
    }

    @Test
    public void test1() {
        System.out.println("Запуск теста с приоритетом 1");
    }

    @Test(priority = 10)
    public void test10() {
        System.out.println("Запуск теста с приоритетом 10");
    }

    @Test(priority = 5)
    public void test5() {
        System.out.println("Запуск теста с приоритетом 5");
    }

    @AfterSuite
    public void close() {
        System.out.println("Закрытие объектов тестирования");
    }
}