package ru.geekbrains.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.geekbrains.client.gui.LoginController;
import ru.geekbrains.client.gui.MainController;

import java.io.IOException;

public class MainApplication extends Application {
    private final ClientExecutorService clientExecutorService = new ClientExecutorService();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = loader.load();
        Scene loginScene = new Scene(root, 500, 500);

        LoginController loginController = loader.getController();

        FXMLLoader loaderMain = new FXMLLoader(getClass().getResource("/main.fxml"));
        Parent rootMain = loaderMain.load();
        Scene mainScene = new Scene(rootMain, 1000, 1000);
        MainController mainController = loaderMain.getController();

        loginController.setStage(primaryStage);
        loginController.setMainScene(mainScene);
        loginController.setMainController(mainController);
        loginController.setWidth(1000);
        loginController.setHeight(1000);
        loginController.setClientExecutorService(clientExecutorService);

        mainController.setStage(primaryStage);
        mainController.setMainScene(mainScene);
        mainController.setClientExecutorService(clientExecutorService);
        clientExecutorService.setScene(mainScene);

        primaryStage.setTitle("Geekbrains");

        primaryStage.setScene(loginScene);
        primaryStage.show();
    }
}