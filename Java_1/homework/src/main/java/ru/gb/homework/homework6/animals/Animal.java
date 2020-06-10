package ru.gb.homework.homework6.animals;

public abstract class Animal {

    public Animal(String name, String color, int age) {
        this.name = name;
        this.color = color;
        this.age = age;

        count++;
    }

    public static int count;

    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String color;

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    private int age;

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    protected String type;

    public void run(float distance) {
        if (distance <= runDistanceLimit) {
            System.out.println(String.format("%s пробежала дистанцию.", type));
        } else {
            System.out.println(String.format("%s не смогла пробежать дистанцию.", type));
        }
    }

    protected float runDistanceLimit;

    public float getRunDistanceLimit() {
        return this.runDistanceLimit;
    }

    public void setRunDistanceLimit(float limit) {
        this.runDistanceLimit = limit;
    }

    public void swim(float distance){
        if (distance <= swimDistanceLimit){
            System.out.println(String.format("%s проплыла дистанцию.", type));
        } else {
            System.out.println(String.format("%s не смогла проплыть дистанцию.", type));
        }
    }

    protected float swimDistanceLimit;

    public float getSwimDistanceLimit() {
        return this.swimDistanceLimit;
    }

    public void setSwimDistanceLimit(float limit) {
        this.swimDistanceLimit = limit;
    }

    public void jump(float height) {
        if (height <= jumpHeightLimit) {
            System.out.println(String.format("%s прыгнула на указанную высоту.", type));
        } else {
            System.out.println(String.format("%s не смогла прыгнуть на указанную высоту.", type));
        }
    }

    protected float jumpHeightLimit;

    public float getJumpHeightLimit() {
        return this.jumpHeightLimit;
    }

    public void setJumpHeightLimit(float limit) {
        this.jumpHeightLimit = limit;
    }
}
