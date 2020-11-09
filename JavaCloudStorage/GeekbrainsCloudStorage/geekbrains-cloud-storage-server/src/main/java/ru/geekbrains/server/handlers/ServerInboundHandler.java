package ru.geekbrains.server.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.geekbrains.common.command.Command;
import ru.geekbrains.common.configuration.CommonConfigurations;
import ru.geekbrains.common.exceptions.UserAuthenticationException;
import ru.geekbrains.common.exceptions.UserDirectoryCreateException;
import ru.geekbrains.common.handlers.CommunicationState;
import ru.geekbrains.common.services.CommandExecutorService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class ServerInboundHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LogManager.getLogger(ServerInboundHandler.class);

    private final CommandExecutorService commandExecutorService;

    public ServerInboundHandler(CommandExecutorService commandExecutorService) {
        this.commandExecutorService = commandExecutorService;
    }

    private CommunicationState currentState = CommunicationState.IDLE;
    private int fileNameLength;
    private long fileLength;
    private String fileName;
    private long inFileLength;

    private int loginLength;
    private int passLength;
    private String login;
    private String pass;
    private String userPath;

    private String oldFileName;
    private String newFileName;

    private BufferedOutputStream fileSaveStream;
    private final byte[] commandBytes = new byte[CommonConfigurations.MESSAGE_TYPE_LENGTH];
    private String commandString = "";

    private File file;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
        ByteBuf buffer = ((ByteBuf) msg);

        while (buffer.readableBytes() > 0) {

            if (currentState == CommunicationState.IDLE) {
                buffer.readBytes(commandBytes);
                commandString = new String(commandBytes, StandardCharsets.UTF_8);
            }

            if (currentState == CommunicationState.IDLE & commandString.equals(Command.U_A.toString())) {
                currentState = CommunicationState.LOGIN_LENGTH;
            }

            if (currentState == CommunicationState.LOGIN_LENGTH && buffer.readableBytes() >= Integer.BYTES) {
                loginLength = buffer.readInt();
                currentState = CommunicationState.LOGIN;
            }

            if (currentState == CommunicationState.LOGIN && buffer.readableBytes() >= loginLength) {
                byte[] loginBytes = new byte[loginLength];
                buffer.readBytes(loginBytes);
                login = new String(loginBytes, StandardCharsets.UTF_8);
                currentState = CommunicationState.PASSWORD_LENGTH;
            }

            if (currentState == CommunicationState.PASSWORD_LENGTH && buffer.readableBytes() >= Integer.BYTES) {
                passLength = buffer.readInt();
                currentState = CommunicationState.PASSWORD;
            }

            if (currentState == CommunicationState.PASSWORD) {
                authenticationUser(buffer, ctx);
            }


            fileReceive(buffer, ctx);

            fileSend(buffer, ctx);

            fileRename(buffer, ctx);

            fileDelete(buffer, ctx);


            if (buffer.readableBytes() == 0) {
                buffer.release();
            }
        }
    }

    private void authenticationUser(ByteBuf buffer, ChannelHandlerContext handlerContext) {
        if (buffer.readableBytes() >= passLength) {
            byte[] passBytes = new byte[passLength];
            buffer.readBytes(passBytes);
            pass = new String(passBytes, StandardCharsets.UTF_8);
            currentState = CommunicationState.IDLE;
        }

        try {
            userPath = commandExecutorService.getUserFolder(login, pass);
            currentState = CommunicationState.IDLE;
            logger.info("Пользователь успешно авторизован {0}", login);

            handlerContext.writeAndFlush(Command.UAS);
            file = commandExecutorService.getFolderContent(userPath);
            handlerContext.writeAndFlush(file);
        } catch (UserAuthenticationException exception) {
            logger.info(exception);
            handlerContext.writeAndFlush(Command.UAF);
            file = null;
        } catch (UserDirectoryCreateException exception) {
            logger.error(exception);
            handlerContext.writeAndFlush(Command.DCE);
            file = null;
        } catch (IOException exception) {
            logger.error(exception);
            handlerContext.writeAndFlush(Command.CSE);
            file = null;
        }
    }

    private void fileReceive(ByteBuf buffer, ChannelHandlerContext handlerContext) throws IOException {
        if (currentState == CommunicationState.IDLE && commandString.equals(Command.F_T.toString())) {
            currentState = CommunicationState.NAME_LENGTH;
            inFileLength = 0L;
        }

        if (currentState == CommunicationState.NAME_LENGTH && buffer.readableBytes() >= Integer.BYTES) {
            fileNameLength = buffer.readInt();
            currentState = CommunicationState.NAME;
        }

        if (currentState == CommunicationState.NAME && buffer.readableBytes() >= fileNameLength) {
            byte[] nameBytes = new byte[fileNameLength];
            buffer.readBytes(nameBytes);
            fileName = new String(nameBytes, StandardCharsets.UTF_8);
            fileSaveStream = new BufferedOutputStream(new FileOutputStream(userPath + fileName));
            currentState = CommunicationState.FILE_LENGTH;
        }

        if (currentState == CommunicationState.FILE_LENGTH && buffer.readableBytes() >= Long.BYTES) {
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

                    try {
                        file = commandExecutorService.getFolderContent(userPath);
                        handlerContext.writeAndFlush(file);
                    } catch (IOException exception) {
                        logger.error(exception);
                        handlerContext.writeAndFlush(Command.CSE);
                        file = null;
                    }
                    break;
                }
            }
        }
    }

    private void fileSend(ByteBuf buffer, ChannelHandlerContext handlerContext) {
        if (currentState == CommunicationState.IDLE & commandString.equals(Command.FDD.toString())) {
            currentState = CommunicationState.DOWNLOAD_LENGTH;
        }

        if (currentState == CommunicationState.DOWNLOAD_LENGTH && buffer.readableBytes() >= Integer.BYTES) {
            fileNameLength = buffer.readInt();
            currentState = CommunicationState.DOWNLOAD_NAME;
        }

        if (currentState == CommunicationState.DOWNLOAD_NAME) {
            if (buffer.readableBytes() >= fileNameLength) {
                byte[] nameBytes = new byte[fileNameLength];
                buffer.readBytes(nameBytes);
                fileName = new String(nameBytes, StandardCharsets.UTF_8);
                currentState = CommunicationState.IDLE;
            }

            handlerContext.writeAndFlush(Paths.get(userPath + fileName).toFile());

            try {
                file = commandExecutorService.getFolderContent(userPath);
                handlerContext.writeAndFlush(commandExecutorService.getFolderContent(userPath));
            } catch (IOException exception) {
                logger.error(exception);
                handlerContext.writeAndFlush(Command.CSE);
                file = null;
            }

        }
    }

    private void fileRename(ByteBuf buffer, ChannelHandlerContext handlerContext) {
        if (currentState == CommunicationState.IDLE & commandString.equals(Command.F_R.toString())) {
            currentState = CommunicationState.OLD_FILE_NAME_LENGTH;
        }

        if (currentState == CommunicationState.OLD_FILE_NAME_LENGTH && buffer.readableBytes() >= Integer.BYTES) {
            if (buffer.readableBytes() >= 4) {
                fileNameLength = buffer.readInt();
                currentState = CommunicationState.OLD_FILE_NAME;
            }
        }

        if (currentState == CommunicationState.OLD_FILE_NAME && buffer.readableBytes() >= fileNameLength) {
            byte[] nameBytes = new byte[fileNameLength];
            buffer.readBytes(nameBytes);
            oldFileName = new String(nameBytes, StandardCharsets.UTF_8);
            currentState = CommunicationState.NEW_FILE_NAME_LENGTH;
        }

        if (currentState == CommunicationState.NEW_FILE_NAME_LENGTH && buffer.readableBytes() >= Integer.BYTES) {
            fileNameLength = buffer.readInt();
            currentState = CommunicationState.NEW_FILE_NAME;
        }

        if (currentState == CommunicationState.NEW_FILE_NAME) {
            if (buffer.readableBytes() >= fileNameLength) {
                byte[] nameBytes = new byte[fileNameLength];
                buffer.readBytes(nameBytes);
                newFileName = new String(nameBytes, StandardCharsets.UTF_8);
                currentState = CommunicationState.IDLE;
            }

            if (commandExecutorService.renameFile(oldFileName, newFileName)) {
                try {
                    file = commandExecutorService.getFolderContent(userPath);
                    handlerContext.writeAndFlush(file);
                } catch (IOException exception) {
                    logger.error(exception);
                    handlerContext.writeAndFlush(Command.CSE);
                    file = null;
                }
            } else {
                logger.info("Не удалось переименовать файл " + oldFileName);
                handlerContext.writeAndFlush(Command.FRE);
            }
        }
    }

    private void fileDelete(ByteBuf buffer, ChannelHandlerContext handlerContext) {
        if (currentState == CommunicationState.IDLE & commandString.equals(Command.FDL.toString())) {
            currentState = CommunicationState.DELETE_FILE_NAME_LENGTH;
        }

        if (currentState == CommunicationState.DELETE_FILE_NAME_LENGTH && buffer.readableBytes() >= Integer.BYTES) {
            fileNameLength = buffer.readInt();
            currentState = CommunicationState.DELETE_FILE_NAME;
        }

        if (currentState == CommunicationState.DELETE_FILE_NAME) {
            if (buffer.readableBytes() >= fileNameLength) {
                byte[] nameBytes = new byte[fileNameLength];
                buffer.readBytes(nameBytes);
                fileName = new String(nameBytes, StandardCharsets.UTF_8);
                currentState = CommunicationState.IDLE;
            }

            if (commandExecutorService.deleteFile(fileName)) {
                try {
                    file = commandExecutorService.getFolderContent(userPath);
                    handlerContext.writeAndFlush(file);
                } catch (IOException exception) {
                    logger.error(exception);
                    handlerContext.writeAndFlush(Command.CSE);
                    file = null;
                }
            } else {
                logger.info("Не удалось удалить файл " + fileName);
                handlerContext.writeAndFlush(Command.FDE);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error(cause);
        ctx.close();
    }
}
