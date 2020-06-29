package ru.geekbrains.homework.homework1;

import ru.geekbrains.homework.homework1.obstacles.Obstacle;
import ru.geekbrains.homework.homework1.obstacles.Treadmill;
import ru.geekbrains.homework.homework1.obstacles.Wall;
import ru.geekbrains.homework.homework1.subjects.Activity;
import ru.geekbrains.homework.homework1.subjects.Cat;
import ru.geekbrains.homework.homework1.subjects.Human;
import ru.geekbrains.homework.homework1.subjects.Robot;

public class Application {
    public static void main(String[] args) {

        /**
         * 1. Создайте три класса Человек, Кот, Робот, которые не наследуются от одного класса. Эти классы должны уметь бегать и прыгать (методы просто выводят информацию о действии в консоль).
         */
        System.out.println(SEPARATOR);
        System.out.println("1. Создайте три класса Человек, Кот, Робот, которые не наследуются от одного класса. Эти классы должны уметь бегать и прыгать (методы просто выводят информацию о действии в консоль).");
        Human human = new Human("Вова", 10000, 5);
        Cat cat = new Cat("Борис", 1000, 10);
        Robot robot = new Robot("Петр", 100000, 5);

        printRunMessage(human, human.run(100));
        printRunMessage(cat, cat.run(100));
        printRunMessage(robot, robot.run(100));
        System.out.println(SEPARATOR);

        printJumpMessage(human, human.jump(2));
        printJumpMessage(cat, cat.jump(2));
        printJumpMessage(robot, robot.jump(2));
        System.out.println(SEPARATOR);

        /**
         * 2. Создайте два класса: беговая дорожка и стена, при прохождении через которые,
         * участники должны выполнять соответствующие действия (бежать или прыгать), результат выполнения печатаем в консоль (успешно пробежал, не смог пробежать и т.д.).
         */

        System.out.println(SEPARATOR);
        System.out.println("2. Создайте два класса: беговая дорожка и стена, при прохождении через которые, участники должны выполнять соответствующие действия (бежать или прыгать), результат выполнения печатаем в консоль (успешно пробежал, не смог пробежать и т.д.).");

        Treadmill treadmill = new Treadmill(500);
        Wall wall = new Wall(10);

        printRunMessage(human, treadmill.pass(human));
        printRunMessage(cat, treadmill.pass(cat));
        printRunMessage(robot, treadmill.pass(robot));
        System.out.println(SEPARATOR);

        printJumpMessage(human, wall.pass(human));
        printJumpMessage(cat, wall.pass(cat));
        printJumpMessage(robot, wall.pass(robot));
        System.out.println(SEPARATOR);

        /**
         * 3. Создайте два массива: с участниками и препятствиями, и заставьте всех участников пройти этот набор препятствий.
         */

        System.out.println(SEPARATOR);
        System.out.println("3. Создайте два массива: с участниками и препятствиями, и заставьте всех участников пройти этот набор препятствий.");

        Activity[] activities = {new Cat("Jack", 100, 10), new Human("John", 1000, 5), new Robot("Bill", 100000, 1)};
        Obstacle[] obstacles = {new Treadmill(1000), new Wall(7)};

        for (int i = 0; i < obstacles.length; i++) {
            for (int j = 0; j < activities.length; j++) {
                if (obstacles[i] instanceof Treadmill) {
                    printRunMessage(activities[j], obstacles[i].pass(activities[j]));
                } else {
                    printJumpMessage(activities[j], obstacles[i].pass(activities[j]));
                }
            }
        }

        System.out.println(SEPARATOR);

        /**
         * 4. * У препятствий есть длина (для дорожки) или высота (для стены), а участников ограничения на бег и прыжки. Если участник не смог пройти одно из препятствий, то дальше по списку он препятствий не идет.
         */

        System.out.println(SEPARATOR);
        System.out.println("4. * У препятствий есть длина (для дорожки) или высота (для стены), а участников ограничения на бег и прыжки. Если участник не смог пройти одно из препятствий, то дальше по списку он препятствий не идет.");

        for (int i = 0; i < activities.length; i++) {
            for (int j = 0; j < obstacles.length; j++) {

                if (!obstacles[j].pass(activities[i])) {
                    break;
                }

                if (obstacles[j] instanceof Treadmill) {
                    printRunMessage(activities[i], true);
                } else {
                    printJumpMessage(activities[i], true);
                }

            }
        }

        System.out.println(SEPARATOR);

    }

    private static final String SEPARATOR = "============================================================================";

    private static void printRunMessage(Object runner, boolean isRun) {
        if (runner instanceof Human) {
            printRunMessage((Human) runner, isRun);
        } else if (runner instanceof Cat) {
            printRunMessage((Cat) runner, isRun);
        } else if (runner instanceof Robot) {
            printRunMessage((Robot) runner, isRun);
        }
    }

    private static String runCompleteFormat = "%s %s пробежал дистанцию.";
    private static String runFailFormat = "%s %s не пробежал дистанцию.";

    private static void printRunMessage(Human runner, boolean isRun) {
        printRunMessage(runner.getType(), runner.getName(), isRun);
    }

    private static void printRunMessage(Cat runner, boolean isRun) {
        printRunMessage(runner.getType(), runner.getName(), isRun);
    }

    private static void printRunMessage(Robot runner, boolean isRun) {
        printRunMessage(runner.getType(), runner.getName(), isRun);
    }

    private static void printRunMessage(String type, String name, boolean isJump) {
        String message = isJump ? String.format(runCompleteFormat, type, name) : String.format(runFailFormat, type, name);
        System.out.println(message);
    }

    private static void printJumpMessage(Object jumper, boolean isJump) {
        if (jumper instanceof Human) {
            printJumpMessage((Human) jumper, isJump);
        } else if (jumper instanceof Cat) {
            printJumpMessage((Cat) jumper, isJump);
        } else if (jumper instanceof Robot) {
            printJumpMessage((Robot) jumper, isJump);
        }
    }

    private static String jumpCompleteFormat = "%s %s запрыгнул на препятствие.";
    private static String jumpFailFormat = "%s %s не запрыгнул на препятствие.";

    private static void printJumpMessage(Human jumper, boolean isJump) {
        printJumpMessage(jumper.getType(), jumper.getName(), isJump);
    }

    private static void printJumpMessage(Cat jumper, boolean isJump) {
        printJumpMessage(jumper.getType(), jumper.getName(), isJump);
    }

    private static void printJumpMessage(Robot jumper, boolean isJump) {
        printJumpMessage(jumper.getType(), jumper.getName(), isJump);
    }

    private static void printJumpMessage(String type, String name, boolean isJump) {
        String message = isJump ? String.format(jumpCompleteFormat, type, name) : String.format(jumpFailFormat, type, name);
        System.out.println(message);
    }
}
