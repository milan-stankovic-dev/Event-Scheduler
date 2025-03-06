package org.example.events;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public interface EventService {
    boolean addEvent(Event newEvent);
    Optional<Event> getByStart(LocalDateTime start);
    Set<Event> getByName(String name);
    boolean removeEvent(LocalDateTime start);
    Set<Event> getAllEvents();
}
