package ru.geekbrains.client.gui;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MessageBox {
    private final Logger logger = LogManager.getLogger(MessageBox.class);

    private Scene scene;
    private String message;

    public MessageBox() {

    }

    public MessageBox(Scene scene, String message) {
        this.scene = scene;
        this.message = message;
    }

    public Scene getScene() {
        return scene;
    }

    public String getMessage() {
        return message;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void showError() {
        show(scene, "Ошибка", Alert.AlertType.ERROR, message);
    }

    public void showInformation() {
        show(scene, "Информация", Alert.AlertType.INFORMATION, message);
    }

    private void show(Scene scene, String header, Alert.AlertType alertType, String message) {
        try {
            Alert alert = new Alert(alertType);
            alert.setTitle("geekbrains.client.cloud.storage");
            alert.setHeaderText(header);
            alert.setContentText(message);

            alert.initOwner(scene.getWindow());
            alert.showAndWait();

        } catch (Exception exception) {
            logger.error(exception);
        }
    }
}
