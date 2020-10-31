package ru.geekbrains.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.geekbrains.client.data.UserFile;
import ru.geekbrains.client.gui.LoginController;
import ru.geekbrains.client.gui.MainController;
import ru.geekbrains.client.gui.MessageBox;
import ru.geekbrains.common.command.Command;
import ru.geekbrains.common.configuration.CommonConfigurations;
import ru.geekbrains.common.services.FileTransferService;
import ru.geekbrains.common.services.FileTransferServiceImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

public class ClientExecutorService {
    private static String userLogin = "";
    private static String userFolderSize;
    private static int userFolderCount;
    private static Scene mainScene;

    private final Logger logger = LogManager.getLogger(ClientExecutorService.class);
    private final FileTransferService fileTransferService = new FileTransferServiceImpl();

    public String getFolderSize() {
        return userFolderSize;
    }

    public String getFolderCount() {
        return Integer.toString(userFolderCount);
    }

    public String getLogin() {
        return userLogin;
    }

    public void setScene(Scene scene) {
        mainScene = scene;
    }

    public void connect(LoginController loginController, MainController mainController) {
        CountDownLatch networkStarter = new CountDownLatch(1);

        Thread t = new Thread(() -> {
            try {
                NettyNetwork.getInstance().start(networkStarter, loginController, mainController, mainScene, this);
            } catch (Exception exception) {
                logger.error(exception);
                networkStarter.countDown();
                loginController.notAvailableServer();
            }
        });
        t.start();
        try {
            networkStarter.await();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            logger.error(exception);
        }
    }

    public void authClient(String login, String pass) {
        userLogin = login;
        ByteBuf buf;

        byte[] msgBytes = Command.U_A.toString().getBytes(StandardCharsets.UTF_8);
        buf = ByteBufAllocator.DEFAULT.directBuffer(msgBytes.length);
        buf.writeBytes(msgBytes);
        send(buf);

        sendString(login);
        sendString(pass);
    }

    public void sendClientFile(Path filePath) throws IOException {
        fileTransferService.transfer(filePath, NettyNetwork.getInstance().getCurrentChannel(), null, channelFutureListener -> {
            if (!channelFutureListener.isSuccess()) {
                channelFutureListener.cause().printStackTrace();
                logger.error(channelFutureListener.cause());
                Platform.runLater(() -> new MessageBox(mainScene, "Невозможно отправить файл!").showError());
            }

            if (channelFutureListener.isSuccess()) {
                logger.info("Файл успешно отправлен");
            }
        });
    }

    public void downloadFile(String fileName) {
        ByteBuf buf;
        byte[] msgBytes = Command.FDL.toString().getBytes(StandardCharsets.UTF_8);
        buf = ByteBufAllocator.DEFAULT.directBuffer(msgBytes.length);
        buf.writeBytes(msgBytes);
        send(buf);
        sendString(fileName);
    }

    public void renameFile(String fileName) {
        TextInputDialog dialog = new TextInputDialog(fileName);

        dialog.setTitle("cloud.drive");
        dialog.setHeaderText("Переименование файла");
        dialog.setContentText("Новое имя файла:");
        dialog.initOwner(mainScene.getWindow());

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            byte[] msgBytes = Command.F_R.toString().getBytes(StandardCharsets.UTF_8);
            ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(msgBytes.length);
            buffer.writeBytes(msgBytes);
            send(buffer);
            sendString(fileName);
            String newFileName = result.get();
            sendString(newFileName);
        }
    }

    public void deleteFile(String fileName) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("cloud.drive");
        alert.setHeaderText("Удаление файла");
        alert.setContentText("Вы уверены, что хотите удалить файл " + fileName);

        alert.initOwner(mainScene.getWindow());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            byte[] msgBytes = Command.FDL.toString().getBytes(StandardCharsets.UTF_8);
            ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(msgBytes.length);
            buffer.writeBytes(msgBytes);
            send(buffer);

            sendString(fileName);
        }
    }


    public ObservableList<UserFile> prepareFileList() throws IOException {
        userFolderCount = 0;
        userFolderSize = "";
        ObservableList<UserFile> files = FXCollections.observableArrayList();

        File file = Paths.get(CommonConfigurations.CLIENT_STORAGE_DIRECTORY + "/" + userLogin + ".tmp").toFile();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line == null || file.length() == 0) {
                return null;
            }

            String[] tmp;
            long overallSize = 0L;
            while (line != null) {
                tmp = line.split(CommonConfigurations.MESSAGE_DATA_DELIMITER);
                files.add(new UserFile(tmp[0], tmp[1], tmp[2]));
                line = reader.readLine();
                overallSize += Long.parseLong(tmp[3]);
                userFolderCount++;
            }

            if (overallSize / (1024) >= 1L) {
                userFolderSize = overallSize / (1024) + " Mb";
            } else {
                userFolderSize = overallSize + " Kb";
            }

            Files.delete(file.toPath());
        }

        return files;
    }

    private void sendString(String value) {
        byte[] msgBytes = value.getBytes(StandardCharsets.UTF_8);
        ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(Integer.SIZE);
        buffer.writeInt(msgBytes.length);
        send(buffer);

        buffer = ByteBufAllocator.DEFAULT.directBuffer(msgBytes.length);
        buffer.writeBytes(msgBytes);
        send(buffer);
    }

    private void send(ByteBuf buffer) {
        Channel channel = NettyNetwork.getInstance().getCurrentChannel();
        channel.writeAndFlush(buffer);
    }
}
