package ru.geekbrains.homework7.testing;

import ru.geekbrains.homework7.testing.annotations.AfterSuite;
import ru.geekbrains.homework7.testing.annotations.BeforeSuite;
import ru.geekbrains.homework7.testing.annotations.Test;

public class TestExample2 {
    @BeforeSuite
    public void init() {
        System.out.println("Инициализация объектов тестирования");
    }

    @Test
    public void test1() {
        System.out.println("Запуск первого теста");
    }

    @Test
    public void test2() {
        System.out.println("Запуск второго теста");
    }

    @Test
    public void test3() {
        System.out.println("Запуск третьего теста");
    }

    @AfterSuite
    public void close() {
        System.out.println("Закрытие объектов тестирования");
    }
}
