package ru.yandex.practicum.gym;

public class CounterOfTrainings implements Comparable<CounterOfTrainings> {

    private final String coachName;
    private final int count;

    public CounterOfTrainings(String coachName, int count) {
        this.coachName = coachName;
        this.count = count;
    }

    public String getCoachName() {
        return coachName;
    }

    public int getCount() {
        return count;
    }

    @Override
    public int compareTo(CounterOfTrainings o) {
        return Integer.compare(o.count, this.count);
    }

    @Override
    public String toString() {
        return "Тренер: " + coachName + ", тренировок: " + count;
    }
}
