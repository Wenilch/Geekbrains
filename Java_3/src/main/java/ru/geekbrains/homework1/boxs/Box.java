package ru.geekbrains.homework1.boxs;

import ru.geekbrains.homework1.fruits.Fruit;

import java.util.ArrayList;

public class Box<T extends Fruit> {

    private ArrayList<T> fruits = new ArrayList<>();

    public ArrayList<T> getFruits() {
        return new ArrayList<>(fruits);
    }

    public void clearFruits() {
        fruits.clear();
    }

    public void pour(Box<T> otherBox) {
        if (otherBox == null)
            throw new NullPointerException("Передаваемая коробка не инициализирована");

        fruits.addAll(otherBox.getFruits());
        otherBox.clearFruits();
    }

    public float getWeight() {
        return fruits.isEmpty() ? 0f : fruits.size() * fruits.get(0).getWeight();
    }

    public void add(T fruit) {
        fruits.add(fruit);
    }

    public boolean compare(Box<? extends Fruit> otherBox) {
        if (otherBox == null)
            return false;

        return this.getWeight() == otherBox.getWeight();
    }
}
