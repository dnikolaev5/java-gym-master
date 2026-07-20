package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> testOneLesson = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, testOneLesson.size());
        Assertions.assertEquals(singleTrainingSession, testOneLesson.get(0));

        List<TrainingSession> testNoLesson = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        Assertions.assertTrue(testNoLesson.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach, DayOfWeek.THURSDAY, new TimeOfDay(20, 0));
        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach, DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach, DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondaySessions.size());

        // Проверить, что за четверг вернулось два занятия в правильном порядке (13:00, затем 20:00)
        List<TrainingSession> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        Assertions.assertEquals(2, thursdaySessions.size());
        Assertions.assertEquals(thursdayChildTrainingSession, thursdaySessions.get(0)); // В 13:00
        Assertions.assertEquals(thursdayAdultTrainingSession, thursdaySessions.get(1)); // В 20:00

        // Проверить, что за вторник не вернулось занятий
        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        Assertions.assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        timetable.addNewTrainingSession(singleTrainingSession);

        // Проверить, что за понедельник в 13:00 вернулось одно занятие
        List<TrainingSession> exactSession = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        Assertions.assertEquals(1, exactSession.size());
        Assertions.assertEquals(singleTrainingSession, exactSession.get(0));

        // Проверить, что за понедельник в 14:00 не вернулось занятий
        List<TrainingSession> noSessionAtTime = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        Assertions.assertTrue(noSessionAtTime.isEmpty());
    }
    @Test
    void testGetCountByCoachesSortingAndAggregation() {
        Timetable timetable = new Timetable();
        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");
        Coach coach2 = new Coach("Петров", "Петр", "Петрович");

        Group group = new Group("Бокс", Age.ADULT, 60);

        // У Иванова 3 тренировки
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.TUESDAY, new TimeOfDay(12, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.WEDNESDAY, new TimeOfDay(14, 0)));

        // У Петрова 1 тренировка
        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.FRIDAY, new TimeOfDay(18, 0)));

        // Получаем отчет
        List<CounterOfTrainings> coachReport = timetable.getCountByCoaches();

        // Проверяем, что в отчете 2 тренера
        Assertions.assertEquals(2, coachReport.size());

        // Проверяем сортировку по убыванию: Иванов должен быть первым (3 тренировки), Петров вторым (1 тренировка)
        Assertions.assertEquals("Тренер: Иванов Иван Иванович, тренировок: 3", coachReport.get(0).toString());
        Assertions.assertEquals("Тренер: Петров Петр Петрович, тренировок: 1", coachReport.get(1).toString());
    }

    @Test
    void testAllTrainingSessionReturnsCorrectTotalCount() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Сидоров", "Олег", "Игоревич");
        Group group = new Group("Йога", Age.ADULT, 90);

        // Добавляем 3 тренировки в разные дни
        timetable.addNewTrainingSession(new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach, DayOfWeek.WEDNESDAY, new TimeOfDay(19, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach, DayOfWeek.SUNDAY, new TimeOfDay(12, 0)));

        // Проверяем, что метод allTrainingSession() возвращает все 3 записи
        List<TrainingSession> allSessions = timetable.allTrainingSession();
        Assertions.assertEquals(3, allSessions.size());
    }

    @Test
    void testParallelSessionsAtTheSameTime() {
        Timetable timetable = new Timetable();
        Coach coach1 = new Coach("Алексеев", "Алексей", "Алексеевич");
        Coach coach2 = new Coach("Дмитриев", "Дмитрий", "Дмитриевич");

        Group group1 = new Group("Кроссфит", Age.ADULT, 60);
        Group group2 = new Group("Растяжка", Age.ADULT, 60);

        // Две разные тренировки в один и тот же день (пятница) и в одно время (15:00)
        TrainingSession session1 = new TrainingSession(group1, coach1, DayOfWeek.FRIDAY, new TimeOfDay(15, 0));
        TrainingSession session2 = new TrainingSession(group2, coach2, DayOfWeek.FRIDAY, new TimeOfDay(15, 0));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);

        // Проверяем, что метод поиска по дню и времени вернул оба занятия
        List<TrainingSession> exactSessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.FRIDAY, new TimeOfDay(15, 0));

        Assertions.assertEquals(2, exactSessions.size());
        Assertions.assertTrue(exactSessions.contains(session1));
        Assertions.assertTrue(exactSessions.contains(session2));
    }
}
