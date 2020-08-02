package ru.geekbrains.data;

public class User {
    private int id;
    private String login;
    private String password;
    private String nickname;

    public User(int id, String login, String password, String nickname) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.nickname = nickname;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getNickname() {
        return nickname;
    }

    public boolean isPasswordCorrect(String password) {
        return this.password.equals(password);
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
