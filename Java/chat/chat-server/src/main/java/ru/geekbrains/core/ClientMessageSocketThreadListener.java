package ru.geekbrains.core;

import ru.geekbrains.chat.common.MessageLibrary;
import ru.geekbrains.net.MessageSocketThreadListener;

public class ClientMessageSocketThreadListener implements MessageSocketThreadListener {

    private ChatServer chatServer;
    private ClientSessionThread clientSession;
    private AuthController authController;

    public ClientMessageSocketThreadListener(ChatServer chatServer, AuthController authController) {
        this.chatServer = chatServer;
        this.authController = authController;
    }

    public ClientSessionThread getClientSession() {
        return clientSession;
    }

    public void setClientSession(ClientSessionThread clientSession) {
        this.clientSession = clientSession;
    }

    @Override
    public void onSocketReady() {
        chatServer.logMessage("Socket ready");
    }

    @Override
    public void onSocketClosed() {
        chatServer.logMessage("Socket Closed");
    }

    @Override
    public void onMessageReceived(String msg) {
        if (clientSession.isAuthorized()) {
            if (msg.startsWith(MessageLibrary.TYPE_BROADCAST + MessageLibrary.DELIMITER)) {
                for (ClientSessionThread client : chatServer.clients) {
                    if (client.getNickname() != clientSession.getNickname()) {
                        chatServer.logMessage(msg);
                        client.sendMessage(msg);
                    }
                }
            } else {
                processAuthorizedUserMessage(msg);
            }
        } else {
            processUnauthorizedUserMessage(msg);
        }
    }

    private void processAuthorizedUserMessage(String msg) {
        chatServer.logMessage(msg);
        clientSession.sendMessage("echo: " + msg);
    }

    private void processUnauthorizedUserMessage(String msg) {
        String[] arr = msg.split(MessageLibrary.DELIMITER);
        if (arr.length < 4 ||
                !arr[0].equals(MessageLibrary.AUTH_METHOD) ||
                !arr[1].equals(MessageLibrary.AUTH_REQUEST)) {
            clientSession.authError("Incorrect request: " + msg);
            return;
        }
        String login = arr[2];
        String password = arr[3];
        String nickname = authController.getNickname(login, password);
        if (nickname == null) {
            clientSession.authDeny();
            return;
        }
        clientSession.authAccept(nickname);
    }

    @Override
    public void onException(Throwable throwable) {
        throwable.printStackTrace();
    }
}
