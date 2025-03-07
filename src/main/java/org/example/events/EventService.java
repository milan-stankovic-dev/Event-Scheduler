package org.example.events;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

/**
 * High level abstraction for servicing events
 */
public interface EventService {
    /**
     * Method used for scheduling events.
     * @param newEvent Event to be scheduled
     * @return true if scheduled successfully, false otherwise
     */
    boolean addEvent(Event newEvent);
    /**
     * Method used for fetching events based on their start time.
     * May return only one event, as events never overlap.
     * @param start Event's starting time
     * @return Optional of event scheduled at starting time, empty optional
     * otherwise
     */
    Optional<Event> getByStart(LocalDateTime start);
    /**
     * Method used for fetching events based on their name.
     * May return more than one event, as event names are not unique.
     * @param name Event's name
     * @return Set of all scheduled events with given name
     */
    Set<Event> getByName(String name);
    /**
     * Method used for removing/unscheduling events.
     * @param start Event's starting time
     * @return True if unscheduling was successful, false otherwise
     */
    boolean removeEvent(LocalDateTime start);
    /**
     * Retrieves all events.
     * @return Set of all events.
     */
    Set<Event> getAllEvents();
}
