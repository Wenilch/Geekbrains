package ru.geekbrains.homework7.testing;

import ru.geekbrains.homework7.testing.annotations.AfterSuite;
import ru.geekbrains.homework7.testing.annotations.BeforeSuite;
import ru.geekbrains.homework7.testing.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Tester {

    public static void test(Class clazz) {
        Method initMethod = null;
        ArrayList<Method> testMethods = new ArrayList<>();
        Method closeMethod = null;

        Object someClass;
        try {
            someClass = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return;
        }

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                if (initMethod != null) {
                    throw new RuntimeException("Превышение количества инициализирующих методов.");
                }

                initMethod = method;
            }

            if (method.isAnnotationPresent(AfterSuite.class)) {
                if (closeMethod != null) {
                    throw new RuntimeException("Превышение количества завершающих методов методов.");
                }

                closeMethod = method;
            }

            if (method.isAnnotationPresent(Test.class) && method.getAnnotation(Test.class).priority() < 11) {
                testMethods.add(method);
            }
        }

        if (testMethods.isEmpty()) {
            return;
        }

        testMethods.sort((method1, method2) -> {
            int priority1 = method1.getAnnotation(Test.class).priority();
            int priority2 = method2.getAnnotation(Test.class).priority();

            return priority2 - priority1;
        });

        for (Method method : testMethods) {
            try {
                if(initMethod != null) {
                    initMethod.invoke(someClass);
                }
                method.invoke(someClass);
                if(closeMethod != null) {
                    closeMethod.invoke(someClass);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

    }

    public static void test(String className) {
        try {
            test(Class.forName(className));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
