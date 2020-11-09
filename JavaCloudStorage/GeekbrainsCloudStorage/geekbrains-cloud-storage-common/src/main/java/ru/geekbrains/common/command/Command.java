package ru.geekbrains.common.command;

public enum Command {
    // USER_AUTHENTICATION
    U_A,
    // USER_AUTHENTICATION_SUCCESSFUL
    UAS,
    //USER_AUTHENTICATION_FAILED
    UAF,
    //FILE_TRANSFER
    F_T,
    // FILE_DOWNLOAD
    FDD,
    //FILE_DOWNLOAD_SUCCESSFUL
    FDS,
    //FILE_RENAME
    F_R,
    // FILE_RENAME_ERROR
    FRE,
    //FILE_DELETE
    FDL,
    // FILE_DELETE_ERROR
    FDE,
    //DIRECTORY_CREATE_ERROR
    DCE,
    //CLOUD_STORAGE_EMPTY
    CSE
}
