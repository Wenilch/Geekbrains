package ru.gb.homework.homework6.animals;

public class Bird extends Animal {
    public Bird(String name, String color, int age) {
        super(name, color, age);

        runDistanceLimit = 5;
        swimDistanceLimit = 0;
        jumpHeightLimit = 0.1f;

        type = "Птица";

        count++;
    }

    @Override
    public void swim(float distance) {
        System.out.println(String.format("%s не смогла проплыть дистанцию. ", type));
    }

    public static int count;
}
