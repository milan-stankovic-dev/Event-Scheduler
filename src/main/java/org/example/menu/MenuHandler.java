package org.example.menu;

import lombok.Getter;
import lombok.val;
import org.example.events.Event;
import org.example.events.EventService;
import org.example.events.EventServiceProvider;
import org.example.validator.UserInputValidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

import static org.example.date_format.DateFormats.HUMAN_READABLE_TIME_FORMAT;
import static org.example.menu.MenuMessages.*;

public class MenuHandler {
    private final List<String> VALID_MENU_CHOICES = List.of(
            "1", "2", "3", "4");
    private final List<String> VALID_CONTINUE_CHOICES = List.of("Y", "N");
    @Getter
    private static final MenuHandler instance = new MenuHandler();

    private MenuHandler() { }
    private final BufferedReader reader =
            new BufferedReader(new InputStreamReader(System.in));
    private final UserInputValidator validator = UserInputValidator.getInstance();
    private final EventService service = EventServiceProvider.getInstance().getEventService();

    public void runMenu() throws IOException {
        System.out.println(WELCOME_MESSAGE);
        var userChoice = "";

        do {
            System.out.println(MAIN_MENU);
                userChoice = readInputFromUser();
                callServiceAccordingToInput(userChoice);
        } while(VALID_MENU_CHOICES.contains(userChoice));
    }

    private void callServiceAccordingToInput(String input) {
        switch (input) {
            case "1":
                promptContinuouslyUntilCRUD(this::getEventFromUserAndSaveIt);
                break;
            case "2":
                promptContinuouslyUntilCRUD(this::removeEventByUserPromptTime);
                break;
            case "3":
                final Optional<Event> evt = getEventByPromptingUserForDate();
                if(evt.isPresent()) {
                    System.out.println(EVENT_FOUND_MESSAGE);
                    System.out.println(evt.get());
                }
                break;
            case "4":
                val eventsFound = service.getAllEvents();
                if(eventsFound == null || eventsFound.isEmpty()) {
                    System.out.println(EVENTS_NOT_FOUND_MESSAGE);
                } else {
                    System.out.println(EVENTS_FOUND_MESSAGE);
                    eventsFound.forEach(System.out::println);
                }
                break;
            default:
                System.out.println(GOODBYE_MESSAGE);
                break;
        }
    }

    private void promptContinuouslyUntilCRUD(BooleanSupplier operationFunc) {
        boolean shouldContinue;
        do {
            reAttemptOperationUnlessSuccessful(operationFunc);
            System.out.println(RE_ENTER_EVENT_PROMPT);
            shouldContinue = promptUserForYesOrNo();
        } while(shouldContinue);
    }

    private boolean promptUserForYesOrNo() {
        String userChoice;
        boolean userChoseYorN;
        do {
            userChoice = readInputFromUserDefaultToEmpty();
            userChoseYorN = VALID_CONTINUE_CHOICES.contains(userChoice);
            if(!userChoseYorN) {
                System.out.println(Y_OR_N_ONLY_WARNING);
            }
        } while(!userChoseYorN);

        return userChoice.equals("Y");
    }

    private void reAttemptOperationUnlessSuccessful(BooleanSupplier operation) {
        boolean successfulSave;
        do {
            successfulSave = operation.getAsBoolean();
        } while (!successfulSave);
    }

    private Optional<Event> getEventByPromptingUserForDate() {
        try {
            final LocalDateTime dateTime = getDateTimeFromPrompt();
            return service.getByStart(dateTime);
        } catch( Throwable ignored ) {
            System.out.println(NO_EVENT_FOUND_MESSAGE);
            return Optional.empty();
        }
    }

    private boolean getEventFromUserAndSaveIt() {
        Event newEvent;
        try {
             newEvent = getNewEventByPromptingUser();
        } catch (IOException ex) {
            System.out.printf("Error occurred! %s\n", ex.getMessage());
            return false;
        }
        val isSaveSuccessful = service.addEvent(newEvent);

        if(isSaveSuccessful) {
            System.out.println(EVENT_SAVED_MESSAGE);
            System.out.println(newEvent);
        }else {
            System.out.println(EVENT_NOT_SAVED_MESSAGE);
        }

        return isSaveSuccessful;
    }

    private LocalDateTime getDateTimeFromPrompt() throws IOException {
        return getDateTimeFromString(
                getProperInputFromUser(ENTER_START_DATE_MESSAGE,
                        validator::isTimeInputValid));
    }

    private boolean removeEventByUserPromptTime() {
        LocalDateTime evtStart;
        boolean removalSuccessful;
        try {
            evtStart = getDateTimeFromPrompt();
            removalSuccessful = service.removeEvent(evtStart);

            if(removalSuccessful) {
                System.out.println(EVENT_DELETED_MESSAGE);
                return true;
            }

            System.out.println(EVENT_NOT_DELETED_MESSAGE);
            return false;

        } catch (IOException ignored) {
            System.out.println(WRONG_INPUT_MESSAGE);
        }

        return false;
    }

    private Event getNewEventByPromptingUser() throws IOException {
        val eventName = getProperInputFromUser(ENTER_NAME_MESSAGE,
                validator::isStringInputValid);
        val eventDescription = getProperInputFromUser(ENTER_DESCRIPTION_MESSAGE,
                validator::isStringInputValid);

        LocalDateTime startingTime, endingTime;
        String startTimeStringToken, endingTimeStringToken;

        do {
            startTimeStringToken = getProperInputFromUser(ENTER_START_DATE_MESSAGE, validator::isTimeInputValid);
            endingTimeStringToken = getProperInputFromUser(ENTER_END_DATE_MESSAGE, validator::isTimeInputValid);

            startingTime = getDateTimeFromString(startTimeStringToken);
            endingTime = getDateTimeFromString(endingTimeStringToken);

            if (startingTime.isBefore(LocalDateTime.now()) || startingTime.isAfter(endingTime)) {
                System.out.println(DATE_MISMATCH_ERROR_MESSAGE);
            }

        } while (startingTime.isBefore(LocalDateTime.now()) || startingTime.isAfter(endingTime));

        return new Event(startingTime, endingTime, eventName, eventDescription);
    }

    private LocalDateTime getDateTimeFromString(String input) throws IOException {
        final LocalDate today = LocalDate.now();
        LocalTime startingTime = null;

        while (startingTime == null) {
            try {
                startingTime = LocalTime.parse(input, HUMAN_READABLE_TIME_FORMAT);
            } catch (DateTimeParseException e) {
                System.out.println(WRONG_DATE_FORMAT_MESSAGE);
                input = readInputFromUser();
            }
        }

        return LocalDateTime.of(today, startingTime);
    }

    private String getProperInputFromUser(String displayText, Predicate<String> validatorFunc)
                throws IOException {
        System.out.println(displayText);

        String userInput;
        boolean isValidInput;
        do {
            userInput = readInputFromUser();
            isValidInput = validatorFunc.test(userInput);
            if(!isValidInput) { System.out.println(WRONG_INPUT_MESSAGE); }

        } while(!isValidInput);

        return userInput;
    }

    private String readInputFromUser() throws IOException {
        return reader.readLine().trim();
    }

    private String readInputFromUserDefaultToEmpty() {
        try {
            return readInputFromUser();
        } catch (IOException ignored) {
            return "";
        }
    }
}
