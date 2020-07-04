package ru.geekbrains.homework.homework3;

import java.util.ArrayList;
import java.util.HashMap;

public class PhoneBook {
    private HashMap<String, ArrayList<Person>> phoneBook = new HashMap<>();

    public void add(String surname, String phone, String email) {
        if(!phoneBook.containsKey(surname)){
            phoneBook.put(surname, new ArrayList<>());
        }

        phoneBook.get(surname).add(new Person(phone, email));
    }

    public ArrayList<String> findPhoneNumbers(String surname) {
        if (phoneBook.containsKey(surname)) {
            ArrayList<Person> persons = phoneBook.get(surname);
            ArrayList<String> phoneNumbers = new ArrayList<>();

            for (Person person : persons) {
                phoneNumbers.add(person.getPhone());
            }

            return phoneNumbers;
        }

        return new ArrayList<>();
    }

    public ArrayList<String> findEmails(String surname) {
        if (phoneBook.containsKey(surname)) {
            ArrayList<Person> persons = phoneBook.get(surname);
            ArrayList<String> emails = new ArrayList<>();

            for (Person person : persons) {
                emails.add(person.getEmail());
            }

            return emails;
        }

        return new ArrayList<>();
    }
}
