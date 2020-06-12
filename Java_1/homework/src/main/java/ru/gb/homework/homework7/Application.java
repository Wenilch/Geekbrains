package ru.gb.homework.homework7;

import ru.gb.homework.homework7.storage.FileStorageService;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;


public class Application {

    public static void main(String[] args) {
        FileStorageService fileStorageService = new FileStorageService();

        if (!fileStorageService.deleteDirectory(WORK_DIRECTORY)) {
            System.out.println("Не удалось удалить созданную ранее дирректорию");
        }

        String firstFilePath = Paths.get(WORK_DIRECTORY, FIRST_FILE_NAME).toString();
        String secondFilePath = Paths.get(WORK_DIRECTORY, SECOND_FILE_NAME).toString();
        String thirdFilePath = Paths.get(WORK_DIRECTORY, THIRD_FILE_NAME).toString();

        if (fileStorageService.createDirectory(WORK_DIRECTORY)) {
            boolean isCreateFirstFile = fileStorageService.createFile(Paths.get(WORK_DIRECTORY, FIRST_FILE_NAME).toString(), LOREM_IPSUM_FIRST);
            boolean isCreateSecondFile = fileStorageService.createFile(Paths.get(WORK_DIRECTORY, SECOND_FILE_NAME).toString(), LOREM_IPSUM_SECOND);

            if (isCreateFirstFile && isCreateSecondFile) {
                System.out.println("Задание 1 Выполнено");
            } else {
                System.out.println("Выполнить задание 1 не удалось.");
            }

            boolean isCombineFilesContent = fileStorageService.combineFilesContent(new String[]{firstFilePath, secondFilePath}, thirdFilePath);
            if (isCombineFilesContent) {
                System.out.println("Задание 2 Выполнено");
            } else {
                System.out.println("Выполнить задание 2 не удалось.");
            }

            System.out.println("Введите любое слово для выполнения задания 3 ");
            Scanner scanner = new Scanner(System.in);
            String inputWord = scanner.nextLine();

            boolean isWordContainInFirstFile = fileStorageService.isWordContainInFile(inputWord, firstFilePath);
            printWordInFileContainMessage(isWordContainInFirstFile, FIRST_FILE_NAME);

            boolean isWordContainInSecondFile = fileStorageService.isWordContainInFile(inputWord, secondFilePath);
            printWordInFileContainMessage(isWordContainInSecondFile, SECOND_FILE_NAME);

            boolean isWordContainInThirdFile = fileStorageService.isWordContainInFile(inputWord, thirdFilePath);
            printWordInFileContainMessage(isWordContainInThirdFile, THIRD_FILE_NAME);

            System.out.println("Введите любое слово для выполнения задания 4 ");
            inputWord = scanner.nextLine();
            String[] files = fileStorageService.getCollectionFilesThatContainSpecifiedWord(inputWord, WORK_DIRECTORY);

            if (files != null && files.length > 0) {
                System.out.println("Список файлов содержащих указанное слово:");
                System.out.println(Arrays.toString(files));
            } else {
                System.out.println("В директории нет файлов содержащих указанное слово.");
            }

            System.out.println("Введите любое слово для выполнения задания 5 ");
            inputWord = scanner.nextLine();
            if (fileStorageService.appendContentToFilesInDirectory(inputWord, WORK_DIRECTORY)) {
                files = fileStorageService.getCollectionFilesThatContainSpecifiedWord(inputWord, WORK_DIRECTORY);

                if (files != null && files.length > 0) {
                    System.out.println("Список файлов содержащих указанное слово:");
                    System.out.println(Arrays.toString(files));
                } else {
                    System.out.println("В директории нет файлов содержащих указанное слово.");
                }
            } else {
                System.out.println("Не удалось добавить указанное слово во все файлы дирректории.");
            }

            scanner.close();

        } else {
            System.out.println("Не удалось создать дирректорию домашнего задания №7.");
        }
    }

    private static final String LOREM_IPSUM_FIRST = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

    private static final String LOREM_IPSUM_SECOND = "Sed ut perspiciatis, unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam eaque ipsa, quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas sit, aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos, qui ratione voluptatem sequi nesciunt, neque porro quisquam est, qui dolorem ipsum, quia dolor sit, amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt, ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit, qui in ea voluptate velit esse, quam nihil molestiae consequatur, vel illum, qui dolorem eum fugiat, quo voluptas nulla pariatur? At vero eos et accusamus et iusto odio dignissimos ducimus, qui blanditiis praesentium voluptatum deleniti atque corrupti, quos dolores et quas molestias excepturi sint, obcaecati cupiditate non provident, similique sunt in culpa, qui officia deserunt mollitia animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio, cumque nihil impedit, quo minus id, quod maxime placeat, facere possimus, omnis voluptas assumenda est, omnis dolor repellendus. Temporibus autem quibusdam et aut officiis debitis aut rerum necessitatibus saepe eveniet, ut et voluptates repudiandae sint et molestiae non recusandae. Itaque earum rerum hic tenetur a sapiente delectus, ut aut reiciendis voluptatibus maiores alias consequatur aut perferendis doloribus asperiores repellat. ";

    private static final String WORK_DIRECTORY = "homework";

    private static final String FIRST_FILE_NAME = "FirstFile.txt";

    private static final String SECOND_FILE_NAME = "SecondFile.txt";

    private static final String THIRD_FILE_NAME = "ThirdFile.txt";

    private static void printWordInFileContainMessage(boolean isContain, String fileName) {
        if (isContain) {
            System.out.println("Введенное слово содержится в файле " + fileName);
        } else {
            System.out.println("Введенное слово не содержится в файле " + fileName);
        }
    }
}
