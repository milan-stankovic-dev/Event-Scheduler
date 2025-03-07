package org.example.events.impl;

import lombok.Getter;
import org.example.events.Event;
import org.example.events.EventService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * In-memory implementation of the EventService interface
 */
public class EventServiceInMemory implements EventService {
    /**
     * Sorted collection of all saved events. Sorted by starting date-time
     */
    private final NavigableSet<Event> savedEvents = new TreeSet<>(
            Comparator.comparing(Event::start));
    /**
     * Singleton instance
     */
    @Getter
    private static final EventServiceInMemory instance = new EventServiceInMemory();
    /**
     * Private singleton constructor
     */
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
    public Set<Event> getAllEvents() {
        return savedEvents;
    }
}
