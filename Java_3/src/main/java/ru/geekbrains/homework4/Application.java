package ru.geekbrains.homework4;

import ru.geekbrains.homework4.stages.Road;
import ru.geekbrains.homework4.stages.Tunnel;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Application {
    public static final int CARS_COUNT = 4;

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        CyclicBarrier cyclicBarrier = new CyclicBarrier(CARS_COUNT);
        CyclicBarrier waitAllCarsIsReady = new CyclicBarrier(CARS_COUNT + 1);
        CountDownLatch startRace = new CountDownLatch(CARS_COUNT);
        CountDownLatch waitAllCarsIsFinish = new CountDownLatch(CARS_COUNT);
        Lock winnerLock = new ReentrantLock();
        CyclicBarrier winnerWaitAllCarsIsFinish = new CyclicBarrier(CARS_COUNT);

        Race race = new Race(new Road(60), new Tunnel(2), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10), cyclicBarrier, waitAllCarsIsReady, startRace, winnerLock, winnerWaitAllCarsIsFinish, waitAllCarsIsFinish);
        }

        for (int i = 0; i < cars.length; i++) {
            new Thread(cars[i]).start();
        }

        startRace.await();
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
        waitAllCarsIsReady.await();


        waitAllCarsIsFinish.await();
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}