package ru.gb.homework.homework6.animals;

public class Horse extends SwimmingAnimal {
    public Horse(String name, String color, int age) {
        super(name, color, age);

        runDistanceLimit = 1500;
        swimDistanceLimit = 100;
        jumpHeightLimit = 3;

        type = "Лошадь";

        count++;
    }

    public static int count;
}
