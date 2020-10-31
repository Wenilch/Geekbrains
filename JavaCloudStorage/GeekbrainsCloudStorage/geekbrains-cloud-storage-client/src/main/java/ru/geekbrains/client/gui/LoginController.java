package ru.geekbrains.client.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.geekbrains.client.ClientExecutorService;

public class LoginController {
    private MainController mainController;
    private Stage stage;
    private Scene mainScene;
    private double width;
    private double height;

    private ClientExecutorService clientExecutorService;
    private final FileChooser fileChooser = new FileChooser();
    private final Logger logger = LogManager.getLogger(LoginController.class);

    @FXML
    private Button connectButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField loginTextFiled;
    @FXML
    private TextField passwordTextFiled;
    @FXML
    private Label messageLabel;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMainScene(Scene scene) {
        mainScene = scene;

        loginTextFiled.setText("admin");
        passwordTextFiled.setText("admin");
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setClientExecutorService(ClientExecutorService clientExecutorService) {
        this.clientExecutorService = clientExecutorService;
    }

    @FXML
    private void login(ActionEvent event) {
        clientExecutorService.connect(this, mainController);
        clientExecutorService.authClient(loginTextFiled.getText(), passwordTextFiled.getText());
    }

    public void loginSuccessful() {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        Platform.runLater(() -> stage.setScene(mainScene));
        stage.setX((screenBounds.getWidth() - width) / 2);
        stage.setY((screenBounds.getHeight() - height) / 2);
        stage.setOnCloseRequest(event1 -> System.exit(0));
    }

    public void loginFailed(String message) {
        Platform.runLater(() -> messageLabel.setText(message));
    }

    public void notAvailableServer() {
        Platform.runLater(() -> messageLabel.setText("Сервер недоступен, попробуйте подключиться позднее!"));
    }

    @FXML
    private void cancel(ActionEvent event) {
        System.exit(0);
    }
}
