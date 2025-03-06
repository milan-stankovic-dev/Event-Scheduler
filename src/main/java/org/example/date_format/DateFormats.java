package org.example.date_format;

import java.time.format.DateTimeFormatter;

public interface DateFormats {
    DateTimeFormatter HUMAN_READABLE_DATE_TIME_FORMAT = DateTimeFormatter
            .ofPattern("'Date:' dd.MM.yyyy 'at:' HH:mm");
    DateTimeFormatter HUMAN_READABLE_TIME_FORMAT = DateTimeFormatter
            .ofPattern("HH:mm");
}
