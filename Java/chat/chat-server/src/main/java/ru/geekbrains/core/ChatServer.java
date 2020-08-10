package ru.geekbrains.core;

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
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer implements ServerSocketThreadListener, MessageSocketRunnableListener {

    private ServerSocketThread serverSocketThread;
    private ChatServerListener listener;
    private AuthController authController;
    private Vector<ClientSessionRunnable> clients = new Vector<>();
    private ExecutorService clientSessionExecutorService = Executors.newCachedThreadPool();
    private ClientSessionSchedulerThread clientSessionSchedulerThread = new ClientSessionSchedulerThread();
    private DbSqlite instance;

    public ChatServer(ChatServerListener listener) throws SQLException {
        this.listener = listener;
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
    }

    @Override
    public void onException(Throwable throwable) {
        throwable.printStackTrace();
    }

    /*
     * Message Socket Thread Listener Methods
     */

    @Override
    public void onClientTimeout(Throwable throwable) {
    }

    @Override
    public void onSocketReady(MessageSocketRunnable thread) {
        logMessage("Socket ready");
    }

    @Override
    public void onSocketClosed(MessageSocketRunnable thread) {
        ClientSessionRunnable clientSession = (ClientSessionRunnable) thread;
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
        if (clientSession.isAuthorized()) {
            if (msg.contains(MessageLibrary.CHANGE_NICKNAME)) {
                String[] arr = msg.split(MessageLibrary.DELIMITER);
                String newNickname = arr[1];

                instance.setUserNickname(clientSession.getUser().getId(), newNickname);
                clientSession.getUser().setNickname(newNickname);

                thread.sendMessage(MessageLibrary.getChangeNickname(newNickname));

                sendToAllAuthorizedClients(MessageLibrary.getUserList(getUsersList()));
            } else {
                processAuthorizedUserMessage(msg);
            }
        } else {
            processUnauthorizedUserMessage(clientSession, msg);
        }
    }

    @Override
    public void onException(MessageSocketRunnable thread, Throwable throwable) {
        throwable.printStackTrace();
    }

    private void processAuthorizedUserMessage(String msg) {
        logMessage(msg);
        for (ClientSessionRunnable client : clients) {
            if (!client.isAuthorized()) {
                continue;
            }
            client.sendMessage(msg);
        }
    }

    private void sendToAllAuthorizedClients(String msg) {
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
            clientSession.authDeny();
            return;
        } else {
            ClientSessionRunnable oldClientSession = findClientSessionByNickname(user.getNickname());
            clientSession.authAccept(user);
            if (oldClientSession == null) {
                sendToAllAuthorizedClients(MessageLibrary.getBroadcastMessage("Server", user.getNickname() + " connected"));
            } else {
                oldClientSession.setReconnected(true);
                clients.remove(oldClientSession);
            }
        }
        sendToAllAuthorizedClients(MessageLibrary.getUserList(getUsersList()));
    }

    public void disconnectAll() {
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
                        }
                    }
                }
            }

        }
    }
}
