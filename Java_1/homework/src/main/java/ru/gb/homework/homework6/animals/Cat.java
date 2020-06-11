package ru.gb.homework.homework6.animals;

public class Cat extends Animal {
    public Cat(String name, String color, int age) {
        super(name, color, age);

        runDistanceLimit = 200;
        jumpHeightLimit = 2;

        type = "Кот";

        count++;
    }

    @Override
    public void run(float distance) {
        if (distance <= getRunDistanceLimit()) {
            System.out.println(String.format("%s пробежал дистанцию.", type));
        } else {
            System.out.println(String.format("%s не смог пробежать дистанцию.", type));
        }
    }

    @Override
    public void jump(float height) {
        if (height <= getJumpHeightLimit()) {
            System.out.println(String.format("%s прыгнул на указанную высоту.", type));
        } else {
            System.out.println(String.format("%s не смог прыгнуть на указанную высоту.", type));
        }
    }

    public static int count;
}
