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

    public String getType(){
        return type;
    }

    public boolean run(float distance) {
        return distance <= runDistanceLimit;
    }

    protected float runDistanceLimit;

    public float getRunDistanceLimit() {
        return this.runDistanceLimit;
    }

    public void setRunDistanceLimit(float limit) {
        this.runDistanceLimit = limit;
    }

    public boolean jump(float height) {
        return height <= jumpHeightLimit;
    }

    protected float jumpHeightLimit;

    public float getJumpHeightLimit() {
        return this.jumpHeightLimit;
    }

    public void setJumpHeightLimit(float limit) {
        this.jumpHeightLimit = limit;
    }
}
