package ru.gb.homework1;

public class HomeWork {

    public static void main(String[] args) {

        byte b = 126;
        short s = 32766;
        int i = 2147483646;
        long l = 9223372036854775807L;
        float f = 15.5f;
        double d = -4664.654654d;
        char c = '!';
        String str = "Homework";
        boolean bool = false;

        System.out.println("Method \"calculateSomeEquation\" result " + calculateSomeEquation(15.5f, 22.2f, 1000, 1f));

        System.out.println("Сумма лежит в пределах от 10 до 20 (включительно) 10 20 " + checkingThatSumNumbersFallsWithinPermissibleLimits(10, 20));
        System.out.println("Сумма лежит в пределах от 10 до 20 (включительно) 10 10 " + checkingThatSumNumbersFallsWithinPermissibleLimits(10, 10));
        System.out.println("Сумма лежит в пределах от 10 до 20 (включительно) 10 11 " + checkingThatSumNumbersFallsWithinPermissibleLimits(10, 11));
        System.out.println("Сумма лежит в пределах от 10 до 20 (включительно) 5 5 " + checkingThatSumNumbersFallsWithinPermissibleLimits(5, 5));
        System.out.println("Сумма лежит в пределах от 10 до 20 (включительно) 0 10 " + checkingThatSumNumbersFallsWithinPermissibleLimits(0, 10));
        System.out.println("Сумма лежит в пределах от 10 до 20 (включительно) 0 0 " + checkingThatSumNumbersFallsWithinPermissibleLimits(0, 0));

        determinationSignNumber(5);
        determinationSignNumber(0);
        determinationSignNumber(-5);

        System.out.println("5 отрицательное число : " + isNegativeNumber(5));
        System.out.println("0 отрицательное число : " + isNegativeNumber(0));
        System.out.println("-5 отрицательное число : " + isNegativeNumber(-5));

        System.out.println("0 високосный год : " + isLeapYear(0));
        System.out.println("4 високосный год : " + isLeapYear(4));
        System.out.println("16 високосный год : " + isLeapYear(16));
        System.out.println("100 високосный год : " + isLeapYear(100));
        System.out.println("400 високосный год : " + isLeapYear(400));
        System.out.println("1000 високосный год : " + isLeapYear(1000));
        System.out.println("1600 високосный год : " + isLeapYear(1600));
        System.out.println("2000 високосный год : " + isLeapYear(2000));
        System.out.println("2020 високосный год : " + isLeapYear(2020));
        System.out.println("2100 високосный год : " + isLeapYear(2100));
        System.out.println("2400 високосный год : " + isLeapYear(2400));

        printHello("Пётр");
    }

    /**
     * 3. Метод вычисляющий выражение a * (b + (c / d)).
     */
    private static float calculateSomeEquation(float a, float b, float c, float d) {
        return a * (b + (c / d));
    }

    /**
     * 4. Метод, принимающий на вход два целых числа и проверяющий, что их сумма лежит в пределах от 10 до 20 (включительно).
     *
     */
    private static boolean checkingThatSumNumbersFallsWithinPermissibleLimits(int x, int y) {

        int sum = x + y;

        return sum >= 10 && sum <= 20;
    }


    /**
     * 5. Написать метод, которому в качестве параметра передается целое число, метод должен напечатать в консоль положительное ли число передали, или отрицательное.
     */
    private static void determinationSignNumber(int x){
        if(!isNegativeNumber(x)){
            System.out.println(x + " положительное число" );

            return;
        }

        System.out.println(x + " отрицательное число");
    }

    /**
     *  6. Написать метод, которому в качестве параметра передается целое число, метод должен вернуть true, если число отрицательное.
     */
    private static boolean isNegativeNumber(int x){
        return x < 0;
    }

    /**
     * 7. Написать метод, которому в качестве параметра передается строка, обозначающая имя, метод должен вывести в консоль сообщение «Привет, указанное_имя!».
     */
    private static void printHello(String name){
        System.out.println(String.format("Привет, %s!", name));
    }

    /**
     * 8. Написать метод, который определяет является ли год високосным, и выводит сообщение в консоль. Каждый 4-й год является високосным, кроме каждого 100-го, при этом каждый 400-й – високосный.
     */
    private static boolean isLeapYear(int year) {
        if (year % 4 == 0) {
            if (year % 100 == 0 && year % 400 == 0) {
                return true;
            } else if (year % 100 == 0) {
                return false;
            }

            return true;
        }

        return false;
    }
}
