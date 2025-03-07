package org.example.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.*;

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
}
