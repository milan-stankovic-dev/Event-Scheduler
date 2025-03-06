package org.example.events.impl;

import lombok.Getter;
import org.example.events.Event;
import org.example.events.EventService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class EventServiceInMemory implements EventService {
    private final NavigableSet<Event> savedEvents = new TreeSet<>(
            Comparator.comparing(Event::start));
    @Getter
    private static final EventServiceInMemory instance = new EventServiceInMemory();
    private EventServiceInMemory() { }

    public boolean addEvent(Event newEvent) {
        final Event before = savedEvents.floor(newEvent);
        final Event after = savedEvents.ceiling(newEvent);

        if ((before != null && !before.end().isBefore(newEvent.start())) ||
                (after != null && !newEvent.end().isBefore(after.start()))) {
            System.out.println("Cannot add event due to overlap.");
            return false;
        }

        savedEvents.add(newEvent);
        return true;
    }

    @Override
    public Optional<Event> getByStart(LocalDateTime start) {
        return savedEvents.stream()
                .filter(evt -> evt.start().equals(start))
                .findFirst();
    }

    @Override
    public Set<Event> getByName(String name) {
        return savedEvents.stream()
                .filter(evt -> evt.name().equals(name)).collect(Collectors.toSet());
    }

    @Override
    public boolean removeEvent(LocalDateTime start) {
       return savedEvents.removeIf(evt ->
            evt.start().equals(start));
    }

    @Override
    public void displayAllEvents() {
        if(savedEvents.isEmpty()) {
            System.out.println("There are no events scheduled for today.\n");
            return;
        }
        System.out.println("Here is a list of all scheduled events, sorted by start date: ");
        savedEvents.forEach(System.out::println);
        System.out.println();
    }
}
