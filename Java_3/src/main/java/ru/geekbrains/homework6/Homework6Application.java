package ru.geekbrains.homework6;

public class Homework6Application {
    /**
     * 2. Написать метод, которому в качестве аргумента передается не пустой одномерный целочисленный массив.
     * Метод должен вернуть новый массив, который получен путем вытаскивания из исходного массива элементов,
     * идущих после последней четверки.
     * Входной массив должен содержать хотя бы одну четверку, иначе в методе необходимо выбросить RuntimeException.
     * Написать набор тестов для этого метода (по 3-4 варианта входных данных).
     * Вх: [ 1 2 4 4 2 3 4 1 7 ] -> вых: [ 1 7 ].
     */
    private final int MIN_ARRAY_SIZE = 6;

    public int[] takeTwoLastElements(int[] array) {
        if (array == null) {
            throw new NullPointerException("Массив не инициализирован.");
        }

        if (array.length < MIN_ARRAY_SIZE) {
            throw new RuntimeException("Массив не удовлетворяет условиям.");
        }

        return new int[]{array[array.length - 2], array[array.length - 1]};
    }

    /**
     * 3. Написать метод, который проверяет состав массива из чисел 1 и 4.
     * Если в нем нет хоть одной четверки или единицы, то метод вернет false;
     * Написать набор тестов для этого метода (по 3-4 варианта входных данных).
     */

    private final int ONE = 1;
    private final int FOUR = 4;

    public boolean isArrayContainJustOneAndFour(int[] array) {
        if (array == null) {
            throw new NullPointerException("Массив не инициализирован.");
        }

        boolean isContainOne = false;
        boolean isContainFour = false;

        for (int value : array) {
            if (value != ONE && value != FOUR) {
                return false;
            }

            if (value == ONE && !isContainOne) {
                isContainOne = true;
            }

            if (value == FOUR && !isContainFour) {
                isContainFour = true;
            }
        }

        return isContainOne && isContainFour;
    }

}
