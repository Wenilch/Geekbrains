package ru.geekbrains.server;

public class ChatServer {

    private static ServerSocketThread serverSocketThread;

    public void start(int port) {
        if (serverSocketThread != null && serverSocketThread.isAlive()) {
            return;
        }
        serverSocketThread = new ServerSocketThread("Chat-Server-Socket-Thread", port);
        serverSocketThread.start();
    }

    public void stop() {
        if (serverSocketThread == null || !serverSocketThread.isAlive()) {
            return;
        }
        serverSocketThread.interrupt();
    }


    /*public static void main(String[] args) {
        serverSocketThread = new ServerSocketThread();
        serverSocketThread.start();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + ": running");
            }
        });
       t.start();
        try {
            Thread.currentThread().sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        serverSocketThread.interrupt();
        System.out.println(Thread.currentThread().getName() + ": Main");
    }*/
}
