package org.example.events;

import java.time.LocalDateTime;

import static org.example.date_format.DateFormats.HUMAN_READABLE_DATE_TIME_FORMAT;

/**
 * Record that defines the Event abstraction
 * @param start the localDateTime at which the scheduled event is to start
 * @param end the localDateTime at which the scheduled event it to end
 * @param name event name
 * @param description short description of the event
 */
public record Event(
        LocalDateTime start,
        LocalDateTime end,
        String name,
        String description) {
    /**
     * ToString method is overriden to create more human-readable
     * representation.
     * @return String representation of the event object.
     */
    @Override
    public String toString() {
        return  """
                * name: %s *
                    - description: %s,
                    - start time: %s,
                    - end time: %s
                """.formatted(name, description,
                start.format(HUMAN_READABLE_DATE_TIME_FORMAT),
                end.format(HUMAN_READABLE_DATE_TIME_FORMAT));
    }
}
