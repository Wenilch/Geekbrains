package ru.geekbrains.homework.homework1.obstacles;

import ru.geekbrains.homework.homework1.subjects.Activity;

public class Treadmill implements Obstacle {
    public Treadmill(float distance) {
        this.distance = distance;
    }

    private float distance;

    @Override
    public boolean pass(Activity activity) {
        return activity.run(distance);
    }
}
