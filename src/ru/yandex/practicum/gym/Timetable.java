package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private List<String> training = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);
    private final List<TrainingSession> sessions = new ArrayList<>();
    private HashMap<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable;

    public Timetable() {
        this.timetable = new HashMap<>();
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {

        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        if (!timetable.containsKey(day)) {
            timetable.put(day, new TreeMap<>());
        }

        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(day);
        if (!dayMap.containsKey(time)) {
            dayMap.put(time, new ArrayList<>());
        }

        List<TrainingSession> sessions = dayMap.get(time);
        sessions.add(trainingSession);
    }


    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        // получаем внутреннюю карту
        TreeMap<TimeOfDay, List<TrainingSession>> timeMap = timetable.get(dayOfWeek);

        if (timeMap == null) {
            return new ArrayList<>();
        }

        // создание итогового списка
        List<TrainingSession> trainingSessions = new ArrayList<>();

        for (List<TrainingSession> elem : timeMap.values()) {
            trainingSessions.addAll(elem);
        }

        return trainingSessions;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> session = timetable.get(dayOfWeek);

        if (session == null) {
            return new ArrayList<>();
        }

        return session.getOrDefault(timeOfDay, new ArrayList<>());
    }

    public List<TrainingSession> allTrainingSession() {
        return sessions;
    }

    public List<CounterOfTrainings> getCountByCoaches() {

        // ключ-имя тренера, значение кол-во тренеровок
        HashMap<Coach, Integer> coachCount = new HashMap<>();

        for (TreeMap<TimeOfDay, List<TrainingSession>> timeMap : timetable.values()) {
            for (List<TrainingSession> trainingSessionList : timeMap.values()) {
                for (TrainingSession trainingSession : trainingSessionList) {

                    Coach coach = trainingSession.getCoach();
                    coachCount.put(coach, coachCount.getOrDefault(coach, 0) + 1);
                }
            }
        }

        List<CounterOfTrainings> counters = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : coachCount.entrySet()) {
            Coach coach = entry.getKey();
            String fullName = coach.getSurname() + " " + coach.getName() + " " + coach.getMiddleName();

            counters.add(new CounterOfTrainings(fullName, entry.getValue()));
        }

        Collections.sort(counters);
        return counters;
    }
}
