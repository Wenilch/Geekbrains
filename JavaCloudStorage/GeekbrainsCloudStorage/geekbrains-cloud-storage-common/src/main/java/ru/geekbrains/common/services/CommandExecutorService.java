package ru.geekbrains.common.services;

import ru.geekbrains.common.exceptions.UserAuthenticationException;
import ru.geekbrains.common.exceptions.UserDirectoryCreateException;

import java.io.File;
import java.io.IOException;

public interface CommandExecutorService {
    String getUserFolder(String login, String password) throws UserAuthenticationException, UserDirectoryCreateException;

    File getFolderContent(String userFolder) throws IOException;

    boolean renameFile(String oldFilename, String newFilename);

    boolean deleteFile(String filename);
}
