package ru.gb.homework.homework6.animals;

public abstract class SwimmingAnimal extends Animal {

    public SwimmingAnimal(String name, String color, int age) {
        super(name, color, age);

        count++;
    }

    public static int count;

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
}
