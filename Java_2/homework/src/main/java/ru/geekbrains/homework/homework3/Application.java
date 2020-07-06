package ru.geekbrains.homework.homework3;

import java.util.Arrays;
import java.util.HashMap;

public class Application {
    public static void main(String[] args) {
        System.out.println("1. Создать массив с набором слов (20-30 слов, должны встречаться повторяющиеся)");
        String[] fruits = {"apple", "orange", "melon", "apple", "kiwi", "kiwi", "banana", "kiwi", "pear", "mango", "papaya", "apple", "orange", "melon", "apple", "kiwi", "kiwi", "banana", "kiwi", "pear", "mango", "papaya"};
        System.out.println(Arrays.toString(fruits));

        HashMap<String, Integer> map = new HashMap<>();
        for (String fruit : fruits) {
            map.put(fruit, map.getOrDefault(fruit, 0) + 1);
        }

        System.out.println("Найти список слов, из которых состоит текст (дубликаты не считать)");
        System.out.println(map.keySet());
        System.out.println("Посчитать сколько раз встречается каждое слово (использовать HashMap)");
        System.out.println(map);

        /**
         * Написать простой класс PhoneBook(внутри использовать HashMap):
         * В качестве ключа использовать фамилию
         * В каждой записи всего два поля: phone, e-mail
         * Отдельный метод для поиска номера телефона по фамилии (ввели фамилию, получили ArrayList телефонов), и отдельный метод для поиска e-mail по фамилии.
         * Следует учесть, что под одной фамилией может быть несколько записей. Итого должно получиться 3 класса Main, PhoneBook, Person.
         */
        PhoneBook phoneBook = new PhoneBook();

        phoneBook.add("Петров", "111", "email1");
        phoneBook.add("Петров", "222", "email2");
        phoneBook.add("Петров", "333", "email3");
        phoneBook.add("Петров", "444", "email4");
        phoneBook.add("Сидоров", "555", "email5");
        phoneBook.add("Сидоров", "666", "email6");

        System.out.println(phoneBook.findEmails("Семенов"));
        System.out.println(phoneBook.findEmails("Петров"));
        System.out.println(phoneBook.findEmails("Сидоров"));

        System.out.println(phoneBook.findPhoneNumbers("Семенов"));
        System.out.println(phoneBook.findPhoneNumbers("Петров"));
        System.out.println(phoneBook.findPhoneNumbers("Сидоров"));
    }
}
