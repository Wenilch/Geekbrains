package ru.geekbrains.homework3;

/**
 * 1. Создать три потока, каждый из которых выводит определенную букву (A, B и C) 5 раз (порядок – ABСABСABС).
 * Используйте wait/notify/notifyAll.
 */
public class Application {
    private final int repeatCount = 5;

    static volatile boolean printA = true;
    static volatile boolean printB = false;
    static volatile boolean printC = false;

    private static final String SymbolA = "A" ;
    private static final String SymbolB = "B" ;
    private static final String SymbolC = "C" ;

    public static void main(String[] args) throws InterruptedException {
        final int repeatCount = 5;
        final Object monitor = new Object();

        Thread A = new Thread(() -> {
            synchronized (monitor) {
                try {
                    for (int i = 0; i < repeatCount; i++) {
                        while (!printA) {
                            monitor.wait();
                        }

                        System.out.println(SymbolA);
                        printA = false;
                        printB = true;
                        monitor.notify();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        Thread B = new Thread(() -> {
            synchronized (monitor) {
                String symbol = "B";
                try {
                    for (int i = 0; i < repeatCount; i++) {
                        while (!printB) {
                            monitor.wait();
                        }

                        System.out.println(SymbolB);
                        printB = false;
                        printC = true;
                        monitor.notifyAll();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

            }
        });

        Thread C = new Thread(() -> {
            synchronized (monitor) {
                String symbol = "C";
                try {
                    for (int i = 0; i < repeatCount; i++) {

                        while (!printC) {
                            monitor.wait();
                        }

                        System.out.println(SymbolC);
                        printC = false;
                        printA = true;
                        monitor.notify();
                    }

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        A.start();
        B.start();
        C.start();

        A.join();
        B.join();
        C.join();

        System.out.println("Завершено");
    }
}
