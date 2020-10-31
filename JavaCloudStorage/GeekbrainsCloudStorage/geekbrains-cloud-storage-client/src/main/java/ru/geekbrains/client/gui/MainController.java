package ru.geekbrains.client.gui;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.geekbrains.client.ClientExecutorService;
import ru.geekbrains.client.data.UserFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class MainController {
    private Stage stage;
    private Scene mainScene;
    private UserFile selectedFile;
    private final String FILE_SEND_ERROR = "Невозможно отправить файл";
    private final String FILE_RENAME_ERROR = "Возникла ошибка при переименовании файла, попробуйте еще раз";
    private final String FILE_DELETE_ERROR = "Возникла ошибка при удалении файла";
    private final String FILE_DOWNLOAD_INFO = "Файл сохранен на локальном диске";

    private final FileChooser fileChooser = new FileChooser();
    private final Logger logger = LogManager.getLogger(MainController.class);
    private ClientExecutorService clientExecutorService;

    @FXML
    private Button renameButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button downloadButton;
    @FXML
    private Label totalFilesLabel;
    @FXML
    private Label totalSizeLabel;

    @FXML
    private TableView<UserFile> fileTableView;
    @FXML
    private TableColumn<UserFile, String> fileNameTableColumn;
    @FXML
    private TableColumn<UserFile, String> fileSizeTableColumn;
    @FXML
    private TableColumn<UserFile, String> fileDateTableColumn;


    public void setStage(Stage stage) {
        this.stage = stage;
        disableButtons();

        this.stage.setOnCloseRequest(event -> System.exit(0));
    }

    public void setMainScene(Scene scene) {
        mainScene = scene;
    }

    public void setClientExecutorService(ClientExecutorService clientExecutorService) {
        this.clientExecutorService = clientExecutorService;
    }

    public void fillFileTable(ObservableList<UserFile> list) {

        if (list == null) {
            fileTableView.setItems(null);
            fileTableView.refresh();
            Platform.runLater(() -> totalSizeLabel.setText(""));
            Platform.runLater(() -> totalFilesLabel.setText(""));
            return;
        }

        fileNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        fileSizeTableColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        fileDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        fileSizeTableColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        fileDateTableColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        fileTableView.setItems(list);

        Platform.runLater(() -> totalSizeLabel.setText(clientExecutorService.getFolderSize()));
        Platform.runLater(() -> totalFilesLabel.setText(clientExecutorService.getFolderCount()));

    }

    @FXML
    private void rowChanged() {
        int row;
        row = fileTableView.getFocusModel().getFocusedCell().getRow();
        selectedFile = fileTableView.getItems().get(row);
        enableButtons();
    }

    @FXML
    private void downloadFile() {
        clientExecutorService.downloadFile(selectedFile.getFileName());
    }

    @FXML
    private void renameFile() {
        int row;
        row = fileTableView.getFocusModel().getFocusedCell().getRow();
        String fileName = fileTableView.getItems().get(row).getFileName();
        clientExecutorService.renameFile(fileName);
    }

    @FXML
    private void deleteFile() {
        int row;
        row = fileTableView.getFocusModel().getFocusedCell().getRow();
        selectedFile = fileTableView.getItems().get(row);
        clientExecutorService.deleteFile(selectedFile.getFileName());
    }

    @FXML
    private void uploadFile(ActionEvent event) {
        Window window = stage.getOwner();
        fileChooser.setTitle("Выберите файл для загрузки");

        File file = fileChooser.showOpenDialog(window);

        try {
            if (file != null) {
                clientExecutorService.sendClientFile(Paths.get(file.getPath()));
            }
        } catch (IOException e) {
            logger.error("File sending failure - " + e.getMessage());
            Platform.runLater(() -> new MessageBox(mainScene, FILE_SEND_ERROR).showError());
        }
    }

    public void renameFailed() {
        Platform.runLater(() -> new MessageBox(mainScene, FILE_RENAME_ERROR).showError());
    }

    public void fileDownloaded() {
        Platform.runLater(() -> new MessageBox(mainScene, FILE_DOWNLOAD_INFO).showInformation());
    }

    public void deleteFailed() {
        Platform.runLater(() -> new MessageBox(mainScene, FILE_DELETE_ERROR).showInformation());
    }

    public void disableButtons() {
        renameButton.setDisable(true);
        deleteButton.setDisable(true);
        downloadButton.setDisable(true);
    }

    public void enableButtons() {
        renameButton.setDisable(false);
        deleteButton.setDisable(false);
        downloadButton.setDisable(false);
    }
}
