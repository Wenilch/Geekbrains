package ru.geekbrains.core;

import org.apache.logging.log4j.Logger;
import ru.geekbrains.chat.common.MessageLibrary;
import ru.geekbrains.data.User;
import ru.geekbrains.db.DbSqlite;
import ru.geekbrains.net.MessageSocketRunnable;
import ru.geekbrains.net.MessageSocketRunnableListener;
import ru.geekbrains.net.ServerSocketThread;
import ru.geekbrains.net.ServerSocketThreadListener;

import java.net.Socket;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer implements ServerSocketThreadListener, MessageSocketRunnableListener {

    private ServerSocketThread serverSocketThread;
    private ChatServerListener listener;
    private Logger logger;
    private AuthController authController;
    private Vector<ClientSessionRunnable> clients = new Vector<>();
    private ExecutorService clientSessionExecutorService = Executors.newCachedThreadPool();
    private ClientSessionSchedulerThread clientSessionSchedulerThread = new ClientSessionSchedulerThread();
    private DbSqlite instance;

    public ChatServer(ChatServerListener listener, Logger logger) throws SQLException {
        this.listener = listener;
        this.logger = logger;
        this.instance = DbSqlite.getInstance();
    }

    public void start(int port) {
        if (serverSocketThread != null && serverSocketThread.isAlive()) {
            return;
        }
        serverSocketThread = new ServerSocketThread(this, "Chat-Server-Socket-Thread", port, 2000);
        serverSocketThread.start();
        authController = new AuthController(this.instance);
    }

    public void stop() {
        if (serverSocketThread == null || !serverSocketThread.isAlive()) {
            return;
        }
        serverSocketThread.interrupt();
        disconnectAll();
    }

    /*
     * Server Socket Thread Listener Methods
     */

    @Override
    public void onClientConnected() {
        logger.trace("Клиент подключился.");
        logMessage("Client connected");
    }

    @Override
    public void onSocketAccepted(Socket socket) {
        ClientSessionRunnable clientSessionRunnable = new ClientSessionRunnable(this, "ClientSessionThread", socket);
        clients.add(clientSessionRunnable);
        clientSessionExecutorService.execute(clientSessionRunnable);

        if (!clientSessionSchedulerThread.isAlive()) {
            clientSessionSchedulerThread = new ClientSessionSchedulerThread();
            clientSessionSchedulerThread.start();
        }

        logger.trace("Клиентская сессия запущена.");
    }

    @Override
    public void onException(Throwable throwable) {
        logger.error("Произошла ошибка {} {}.", throwable.getMessage(), throwable);
        throwable.printStackTrace();
    }

    /*
     * Message Socket Thread Listener Methods
     */

    @Override
    public void onClientTimeout(Throwable throwable) {
        logger.error("Таймаут клинта {} {}.", throwable.getMessage(), throwable);
    }

    @Override
    public void onSocketReady(MessageSocketRunnable thread) {
        logger.trace("Сокет готов к работе.");
        logMessage("Socket ready");
    }

    @Override
    public void onSocketClosed(MessageSocketRunnable thread) {
        ClientSessionRunnable clientSession = (ClientSessionRunnable) thread;
        logger.trace("Сокет закрыт.");
        logMessage("Socket Closed");
        clients.remove(thread);
        if (clientSession.isAuthorized() && !clientSession.isReconnected()) {
            sendToAllAuthorizedClients(MessageLibrary.getBroadcastMessage("server", "User " + clientSession.getNickname() + " disconnected"));
        }
        sendToAllAuthorizedClients(MessageLibrary.getUserList(getUsersList()));
    }

    @Override
    public void onMessageReceived(MessageSocketRunnable thread, String msg) {
        ClientSessionRunnable clientSession = (ClientSessionRunnable) thread;
        logger.trace("Пришло сообщение от клиента.");
        if (clientSession.isAuthorized()) {
            if (msg.contains(MessageLibrary.CHANGE_NICKNAME)) {

                String[] arr = msg.split(MessageLibrary.DELIMITER);
                String newNickname = arr[1];

                logger.trace("Пришла команда изменения ника клиента {} на {}.", clientSession.getUser().getNickname(), newNickname);

                instance.setUserNickname(clientSession.getUser().getId(), newNickname);
                clientSession.getUser().setNickname(newNickname);

                thread.sendMessage(MessageLibrary.getChangeNickname(newNickname));

                sendToAllAuthorizedClients(MessageLibrary.getUserList(getUsersList()));
            } else {
                processAuthorizedUserMessage(msg);
            }
        } else {
            logger.trace("Авторизация клиента.");
            processUnauthorizedUserMessage(clientSession, msg);
        }
    }

    @Override
    public void onException(MessageSocketRunnable thread, Throwable throwable) {
        throwable.printStackTrace();
    }

    private void processAuthorizedUserMessage(String msg) {
        logMessage(msg);
        logger.trace("Отправка всем пользователям сообщения: {}.", msg);

        for (ClientSessionRunnable client : clients) {
            if (!client.isAuthorized()) {
                continue;
            }
            client.sendMessage(msg);
        }
    }

    private void sendToAllAuthorizedClients(String msg) {
        logger.trace("Отправка всем пользователям сообщения: {}.", msg);

        for (ClientSessionRunnable client : clients) {
            if (!client.isAuthorized()) {
                continue;
            }
            client.sendMessage(msg);
        }
    }

    private void processUnauthorizedUserMessage(ClientSessionRunnable clientSession, String msg) {
        String[] arr = msg.split(MessageLibrary.DELIMITER);
        if (arr.length < 4 ||
                !arr[0].equals(MessageLibrary.AUTH_METHOD) ||
                !arr[1].equals(MessageLibrary.AUTH_REQUEST)) {
            clientSession.authError("Incorrect request: " + msg);
            return;
        }
        String login = arr[2];
        String password = arr[3];
        User user = authController.getUser(login, password);
        if (user == null) {
            logger.warn("Клиент с парой логин {} пароль {} не найден.", login, password);
            clientSession.authDeny();
            return;
        } else {
            logger.trace("Клиент {} успешно авторизован.", user.getNickname());
            ClientSessionRunnable oldClientSession = findClientSessionByNickname(user.getNickname());
            clientSession.authAccept(user);
            if (oldClientSession == null) {
                sendToAllAuthorizedClients(MessageLibrary.getBroadcastMessage("Server", user.getNickname() + " connected"));
            } else {
                oldClientSession.setReconnected(true);
                clients.remove(oldClientSession);
            }
        }

        logger.trace("Отправка обновленного списка пользователей.");
        sendToAllAuthorizedClients(MessageLibrary.getUserList(getUsersList()));
    }

    public void disconnectAll() {
        logger.trace("Отключение всех пользователей.");
        ArrayList<ClientSessionRunnable> currentClients = new ArrayList<>(clients);
        for (ClientSessionRunnable client : currentClients) {
            client.close();
            clients.remove(client);
        }

    }

    private void logMessage(String msg) {
        listener.onChatServerMessage(msg);
    }

    public String getUsersList() {
        StringBuilder sb = new StringBuilder();
        for (ClientSessionRunnable client : clients) {
            if (!client.isAuthorized()) {
                continue;
            }
            sb.append(client.getNickname()).append(MessageLibrary.DELIMITER);
        }
        return sb.toString();
    }

    private ClientSessionRunnable findClientSessionByNickname(String nickname) {
        for (ClientSessionRunnable client : clients) {
            if (!client.isAuthorized()) {
                continue;
            }
            if (client.getNickname().equals(nickname)) {
                return client;
            }
        }
        return null;
    }

    private class ClientSessionSchedulerThread extends Thread {
        @Override
        public void run() {
            boolean anyUnauthorizedUsers = true;

            while (anyUnauthorizedUsers) {
                anyUnauthorizedUsers = false;
                ArrayList<ClientSessionRunnable> currentClients = new ArrayList<>(clients);

                for (ClientSessionRunnable client : currentClients) {
                    if (!client.isAuthorized()) {
                        anyUnauthorizedUsers = true;
                        if (Instant.now().getEpochSecond() - client.getStartTime() > 120) {
                            client.authDeny();
                            clients.remove(client);
                            logger.trace("Пользователь отключен по таймауту авторизации.");
                        }
                    }
                }
            }

        }
    }
}
