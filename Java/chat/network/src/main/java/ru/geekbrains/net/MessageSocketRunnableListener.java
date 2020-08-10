package ru.geekbrains.net;

public interface MessageSocketRunnableListener {

    void onSocketReady(MessageSocketRunnable thread);
    void onSocketClosed(MessageSocketRunnable thread);
    void onMessageReceived(MessageSocketRunnable thread, String msg);
    void onException(MessageSocketRunnable thread, Throwable throwable);

}
