package ru.gb.homework.homework6.animals;

public class Dog extends Animal {
    public Dog(String name, String color, int age) {
        super(name, color, age);

        runDistanceLimit = 500;
        swimDistanceLimit = 10;
        jumpHeightLimit = 0.4f;

        type = "Собака";

        count++;
    }

    public static int count;
}
