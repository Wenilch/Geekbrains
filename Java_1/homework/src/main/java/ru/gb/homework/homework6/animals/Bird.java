package ru.gb.homework.homework6.animals;

public class Bird extends Animal {
    public Bird(String name, String color, int age) {
        super(name, color, age);

        runDistanceLimit = 5;
        jumpHeightLimit = 0.1f;

        type = "Птица";

        count++;
    }

    public static int count;
}
