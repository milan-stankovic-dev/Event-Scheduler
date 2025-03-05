package org.example.events;

import lombok.Getter;
import lombok.val;

import java.time.LocalDateTime;
import java.util.*;

public class EventService {
    private final Set<Event> savedEvents = new TreeSet<>(
            Comparator.comparing(Event::start));
    @Getter
    private static final EventService instance = new EventService();
    private EventService() { }

    public boolean addEvent(Event event) {
        savedEvents.add(event);
        return true;
    }

    public Optional<Event> getByStart(LocalDateTime start) {
        return savedEvents.stream()
                .filter(evt -> evt.start().equals(start))
                .findFirst();
    }

    public Optional<Event> getByName(String name) {
        return savedEvents.stream()
                .filter(evt -> evt.name().equals(name))
                .findFirst();
    }

    public boolean removeEvent(LocalDateTime start) {
       return savedEvents.removeIf(evt ->
            evt.start().equals(start));
    }

    public void displayAllEvents() {
        System.out.println("Here is a list of all scheduled events, sorted by start date: ");
        savedEvents.forEach(System.out::println);
    }
}
