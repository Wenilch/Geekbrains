package ru.geekbrains.homework4;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;

public class Car implements Runnable {
    private static int CARS_COUNT;

    static {
        CARS_COUNT = 0;
    }

    private Race race;
    private int speed;
    private String name;
    // Ожидание подготовки
    private CyclicBarrier cyclicBarrier;
    // Ожидание готовности
    private CyclicBarrier waitCarIsReady;
    // Ожидание начала гонки
    private CountDownLatch waitStartRace;
    // Ожидание финиша последней машины
    private CountDownLatch waitCarIsFinish;
    // Блокировка победителя
    private Lock winnerLock;
    // Ожидание всех машин, для корректной разблокировки "блокировки победителя" )
    private CyclicBarrier winnerWaitAllCarsIsFinish;


    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed, CyclicBarrier cyclicBarrier, CyclicBarrier waitCarIsReady, CountDownLatch waitStartRace, Lock winnerLock, CyclicBarrier winnerWaitAllCarsIsFinish, CountDownLatch waitCarIsFinish) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
        this.cyclicBarrier = cyclicBarrier;
        this.waitCarIsReady = waitCarIsReady;
        this.waitCarIsFinish = waitCarIsFinish;
        this.waitStartRace = waitStartRace;
        this.winnerLock = winnerLock;
        this.winnerWaitAllCarsIsFinish = winnerWaitAllCarsIsFinish;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            cyclicBarrier.await();
            System.out.println(this.name + " готов");
            waitStartRace.countDown();
            waitCarIsReady.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }
        try {
            if (winnerLock.tryLock()) {
                System.out.println(this.name + "- Win");
                winnerWaitAllCarsIsFinish.await();
                winnerLock.unlock();
            } else {
                winnerWaitAllCarsIsFinish.await();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        waitCarIsFinish.countDown();
    }
}
