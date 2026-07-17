package ru.yandex.practicum.gym;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    private static final Timetable timetable = new Timetable();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        while (true) {
            printMenu();

            System.out.println("Выберите действие: ");
            int command = scanner.nextInt();

            if (command == 0) {
                System.out.println("----Выход из программы------");
                break;
            }
            if (command == 1) {
                addSession();
            }
            if (command == 2) {
                // Вывод всех тренировок, упорядоченных по времени начала, за конкретный день недели
                printSessions();
            }
            if (command == 3) {
                // Вывод всех тренировок, упорядоченных по времени начала, за конкретный день недели
                getSessionsByTime();
            }
            if (command == 4) {
                // Вывод всех тренировок, упорядоченных по времени начала, за конкретный день недели
                printCoachesReport();
            }
        }
    }

    public static void printMenu() {
        System.out.println("\n--- Меню спортзала ----");
        System.out.println("1 - Добавить новую тренировку.");
        System.out.println("2 - Вывод всех тренировок, упорядоченных по времени начала, за конкретный день недели");
        System.out.println("3 - Получение всех тренировок, начинающихся в конкретное время, за конкретный день недели");
        System.out.println("4 - Показать сколько занятий в неделю ведёт каждый из тренеров");
    }

    public static void addSession() {
        // 1. Ввод времени
        System.out.print("Введите часы (0-23): ");
        int hours = scanner.nextInt();

        System.out.print("Введите минуты (0-59): ");
        int minutes = scanner.nextInt();
        TimeOfDay timeOfDay = new TimeOfDay(hours, minutes);

        // 2. Ввод дня недели
        System.out.print("Введите день недели (например, Monday): ");
        String dayWord = scanner.next();
        DayOfWeek dayOfWeek = DayOfWeek.valueOf(dayWord.toUpperCase(Locale.ROOT));

        // Очистка буфера сканера после чтения строк через next()
        scanner.nextLine();

        // 3. Ввод данных группы
        System.out.print("Введите название группы: ");
        String titleGroup = scanner.nextLine();

        System.out.print("Введите тип группы (child или adult): ");
        String typeGroup = scanner.next();
        Age age = Age.valueOf(typeGroup.toUpperCase(Locale.ROOT));

        // Очистка буфера сканера
        scanner.nextLine();

        System.out.print("Введите длительность тренировки (в минутах): ");
        int workoutDuration = scanner.nextInt();

        // Очистка буфера сканера
        scanner.nextLine();

        Group group = new Group(titleGroup, age, workoutDuration);

        // 4. Ввод данных тренера
        System.out.print("Введите фамилию тренера: ");
        String surnameCoach = scanner.nextLine();

        System.out.print("Введите имя тренера: ");
        String nameCoach = scanner.nextLine();

        System.out.print("Введите отчество тренера: ");
        String middleName = scanner.nextLine();

        Coach coach = new Coach(surnameCoach, nameCoach, middleName);

        TrainingSession trainingSession = new TrainingSession(group, coach, dayOfWeek, timeOfDay);
        timetable.addNewTrainingSession(trainingSession);
    }

    public static void printSessions() {

        System.out.print("Введите день недели для вывода расписания (например, Monday): ");
        String dayWord = scanner.next();

        DayOfWeek dayOfWeek = DayOfWeek.valueOf(dayWord.toUpperCase(Locale.ROOT));

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDay(dayOfWeek);

        if (sessions.isEmpty()) {
            System.out.println("Расписание пусто");
            return;
        }

        System.out.println("---- Расписание на " + dayOfWeek + " ----");
        for (TrainingSession trainingSession : sessions) {
            printOneSession(trainingSession);
            System.out.println("------------------------");
        }
    }

    public static void getSessionsByTime() {
        System.out.print("Введите день недели (например, Monday): ");
        String dayWord = scanner.next();

        DayOfWeek dayOfWeek = DayOfWeek.valueOf(dayWord.toUpperCase(Locale.ROOT));

        System.out.print("Введите часы (0-23): ");
        int hours = scanner.nextInt();

        System.out.print("Введите минуты (0-59): ");
        int minutes = scanner.nextInt();

        TimeOfDay timeOfDay = new TimeOfDay(hours, minutes);

        List<TrainingSession> trainingSessions = timetable.getTrainingSessionsForDayAndTime(dayOfWeek, timeOfDay);

        for (TrainingSession oneSession : trainingSessions) {
            printOneSession(oneSession);
            System.out.println("------------------------");
        }

    }

    public static void printOneSession(TrainingSession trainingSession) {
        System.out.println("День: " + trainingSession.getDayOfWeek());
        System.out.printf("Время: %02d:%02d%n", trainingSession.getTimeOfDay().getHours(), trainingSession.getTimeOfDay().getMinutes());
        System.out.println("Группа: " + trainingSession.getGroup().getTitle());
        System.out.println("Тренер: " + trainingSession.getCoach().getSurname() + " "
                + trainingSession.getCoach().getName().charAt(0) + "."
                + trainingSession.getCoach().getMiddleName().charAt(0) + ".");
    }

    public static void printCoachesReport() {
        // Получаем отсортированный по убыванию список счетчиков
        List<CounterOfTrainings> report = timetable.getCountByCoaches();

        System.out.println("---- Отчет (тренер, количество тренировок: ) ---");
        for (CounterOfTrainings counter : report) {
            // Здесь автоматически вызовется ваш метод toString() из класса CounterOfTrainings
            System.out.println(counter);
        }
        System.out.println("--------------------------------------------------");
    }
}
