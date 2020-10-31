package ru.geekbrains.common.services;

public interface AuthenticationService {
    boolean authentication(String login, String password);
}
