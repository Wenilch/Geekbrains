package ru.geekbrains.homework4.stages;

import ru.geekbrains.homework4.Car;

public abstract class Stage {
    protected int length;
    protected String description;

    public String getDescription() {
        return description;
    }

    public abstract void go(Car c);
}
