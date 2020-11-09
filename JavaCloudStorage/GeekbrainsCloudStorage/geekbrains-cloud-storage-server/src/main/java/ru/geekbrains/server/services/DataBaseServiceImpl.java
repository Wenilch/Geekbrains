package ru.geekbrains.server.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.geekbrains.common.configuration.CommonConfigurations;
import ru.geekbrains.common.model.User;
import ru.geekbrains.common.services.DataBaseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseServiceImpl implements DataBaseService {
    private static final Logger logger = LogManager.getLogger(DataBaseServiceImpl.class);

    private static Connection connection;

    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();

        try {
            connect();
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user")) {
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    users.add(
                            new User(rs.getString(2), rs.getString(3), rs.getString(4)));
                }
            }
        } catch (SQLException | ClassNotFoundException exception) {
            logger.error("Ошибка при выполнении запроса получения списка пользователей {0}", exception);
        } finally {
            try {
                connection.close();
            } catch (SQLException exception) {
                logger.error(exception);
            }
        }
        return users;
    }

    private void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection(CommonConfigurations.DATABASE_CONNECTION_STRING);
    }
}
