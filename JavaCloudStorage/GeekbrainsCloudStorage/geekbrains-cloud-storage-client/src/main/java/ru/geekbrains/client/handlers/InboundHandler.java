package ru.geekbrains.client.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javafx.application.Platform;
import javafx.scene.Scene;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.geekbrains.client.ClientExecutorService;
import ru.geekbrains.client.gui.LoginController;
import ru.geekbrains.client.gui.MainController;
import ru.geekbrains.client.gui.MessageBox;
import ru.geekbrains.common.command.Command;
import ru.geekbrains.common.configuration.CommonConfigurations;
import ru.geekbrains.common.handlers.CommunicationState;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class InboundHandler extends ChannelInboundHandlerAdapter {
    private CommunicationState currentState = CommunicationState.IDLE;
    private int fileNameLength;
    private long fileLength;
    private long inFileLength;
    private BufferedOutputStream fileSaveStream;
    private final byte[] msgBytes = new byte[CommonConfigurations.MESSAGE_TYPE_LENGTH];
    private String commandString = "";
    private String fileName;

    private final LoginController loginController;
    private final MainController mainController;
    private final Scene mainScene;
    private final ClientExecutorService clientExecutorService;

    private static final Logger logger = LogManager.getLogger(InboundHandler.class);

    public InboundHandler(LoginController loginController, MainController mainController, Scene mainScene, ClientExecutorService clientExecutorService) {
        this.loginController = loginController;
        this.mainController = mainController;
        this.mainScene = mainScene;
        this.clientExecutorService = clientExecutorService;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
        ByteBuf buffer = ((ByteBuf) msg);

        while (buffer.readableBytes() > 0) {

            if (currentState == CommunicationState.IDLE) {
                buffer.readBytes(msgBytes);
                commandString = new String(msgBytes, StandardCharsets.UTF_8);
            }

            if (currentState == CommunicationState.IDLE) {

                if (commandString.equals(Command.CSE.toString())) {
                    mainController.fillFileTable(null);
                    logger.info("Empty user folder received");
                }

                if (commandString.equals(Command.UAS.toString())) {
                    logger.info("Login successful");
                    loginController.loginSuccessful();
                }

                if (commandString.equals(Command.UAF.toString())) {
                    logger.info("Login failed");
                    loginController.loginFailed("Неверный логин или пароль!");
                }

                if (commandString.equals(Command.DCE.toString())) {
                    logger.info("Error sever folder creation");
                    loginController.loginFailed("Ошибка создания раздела на сервере");
                }

                if (commandString.equals(Command.FDS.toString())) {
                    mainController.fileDownloaded();
                }

                if (commandString.equals(Command.FRE.toString())) {
                    logger.info("Error file renaming");
                    mainController.renameFailed();
                }

                if (commandString.equals(Command.FDE.toString())) {
                    logger.info("Error file deleting");
                    mainController.deleteFailed();
                }

                if (commandString.equals(Command.F_T.toString())) {
                    currentState = CommunicationState.NAME_LENGTH;
                    inFileLength = 0L;
                }
            }

            if (currentState == CommunicationState.NAME_LENGTH && buffer.readableBytes() >= Integer.BYTES) {
                fileNameLength = buffer.readInt();
                currentState = CommunicationState.NAME;
            }

            if (currentState == CommunicationState.NAME && buffer.readableBytes() >= fileNameLength) {
                byte[] fileByte = new byte[fileNameLength];
                buffer.readBytes(fileByte);

                fileName = new String(fileByte, StandardCharsets.UTF_8);
                fileSaveStream = new BufferedOutputStream(new FileOutputStream(CommonConfigurations.CLIENT_STORAGE_DIRECTORY + "/" + fileName));
                currentState = CommunicationState.FILE_LENGTH;
            }

            if (currentState == CommunicationState.FILE_LENGTH && buffer.readableBytes() >= Long.SIZE) {
                fileLength = buffer.readLong();
                currentState = CommunicationState.FILE;
            }

            if (currentState == CommunicationState.FILE) {
                while (buffer.readableBytes() > 0) {
                    fileSaveStream.write(buffer.readByte());
                    inFileLength++;
                    if (fileLength == inFileLength) {
                        currentState = CommunicationState.IDLE;

                        fileSaveStream.close();
                        if (fileName.equals(clientExecutorService.getLogin() + ".tmp")) {
                            mainController.fillFileTable(clientExecutorService.prepareFileList());
                        }

                        break;
                    }
                }
            }
        }

        if (buffer.readableBytes() == 0) {
            buffer.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
        logger.error(cause.getMessage());
        Platform.runLater(() -> new MessageBox(mainScene, cause.getMessage()).showError());
    }
}
