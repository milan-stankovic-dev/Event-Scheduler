package org.example.validator;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ofPattern;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for UserInputValidator
 */
public class UserInputValidatorTest {
    private final UserInputValidator validator = UserInputValidator.getInstance();

    @Test
    void sanityCheck() {
        assertEquals(4,2+2);
    }

    @ParameterizedTest
    @DisplayName("Valid input string tests.")
    @CsvFileSource(resources = "/csv/validInputStrings.csv")
    void validInputTests(String input) {
        assertTrue(validator.isStringInputValid(input));
    }

    @ParameterizedTest
    @DisplayName("Invalid input string tests.")
    @CsvFileSource(resources = "/csv/invalidInputStrings.csv")
    void invalidInputTests(String input) {
        assertFalse(validator.isStringInputValid(input));
    }

    @ParameterizedTest
    @DisplayName("Valid input times tests.")
    @CsvFileSource(resources = "/csv/validTimes.csv")
    void validInputTimes(String input) {
        assertTrue(validator.isTimeInputValid(input));
    }

    @ParameterizedTest
    @DisplayName("Invalid input times tests.")
    @CsvFileSource(resources = "/csv/invalidTimes.csv")
    void invalidTimesTests(String input) {
        assertFalse(validator.isTimeInputValid(input));
    }

    private final DateTimeFormatter formatter = ofPattern("yyyy-MM-dd HH:mm");

    @ParameterizedTest
    @DisplayName("Start end validator tests.")
    @CsvFileSource(resources = "/csv/startEndDates.csv")
    void startEndTimesTest(String date1, String date2, boolean expected) {
        final LocalDateTime startTime = LocalDateTime.parse(date1, formatter);
        final LocalDateTime endTime = LocalDateTime.parse(date2, formatter);

        final boolean actual = validator.areStartEndTimesValid(startTime, endTime);

        assertEquals(expected, actual);
    }
}
