package ru.gb.homework.homework5;

public class Employee {

    public Employee(String fio, String position, String email, String phone, int salary, int age) {
        this.fio = fio;
        this.position = position;
        this.email = email;
        this.phone = phone;
        this.salary = salary;
        this.age = age;
    }

    public String fio;

    public String position;

    public String email;

    public String phone;

    public int salary;

    public int age;

    public void printToConsole() {
        System.out.println(String.format("Сотрудник %s должность %s емайл %s телефон %s зарплата %d возраст %d", fio, position, email, phone, salary, age));
    }
}
