package org.example.events;

import java.time.LocalDateTime;

public record Event(
        LocalDateTime start,
        LocalDateTime end,
        String name,
        String description) { }
