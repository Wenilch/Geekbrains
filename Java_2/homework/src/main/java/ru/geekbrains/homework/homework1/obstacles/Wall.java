package ru.geekbrains.homework.homework1.obstacles;

import ru.geekbrains.homework.homework1.subjects.Activity;

public class Wall implements Obstacle  {

    public Wall(float height) {
        this.height = height;
    }

    private float height;

    @Override
    public boolean pass(Activity activity) {
        return activity.jump(height);
    }
}
