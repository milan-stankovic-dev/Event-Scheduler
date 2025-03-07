package org.example.date_format;

import java.time.format.DateTimeFormatter;

/**
 * Interface for different date formats. It houses DateFormatParser fields for human readable dates.
 */
public interface DateFormats {
    /**
     * Standard date for user display, includes date and time
     */
    DateTimeFormatter HUMAN_READABLE_DATE_TIME_FORMAT = DateTimeFormatter
            .ofPattern("'Date:' dd.MM.yyyy 'at:' HH:mm");
    /**
     * User input form for getting proper time
     */
    DateTimeFormatter HUMAN_READABLE_TIME_FORMAT = DateTimeFormatter
            .ofPattern("HH:mm");
}
