package ru.geekbrains.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MessageSocketRunnable implements Runnable {

    private Socket socket;
    private MessageSocketRunnableListener listener;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean isClosed = false;
    private String name;

    public MessageSocketRunnable(MessageSocketRunnableListener listener, String name, Socket socket) {
        this.name = name;
        this.socket = socket;
        this.listener = listener;
    }

    @Override
    public void run() {
        Thread.currentThread().setName(name);
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            listener.onSocketReady(this);
            while (!Thread.currentThread().isInterrupted()) {
                if (!isClosed) {
                    listener.onMessageReceived(this, in.readUTF());
                }
            }
        } catch (IOException e) {
            close();
            System.out.println(e);
        } finally {
            close();
        }
    }

    public void sendMessage(String message) {
        try {
            if (!socket.isConnected() || socket.isClosed() || isClosed) {
                listener.onException(this, new RuntimeException("Socked closed or not initialized"));
                return;
            }
            if (!isClosed) {
                out.writeUTF(message);
            }
        } catch (IOException e) {
            close();
            listener.onException(this, e);
        }
    }

    public synchronized void close() {
        isClosed = true;
        Thread.currentThread().interrupt();
        try {
            if (out != null) {
                out.close();
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        listener.onSocketClosed(this);
    }
}
