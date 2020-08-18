package ru.geekbrains.homework7;

import ru.geekbrains.homework7.testing.TestExample;
import ru.geekbrains.homework7.testing.TestExample2;
import ru.geekbrains.homework7.testing.TestExceptionExample;
import ru.geekbrains.homework7.testing.Tester;

public class Application {
    public static void main(String[] args) {
        Tester.test(TestExample.class);
        Tester.test(TestExample2.class);

        try {
            Tester.test(TestExceptionExample.class.getName());
        } catch (Exception exception){
            exception.printStackTrace();
        }
    }
}
