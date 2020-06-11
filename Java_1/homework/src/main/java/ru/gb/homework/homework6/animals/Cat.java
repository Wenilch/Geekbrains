package ru.gb.homework.homework6.animals;

public class Cat extends Animal {
    public Cat(String name, String color, int age) {
        super(name, color, age);

        runDistanceLimit = 200;
        jumpHeightLimit = 2;

        type = "Кошка";

        count++;
    }

    public static int count;
}
