package ru.gb.homework.homework6.animals;

public abstract class SwimmingAnimal extends Animal {

    public SwimmingAnimal(String name, String color, int age) {
        super(name, color, age);

        count++;
    }

    public static int count;

    public boolean swim(float distance) {
        return distance <= swimDistanceLimit;
    }

    protected float swimDistanceLimit;

    public float getSwimDistanceLimit() {
        return this.swimDistanceLimit;
    }

    public void setSwimDistanceLimit(float limit) {
        this.swimDistanceLimit = limit;
    }
}
