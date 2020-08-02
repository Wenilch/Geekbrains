package ru.geekbrains.db;

import org.sqlite.JDBC;
import ru.geekbrains.data.User;

import java.sql.*;
import java.util.ArrayList;

public class DbSqlite implements AutoCloseable {
    private static final String CONNECTION_STRING = "jdbc:sqlite:chat-server\\chat.db";

    private static DbSqlite instance = null;

    public static DbSqlite getInstance() throws SQLException {
        if (instance == null) {
            instance = new DbSqlite();
        }

        return instance;
    }

    private Connection connection;

    private DbSqlite() throws SQLException {
        DriverManager.registerDriver(new JDBC());
        connection = DriverManager.getConnection(CONNECTION_STRING);
    }

    public User getUser(String login, String password) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM client WHERE login = ? and password = ?")) {
            statement.setString(1, login);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(resultSet.getInt("id"), resultSet.getString("login"), resultSet.getString("password"), resultSet.getString("nickname"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM client")) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                users.add(new User(resultSet.getInt("id"), resultSet.getString("login"), resultSet.getString("password"), resultSet.getString("nickname")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public boolean setUserNickname(Integer id, String newNickname) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE client SET nickname = ? WHERE id = ?")) {
            statement.setString(1, newNickname);
            statement.setInt(2, id);
            return statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void close() throws Exception {
        if (connection != null && !connection.isClosed()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
