package ru.geekbrains.server.data;

public class ClientConnection {
    private boolean isAuthenticated;
    private String username;

    public ClientConnection() {
        this.isAuthenticated = false;
    }

    public void setAuthenticated(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

    public boolean isAuthenticated() {
        return this.isAuthenticated;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
