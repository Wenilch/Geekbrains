package ru.geekbrains.common.services;

import ru.geekbrains.common.model.User;

import java.util.List;

public interface DataBaseService {
    List<User> getUsers();
}
