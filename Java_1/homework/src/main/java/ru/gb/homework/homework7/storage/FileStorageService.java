package ru.gb.homework.homework7.storage;

import java.io.*;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class FileStorageService {
    public boolean createDirectory(String directoryPath) {
        if (directoryPath == null || directoryPath.isEmpty())
            return false;

        File directory = new File(directoryPath);
        if (!directory.exists()) {
            try {
                return new File(directoryPath).mkdirs();
            } catch (SecurityException securityException) {
                securityException.printStackTrace();
            }
        } else {
            return true;
        }

        return false;
    }

    public boolean deleteDirectory(String directoryPath) {
        if (directoryPath == null || directoryPath.isEmpty())
            return false;

        File directory = new File(directoryPath);
        if (directory.exists()) {
            if (directory.listFiles() != null) {
                for (File file : directory.listFiles()) {
                    file.delete();
                }
            }

            try {
                return directory.delete();
            } catch (SecurityException securityException) {
                securityException.printStackTrace();
            }
        }

        return false;
    }

    public boolean createFile(String filePath, String content) {
        if (filePath == null || filePath.isEmpty())
            return false;

        return appendContentToFile(filePath, content);
    }

    private boolean appendContentToFile(String filePath, String content) {
        try {
            PrintStream printStream = new PrintStream(new FileOutputStream(filePath, true));
            if (content != null && !content.isEmpty()) {
                printStream.println(content);
            }
            printStream.close();

            return true;
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return false;
    }

    public boolean combineFilesContent(String[] filePaths, String resultFilePath) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String filePath : filePaths) {
            String fileContent = readFileContent(filePath);

            if (fileContent != null && !fileContent.isEmpty()) {
                stringBuilder.append(fileContent);
                stringBuilder.append(System.getProperty("line.separator"));
            }
        }

        return createFile(resultFilePath, stringBuilder.toString());
    }

    private String readFileContent(String filePath) {
        if (filePath == null || filePath.isEmpty())
            return null;

        StringBuilder stringBuilder = new StringBuilder();
        try {
            Scanner scanner = new Scanner(new FileInputStream(filePath));
            while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    public boolean isWordContainInFile(String inputWord, String filePath) {
        String fileContent = readFileContent(filePath);
        if (fileContent != null && !fileContent.isEmpty()) {
            return fileContent.contains(inputWord);
        }

        return false;
    }

    public String[] getCollectionFilesThatContainSpecifiedWord(String inputWord, String directoryPath) {
        if (directoryPath == null || directoryPath.isEmpty())
            return null;

        String[] files = new String[0];

        File directory = new File(directoryPath);
        if (directory.exists()) {
            String[] fileNames = directory.list();
            if (fileNames != null) {
                for (String fileName : fileNames) {
                    if (isWordContainInFile(inputWord, Paths.get(directoryPath, fileName).toString())) {
                        files = Arrays.copyOf(files, files.length + 1);
                        files[files.length - 1] = fileName;
                    }
                }
            }

            return files;
        }

        return null;
    }

    public boolean appendContentToFilesInDirectory(String inputWord, String directoryPath) {
        boolean result = false;

        if (directoryPath == null || directoryPath.isEmpty())
            return result;

        File directory = new File(directoryPath);
        if (directory.exists()) {
            String[] fileNames = directory.list();
            if (fileNames != null) {
                for (String fileName : fileNames) {
                    result = appendContentToFile(Paths.get(directoryPath, fileName).toString(), inputWord);
                }
            }
        }

        return result;
    }
}


