package ru.geekbrains.gui.fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.geekbrains.core.ChatServer;
import ru.geekbrains.core.ChatServerListener;

import java.sql.SQLException;

public class Controller implements ChatServerListener {

    @FXML
    public TextArea logArea;

    private final Logger logger = LogManager.getLogger("Server");

    private ChatServer chatServer = new ChatServer(this, logger);

    public Controller() throws SQLException {
    }

    public void start(ActionEvent actionEvent) {
        chatServer.start(8181);
    }

    public void stop(ActionEvent actionEvent) {
        chatServer.stop();
    }

    public void disconnectAll(ActionEvent actionEvent) {
        chatServer.disconnectAll();
    }

    @Override
    public void onChatServerMessage(String msg) {
        logArea.appendText(msg + System.lineSeparator());
    }
}
