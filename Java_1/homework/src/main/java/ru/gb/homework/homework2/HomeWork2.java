package ru.gb.homework.homework2;

import java.util.Arrays;

public class HomeWork2 {
    public static void main(String[] args) {

        int[] arrayForReverseArrayValue = {1, 1, 0, 0, 1, 0, 1, 1, 0, 0};
        reverseArrayValue(arrayForReverseArrayValue);

        fillEmptyArray();

        int[] arrayForMultiplySomeArrayValuesByTwo = {1, 5, 3, 2, 11, 4, 5, 2, 4, 8, 9, 1};
        multiplySomeArrayValuesByTwo(arrayForMultiplySomeArrayValuesByTwo);

        fillDiagonalsMatrixValues(5);

        findMaxAndMinValue(arrayForMultiplySomeArrayValuesByTwo);

        System.out.println("checkBalance([2, 2, 2, 1, 2, 2, || 10, 1]) → true. Метод вернул : " + checkBalance(new int[]{2, 2, 2, 1, 2, 2, 10, 1}));
        System.out.println("checkBalance([1, 1, 1, || 2, 1]) → true . Метод вернул : " + checkBalance(new int[]{1, 1, 1, 2, 1}));
        System.out.println("checkBalance([1, 2 ,3 ,4]→ false. Метод вернул : " + checkBalance(new int[]{1, 2, 3, 4}));

        offsetArrayElements(new int[]{0, 1, 2, 3, 4}, 2);
        offsetArrayElements(new int[]{0, 1, 2, 3, 4}, -2);
    }

    /**
     * 1. Задать целочисленный массив, состоящий из элементов 0 и 1.
     */
    private static void reverseArrayValue(int[] array) {
        System.out.print("Начальное состояние массива " + Arrays.toString(array));
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i] == 0 ? 1 : 0;
        }
        System.out.println("Состояние массива после выполнения метода " + Arrays.toString(array));
    }

    /**
     * 2. Задать пустой целочисленный массив размером 8. С помощью цикла заполнить его значениями 0 3 6 9 12 15 18 21;
     */
    private static void fillEmptyArray() {
        int[] emptyArray = new int[8];
        int value = 0;

        for (int i = 0; i < emptyArray.length; i++) {
            emptyArray[i] = value;
            value += 3;
        }

        System.out.println("Задать пустой целочисленный массив размером 8 " + Arrays.toString(emptyArray));
    }

    /**
     * 3. Задать массив [ 1, 5, 3, 2, 11, 4, 5, 2, 4, 8, 9, 1 ] пройти по нему циклом, и числа меньшие 6 умножить на 2
     */
    private static void multiplySomeArrayValuesByTwo(int[] array) {
        System.out.print("Начальное состояние массива " + Arrays.toString(array));
        for (int i = 0; i < array.length; i++) {
            if (array[i] < 6) {
                array[i] *= 2;
            }
        }
        System.out.println("Состояние массива после выполнения метода " + Arrays.toString(array));
    }

    /**
     * 4. Создать квадратный двумерный целочисленный массив (количество строк и столбцов одинаковое),
     * и с помощью цикла(-ов) заполнить его диагональные элементы единицами;
     */
    private static void fillDiagonalsMatrixValues(int lenght) {
        int[][] array = new int[lenght][lenght];

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length; j++) {
                if (i == j || i + j == lenght - 1) {
                    array[i][j] = 1;
                }
            }
        }

        System.out.println("Состояние массива после выполнения метода ");
        for (int[] values : array) {
            System.out.println(Arrays.toString(values));
        }
    }

    /**
     * 5. ** Задать одномерный массив и найти в нем минимальный и максимальный элементы (без помощи интернета);
     */
    private static void findMaxAndMinValue(int[] array) {
        int max = array[0];
        int min = array[0];

        for (int value : array) {
            if (value > max) {
                max = value;
            }

            if (value < min) {
                min = value;
            }
        }

        System.out.println("Максимальный элемент " + max);
        System.out.println("Минимальный элемент " + min);
    }

    /**
     * 6. ** Написать метод, в который передается не пустой одномерный целочисленный массив, метод должен вернуть true,
     * если в массиве есть место, в котором сумма левой и правой части массива равны.
     * Примеры: checkBalance([2, 2, 2, 1, 2, 2, || 10, 1]) → true, checkBalance([1, 1, 1, || 2, 1]) → true, граница показана символами ||, эти символы в массив не входят.
     */
    private static boolean checkBalance(int[] array) {
        int mainIndex = 0;
        int sumLeftPart = 0;
        int sumRightPart = 0;

        while (mainIndex < array.length) {

            for (int i = 0; i < mainIndex; i++) {
                sumLeftPart += array[i];
            }

            for (int j = mainIndex; j < array.length; j++) {
                sumRightPart += array[j];
            }

            if (sumLeftPart == sumRightPart) {
                return true;
            }

            sumLeftPart = 0;
            sumRightPart = 0;
            mainIndex++;
        }

        return false;
    }

    /**
     * **** Написать метод, которому на вход подается одномерный массив и число n (может быть положительным, или отрицательным),
     * при этом метод должен сместить все элементы массива на n позиций. Для усложнения задачи нельзя пользоваться вспомогательными массивами.
     */
    private static void offsetArrayElements(int[] array, int offset) {
        System.out.println("Начальное состояние массива " + Arrays.toString(array));

        int temp, buffer;

        if (offset >= 0) {
            temp = array[0];

            while (offset > 0) {
                for (int i = 0; i < array.length; i++) {
                    if (i == 0) {
                        temp = array[i + 1];
                        array[i + 1] = array[i];
                    } else if (i == array.length - 1) {
                        array[0] = temp;
                    } else {
                        buffer = array[i + 1];
                        array[i + 1] = temp;
                        temp = buffer;
                    }
                }

                offset--;
            }
        } else {
            temp = array[array.length - 1];

            while (offset < 0) {

                for (int i = array.length - 1; i >= 0; i--) {
                    if (i == array.length - 1) {
                        temp = array[i - 1];
                        array[i - 1] = array[i];
                    } else if (i == 0) {
                        array[array.length - 1] = temp;
                    } else {
                        buffer = array[i - 1];
                        array[i - 1] = temp;
                        temp = buffer;
                    }

                }

                offset++;
            }
        }

        System.out.println("Состояние массива после выполнения метода " + Arrays.toString(array));
    }
}
