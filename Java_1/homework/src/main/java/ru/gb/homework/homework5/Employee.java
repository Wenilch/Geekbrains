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

    private String fio;

    private String position;

    private String email;

    private String phone;

    private int salary;

    private int age;

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public void printToConsole() {
        System.out.println(String.format("Сотрудник %s должность %s емайл %s телефон %s зарплата %d возраст %d", fio, position, email, phone, salary, age));
    }
}
