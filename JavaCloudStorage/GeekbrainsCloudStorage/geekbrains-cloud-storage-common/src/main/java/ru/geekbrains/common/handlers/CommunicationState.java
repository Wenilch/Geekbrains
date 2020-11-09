package ru.geekbrains.common.handlers;

public enum CommunicationState {
    IDLE,
    NAME_LENGTH, NAME,
    FILE_LENGTH, FILE,
    DOWNLOAD_LENGTH, DOWNLOAD_NAME,
    LOGIN_LENGTH, LOGIN,
    PASSWORD_LENGTH, PASSWORD,
    OLD_FILE_NAME_LENGTH, OLD_FILE_NAME,
    NEW_FILE_NAME_LENGTH, NEW_FILE_NAME,
    DELETE_FILE_NAME_LENGTH, DELETE_FILE_NAME
}
