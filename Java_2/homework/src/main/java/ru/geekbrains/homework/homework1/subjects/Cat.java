package ru.geekbrains.homework.homework1.subjects;

public class Cat implements Activity {

    public Cat(String name, float distanceLimit, float heightLimit) {
        this.name = name;
        this.distanceLimit = distanceLimit;
        this.heightLimit = heightLimit;
    }

    private String name;

    public String getName() {
        return name;
    }

    public String getType() {
        return "Кот";
    }

    private float distanceLimit;

    @Override
    public boolean run(float distance) {
        return distance < distanceLimit;
    }

    private float heightLimit;

    @Override
    public boolean jump(float height) {
        return height < heightLimit;
    }
}
