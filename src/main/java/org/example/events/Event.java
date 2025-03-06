package org.example.events;

import java.time.LocalDateTime;

import static org.example.date_format.DateFormats.HUMAN_READABLE_DATE_TIME_FORMAT;


public record Event(
        LocalDateTime start,
        LocalDateTime end,
        String name,
        String description) {
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
