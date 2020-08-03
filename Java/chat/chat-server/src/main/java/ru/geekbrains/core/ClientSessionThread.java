package ru.geekbrains.core;

import ru.geekbrains.chat.common.MessageLibrary;
import ru.geekbrains.data.User;
import ru.geekbrains.net.MessageSocketThread;
import ru.geekbrains.net.MessageSocketThreadListener;

import java.net.Socket;
import java.time.Instant;

public class ClientSessionThread extends MessageSocketThread {

    private boolean isAuthorized = false;
    private String nickname;
    private boolean reconnected = false;
    private long startTime;
    private User user;

    public ClientSessionThread(MessageSocketThreadListener listener, String name, Socket socket) {
        super(listener, name, socket);
        startTime = Instant.now().getEpochSecond();
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    public String getNickname() {
        return user.getNickname();
    }

    public long getStartTime() {
        return startTime;
    }

    public User getUser() {
        return this.user;
    }

    public void authAccept(User user) {
        this.user = user;
        this.isAuthorized = true;
        sendMessage(MessageLibrary.getAuthAcceptMessage(this.user.getNickname()));
    }

    public void authDeny() {
        sendMessage(MessageLibrary.getAuthDeniedMessage());
        close();
    }

    public void authError(String msg) {
        sendMessage(MessageLibrary.getMsgFormatErrorMessage(msg));
        close();
    }

    public void setAuthorized(boolean authorized) {
        isAuthorized = authorized;
    }

    public void setNickname(String nickname) {
        user.setNickname(nickname);
    }

    public boolean isReconnected() {
        return reconnected;
    }

    public void setReconnected(boolean reconnected) {
        this.reconnected = reconnected;
    }
}
