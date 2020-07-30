package ru.geekbrains.homework1;

import ru.geekbrains.homework1.boxs.Box;
import ru.geekbrains.homework1.fruits.Apple;
import ru.geekbrains.homework1.fruits.Orange;

import java.util.ArrayList;
import java.util.Arrays;

public class Application {
    public static void main(String[] args) {
        /**
         * 1. Написать метод, который меняет два элемента массива местами.(массив может быть любого ссылочного типа);
         */
        Integer[] firstArray = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        System.out.println(Arrays.toString(firstArray));
        swapElementsInArray(firstArray, 0, firstArray.length - 1);
        System.out.println(Arrays.toString(firstArray));
        swapElementsInArray(firstArray, 1, firstArray.length - 2);
        System.out.println(Arrays.toString(firstArray));

        /**
         * 2. Написать метод, который преобразует массив в ArrayList;
         */
        ArrayList<Integer> secondArrayList = arrayToArrayList(firstArray);
        System.out.println(secondArrayList);

        /**
         * 3. Большая задача
         */

        Box<Apple> apples = new Box<>();
        for (int i = 0; i < 5; i++) {
            apples.add(new Apple());
        }

        System.out.println("Вес коробки : " + apples.getWeight());

        Box<Orange> oranges = new Box<>();
        for (int i = 0; i < 6; i++) {
            oranges.add(new Orange());
        }

        System.out.println("Вес коробки : " + oranges.getWeight());


        if(!apples.compare(oranges)){
            System.out.println("Веса коробок не равны");
        }

        Box<Apple> apples2 = new Box<>();
        for (int i = 0; i < 100; i++) {
            apples2.add(new Apple());
        }
        System.out.println("Вес коробки из которой пересыпаем : " + apples2.getWeight());
        System.out.println("Вес коробки в которую пересыпаем: " + apples.getWeight());
        apples.pour(apples2);
        System.out.println("Вес коробки из которой пересыпали : " + apples2.getWeight());
        System.out.println("Вес коробки в которую пересыпали: " + apples.getWeight());

    }

    /**
     * 1. Написать метод, который меняет два элемента массива местами.(массив может быть любого ссылочного типа);
     */
    public static <T> void swapElementsInArray(T[] source, int firstElementIndex, int secondElementIndex) {
        if (source == null)
            throw new NullPointerException("Исходный массив не инициализирован");

        if (firstElementIndex < 0 || firstElementIndex > source.length - 1)
            throw new ArrayIndexOutOfBoundsException("Индекс первого элемента выходит за пределы массива");

        if (secondElementIndex < 0 || secondElementIndex > source.length - 1)
            throw new ArrayIndexOutOfBoundsException("Индекс второго элемента выходит за пределы массива");

        T temp = source[firstElementIndex];
        source[firstElementIndex] = source[secondElementIndex];
        source[secondElementIndex] = temp;
    }

    /**
     * 2. Написать метод, который преобразует массив в ArrayList;
     */

    public static <T> ArrayList<T> arrayToArrayList(T[] source) {
        if (source == null)
            throw new NullPointerException("Исходный массив не инициализирован");

        return new ArrayList<>(Arrays.asList(source));
    }
}
