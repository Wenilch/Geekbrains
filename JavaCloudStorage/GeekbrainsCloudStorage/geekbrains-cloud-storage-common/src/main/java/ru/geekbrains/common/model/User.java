package ru.geekbrains.common.model;

public class User {

    private final String login;
    private final String password;
    private final String firstname;

    public User(String login, String password, String firstname) {
        this.login = login;
        this.password = password;
        this.firstname = firstname;
    }

    public String getLogin() {
        return login;
    }

    public String getFirstName() {
        return firstname;
    }

    public boolean isValidPassword(String password) {
        return this.password.equals(password);
    }
}
