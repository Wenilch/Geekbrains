package ru.gb.homework.homework6;

import ru.gb.homework.homework6.animals.*;

public class Application {

    public static void main(String[] args) {
        Cat cat = new Cat("Boris", "Black", 10);

        cat.run(1000f);
        cat.jump(1000f);

        cat.setRunDistanceLimit(10000);
        cat.run(1000f);

        System.out.println("========================================");
        System.out.println("========================================");

        Dog dog = new Dog("Chak", "White", 5);
        Horse horse = new Horse("Jack", "Green", 1);
        Bird bird = new Bird("Samy", "Yellow", 6);

        dog.run(1000f);
        dog.swim(1000f);
        dog.jump(1000f);

        System.out.println("========================================");
        System.out.println("========================================");

        horse.run(1000f);
        horse.swim(1000f);
        horse.jump(1000f);

        horse.setRunDistanceLimit(0);
        horse.run(1000f);

        System.out.println("========================================");
        System.out.println("========================================");

        bird.run(1000f);
        bird.jump(1000f);

        bird.setJumpHeightLimit(10000);
        bird.jump(1000f);

        System.out.println("========================================");
        System.out.println("========================================");

        new Dog("Chak", "White", 5);
        new Bird("Samy", "Yellow", 6);

        System.out.println("Количество котов " + Cat.count);
        System.out.println("Количество собак " + Dog.count);
        System.out.println("Количество птиц " + Horse.count);
        System.out.println("Количество коней " + Bird.count);
        System.out.println("Количество водоплавающих " + SwimmingAnimal.count);
        System.out.println("Количество животных " + Animal.count);
    }
}
