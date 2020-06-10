package ru.gb.homework.homework5;

public class Application {
    public static void main(String[] args){
        Employee[] employees = new Employee[]{
                new Employee("Петров Петро", "грузчик", "ya@yandex.ru", "+79000001245", 10000, 15),
                new Employee("Семенов Семен", "управляющий", "petro@yandex.ru", "+79111001111", 100000, 150),
                new Employee("Борисов Борис", "главный", "boris@yandex.ru", "+79111002222", 1000000, 41),
                new Employee("Витальев Виталий", "помошник", "vit@yandex.ru", "+79666666666", 100, 40),
                new Employee("Арсеньев Арсений", "стажёр", "ars@yandex.ru", "+70000000000", 10, 19),
        };

        for (Employee employee : employees){
            if(employee.age > 40){
                employee.printToConsole();
            }
        }
    }
}
