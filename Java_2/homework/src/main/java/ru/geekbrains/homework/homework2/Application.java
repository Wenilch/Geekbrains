package ru.geekbrains.homework.homework2;

import ru.geekbrains.homework.homework2.exceptions.MyArrayDataException;
import ru.geekbrains.homework.homework2.exceptions.MyArraySizeException;

import java.util.Arrays;

import static java.lang.System.out;

public class Application {

    private static final String TEST_DATA_VALID = "10 3 1 2\n2 3 2 2\n5 6 7 1\n300 3 1 0";
    private static final String TEST_DATA_INCORRECT_DATA = "10 3 null 2\n2 3 2 2\n5 6 7 1\n300 3 1 0";
    private static final String TEST_DATA_INCORRECT_ARRAY_SIZE = "10 3 1 2\n2 3 2 2\n5 6 7 1\n300 3";

    private static final int ARRAY_SIZE = 4;

    public static void main(String[] args) {
        summarizeArrayElements(TEST_DATA_VALID);
        summarizeArrayElements(TEST_DATA_INCORRECT_DATA);
        summarizeArrayElements(TEST_DATA_INCORRECT_ARRAY_SIZE);
    }

    private static void summarizeArrayElements(String data) {
        String[][] dataArray = convertStringToArray(data);

        if (dataArray != null) {

            try {
                Integer sum = sumElementsArray(dataArray);
                if (sum != null) {
                    out.println("Sum of array elements : " + sum);
                }
            } catch (MyArraySizeException | MyArrayDataException e) {
                out.println(e.getMessage());
                out.println(Arrays.toString(e.getStackTrace()));
            }
        }
    }

    private static String[][] convertStringToArray(String data) {
        if (data == null || data.isEmpty())
            return null;

        String[] rows = data.split("\n");
        String[][] result = new String[rows.length][];

        for (int i = 0; i < rows.length; i++) {
            String[] splitRow = rows[i].split(" ");
            result[i] = new String[splitRow.length];

            for (int j = 0; j < splitRow.length; j++) {
                result[i][j] = splitRow[j];
            }
        }

        return result;
    }

    private static int sumElementsArray(String[][] dataArray) throws MyArraySizeException, MyArrayDataException {
        if (dataArray.length != ARRAY_SIZE) {
            throw new MyArraySizeException();
        }

        Integer sum = null;
        for (int i = 0; i < dataArray.length; i++) {

            if (dataArray[i].length != ARRAY_SIZE) {
                throw new MyArraySizeException();
            }

            for (int j = 0; j < dataArray[i].length; j++) {

                try {
                    int value = Integer.parseInt(dataArray[i][j]);

                    if (sum == null) {
                        sum = 0;
                    }

                    sum += value;
                } catch (NumberFormatException e) {
                    throw new MyArrayDataException(i, j);
                }
            }
        }

        if (sum != null) {
            sum /= 2;
        }

        return sum;
    }
}
