package ru.geekbrains.server.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.geekbrains.common.model.User;
import ru.geekbrains.common.services.AuthenticationService;
import ru.geekbrains.common.services.DataBaseService;

import java.util.HashMap;
import java.util.List;

public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger logger = LogManager.getLogger(AuthenticationServiceImpl.class);
    private HashMap<String, User> users = new HashMap<>();

    private final DataBaseService dataBaseService;

    public AuthenticationServiceImpl(DataBaseService dataBaseService) {
        this.dataBaseService = dataBaseService;
        updateUserCache();
    }

    public void updateUserCache() {
        for (User user : receiveUsers()) {
            users.put(user.getLogin(), user);
        }
    }

    private List<User> receiveUsers() {
        return dataBaseService.getUsers();
    }

    @Override
    public boolean authentication(String login, String password) {
        User user = users.get(login);
        if (user != null && user.isValidPassword(password)) {
            logger.info("User {0} is authenticated.", login);
            return true;
        }
        logger.info("User authentication {0} failed.", login);
        return false;
    }
}
