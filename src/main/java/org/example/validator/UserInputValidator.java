package org.example.validator;

import lombok.Getter;
import lombok.val;

public class UserInputValidator {
    @Getter
    private static final UserInputValidator instance = new UserInputValidator();
    private UserInputValidator() { }

    public boolean isStringInputValid(String input) {
        return isNotBlankOrNull(input) && isOfProperLength(input);
    }

    public boolean isTimeInputValid(String input) {
        return isNotBlankOrNull(input) && isPatternCorrect(input);
    }

    private boolean isPatternCorrect(String input) {
        try {
            final String[] tokens = input.split(":");
            if(tokens.length != 2) { return false; }

            val hours = Integer.parseInt(tokens[0].trim());
            val minutes = Integer.parseInt(tokens[1].trim());

            return hours > 0 && hours < 24 && minutes > 0 &&
                    minutes < 59;
        } catch (Throwable ignored) {
            return false;
        }
    }

    private boolean isOfProperLength(String input) {
        return input.length() > 2 && input.length() < 500;
    }

    private boolean isNotBlankOrNull(String input) {
        return input != null && !input.trim().isBlank();
    }

}
