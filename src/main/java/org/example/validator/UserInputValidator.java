package org.example.validator;

import lombok.Getter;
import lombok.val;

/**
 * Validates user inputs
 */
public class UserInputValidator {
    /**
     * Singleton instance
     */
    @Getter
    private static final UserInputValidator instance = new UserInputValidator();
    /**
     * Private singleton constructor
     */
    private UserInputValidator() { }
    /**
     * Checks if string-type input is valid.
     * @param input String input from user
     * @return True if input is not blank or null, and is between 2 and 500 characters, false otherwise.
     */
    public boolean isStringInputValid(String input) {
        return isNotBlankOrNull(input) && isOfProperLength(input);
    }
    /**
     * Checks if string-type input can be turned into a valid LocalTime object.
     * @param input String input from user
     * @return True if input is not blank or null, and adheres to 'HH:mm' format, false otherwise
     */
    public boolean isTimeInputValid(String input) {
        return isNotBlankOrNull(input) && isPatternCorrect(input);
    }

    private boolean isPatternCorrect(String input) {
        try {
            final String[] tokens = input.split(":");
            if(tokens.length != 2) { return false; }

            val hours = Integer.parseInt(tokens[0].trim());
            val minutes = Integer.parseInt(tokens[1].trim());

            return hours >= 0 && hours < 24 && minutes >= 0 &&
                    minutes < 59;
        } catch (Throwable ignored) {
            return false;
        }
    }
    /**
     * Length check
     * @param input String input from user
     * @return True if input is from 3 to 499 chars long, false otherwise
     */
    private boolean isOfProperLength(String input) {
        return input.length() > 2 && input.length() < 500;
    }
    /**
     * Null/blank check
     * @param input String input from user
     * @return True if input is not blank, null, and does not contain exclusively blank characters,
     * false otherwise
     */
    private boolean isNotBlankOrNull(String input) {
        return input != null && !input.trim().isBlank();
    }

}
