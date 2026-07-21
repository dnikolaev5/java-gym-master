package ru.yandex.practicum.gym;

public class CounterOfTrainings implements Comparable<CounterOfTrainings> {

    private final Coach coachName;
    private final int count;

    public CounterOfTrainings(Coach coachName, int count) {
        this.coachName = coachName;
        this.count = count;
    }

    public Coach getCoachName() {
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
