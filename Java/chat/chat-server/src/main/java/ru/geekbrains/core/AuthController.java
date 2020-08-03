package ru.geekbrains.core;

import ru.geekbrains.data.User;
import ru.geekbrains.db.DbSqlite;

public class AuthController {

    private DbSqlite instance;

    public AuthController(DbSqlite instance) {
        this.instance = instance;
    }

    public User getUser(String login, String password) {
        return instance.getUser(login, password);
    }
}
