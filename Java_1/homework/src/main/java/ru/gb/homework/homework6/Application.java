package ru.gb.homework.homework6;

import ru.gb.homework.homework6.animals.*;

public class Application {

    public static void main(String[] args) {
        Cat cat = new Cat("Boris", "Black", 10);

        printRunResultMessage(cat.run(1000f), cat.getType());
        printJumpResultMessage(cat.jump(1000f), cat.getType());

        cat.setRunDistanceLimit(10000);
        printRunResultMessage(cat.run(1000f), cat.getType());

        System.out.println("========================================");
        System.out.println("========================================");

        Dog dog = new Dog("Chak", "White", 5);
        Horse horse = new Horse("Jack", "Green", 1);
        Bird bird = new Bird("Samy", "Yellow", 6);

        printRunResultMessage(dog.run(1000f), dog.getType());
        printSwimResultMessage(dog.swim(1000f), dog.getType());
        printJumpResultMessage(dog.jump(1000f), dog.getType());

        System.out.println("========================================");
        System.out.println("========================================");

        printRunResultMessage(horse.run(1000f), horse.getType());
        printSwimResultMessage(horse.swim(1000f), horse.getType());
        printJumpResultMessage(horse.jump(1000f), horse.getType());

        horse.setRunDistanceLimit(0);
        printRunResultMessage(horse.run(1000f), horse.getType());

        System.out.println("========================================");
        System.out.println("========================================");

        bird.run(1000f);
        printJumpResultMessage(bird.jump(1000f), bird.getType());

        bird.setJumpHeightLimit(10000);
        printJumpResultMessage(bird.jump(1000f), bird.getType());

        System.out.println("========================================");
        System.out.println("========================================");

        Animal[] animals = new Animal[]{cat, dog, bird, horse, new Dog("Chak", "White", 5), new Bird("Samy", "Yellow", 6)};
        printAnimalStatistic(animals);
    }

    private static void printRunResultMessage(boolean isRun, String animalType) {
        if (isRun) {
            System.out.println(String.format("%s пробежала дистанцию.", animalType));
        } else {
            System.out.println(String.format("%s не смогла пробежать дистанцию.", animalType));
        }
    }

    private static void printSwimResultMessage(boolean isRun, String animalType) {
        if (isRun) {
            System.out.println(String.format("%s проплыла дистанцию.", animalType));
        } else {
            System.out.println(String.format("%s не смогла проплыть дистанцию.", animalType));
        }
    }

    private static void printJumpResultMessage(boolean isRun, String animalType) {
        if (isRun) {
            System.out.println(String.format("%s прыгнула на указанную высоту.", animalType));
        } else {
            System.out.println(String.format("%s не смогла прыгнуть на указанную высоту.", animalType));
        }
    }

    private static void printAnimalStatistic(Animal[] animals) {
        int catCount = 0;
        int dogCount = 0;
        int birdCount = 0;
        int horseCount = 0;
        for (Animal animal : animals) {
            if (animal instanceof Cat) {
                catCount++;
            } else if (animal instanceof Dog) {
                dogCount++;
            } else if (animal instanceof Bird) {
                birdCount++;
            } else {
                horseCount++;
            }
        }

        printAnimalStatisticMessage(catCount, dogCount, birdCount, horseCount);

        System.out.println("========================================");
        System.out.println("========================================");

        printAnimalStatisticMessage(Cat.count, Dog.count, Bird.count, Horse.count);

        System.out.println("Количество водоплавающих " + SwimmingAnimal.count);
        System.out.println("Количество животных " + Animal.count);
    }

    private static void printAnimalStatisticMessage(int cat, int dog, int bird, int horse) {
        System.out.println("Количество котов " + cat);
        System.out.println("Количество собак " + dog);
        System.out.println("Количество птиц " + bird);
        System.out.println("Количество коней " + horse);
    }
}
