package ru.geekbrains.server.services;

import ru.geekbrains.common.configuration.CommonConfigurations;
import ru.geekbrains.common.exceptions.UserAuthenticationException;
import ru.geekbrains.common.exceptions.UserDirectoryCreateException;
import ru.geekbrains.common.services.AuthenticationService;
import ru.geekbrains.common.services.CommandExecutorService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class CommandExecutorServiceImpl implements CommandExecutorService {

    private final AuthenticationService authenticationService;
    private String login = null;
    private String userDirectoryPath = null;

    private final SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss");

    public CommandExecutorServiceImpl(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public String getUserFolder(String login, String password) throws UserAuthenticationException, UserDirectoryCreateException {
        if (authenticationService.authentication(login, password)) {
            String path = CommonConfigurations.SERVER_STORAGE_DIRECTORY + "\\" + login + "\\";
            File folder = new File(path);
            if (!folder.exists()) {
                if (!folder.mkdir()) {
                    throw new UserDirectoryCreateException("Не удалось создать папку для пользователя");
                }
            }

            this.login = login;
            this.userDirectoryPath = path;
            return path;
        }

        throw new UserAuthenticationException("Ошибка авторизации пользователя");
    }

    @Override
    public File getFolderContent(String userFolder) throws IOException {
        if (login == null) {
            return null;
        }

        String fileSize;

        File dir = new File(userFolder);
        File tmp = new File(login + ".tmp");

        if (!dir.isDirectory() || Objects.requireNonNull(dir.listFiles()).length == 0) {
            return null;
        }

        try (Writer fileWriter = new FileWriter(tmp)) {
            for (File item : Objects.requireNonNull(dir.listFiles())) {
                if (item.isFile()) {
                    if (item.length() / (1024 * 1024) >= 1L) {
                        fileSize = item.length() / (1024 * 1024) + " Mb";
                    } else {
                        fileSize = item.length() / (1024) + " Kb";
                    }

                    String strFile = item.getName() + CommonConfigurations.MESSAGE_DATA_DELIMITER + fileSize + CommonConfigurations.MESSAGE_DATA_DELIMITER
                            + formatDate.format(item.lastModified()) + CommonConfigurations.MESSAGE_DATA_DELIMITER + item.length() / (1024);

                    fileWriter.write(strFile + System.getProperty("line.separator"));
                }
            }
        }
        return tmp;
    }

    @Override
    public boolean renameFile(String oldFilename, String newFilename) {
        if (login == null) {
            return false;
        }

        File oldFile = Paths.get(userDirectoryPath + "\\" + oldFilename).toFile();
        File newFile = Paths.get(userDirectoryPath + "\\" + newFilename).toFile();
        return oldFile.renameTo(newFile);
    }

    @Override
    public boolean deleteFile(String filename) {
        if (login == null) {
            return false;
        }

        return Paths.get(userDirectoryPath + "/" + filename).toFile().delete();
    }
}
