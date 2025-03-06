package org.example.menu;

import lombok.Getter;
import lombok.val;
import org.example.events.Event;
import org.example.events.EventService;
import org.example.validator.UserInputValidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static org.example.date_format.DateFormats.HUMAN_READABLE_TIME_FORMAT;

public class MenuHandler {
    private final List<String> VALID_MENU_CHOICES = List.of(
            "1", "2", "3", "4");
    @Getter
    private static final MenuHandler instance = new MenuHandler();

    private MenuHandler() { }
    private final BufferedReader reader =
            new BufferedReader(new InputStreamReader(System.in));
    private final UserInputValidator validator = UserInputValidator.getInstance();
    private final EventService service = EventService.getInstance();

    public void runMenu() throws IOException {
        System.out.println("Welcome to the Event Scheduler app! " +
                "This app allow you to add, remove and view scheduled events.\n");
        var userChoice = "";

        do {
            System.out.println("""
                    Type [1] to add new events.
                    Type [2] to remove events.
                    Type [3] to search events by start time.
                    Type [4] to list all scheduled events.
                    Type any other key to quit application.
                    
                    Your choice:
                    """);
                userChoice = getUserMenuModeInput();
                callServiceAccordingToInput(userChoice);
        } while(VALID_MENU_CHOICES.contains(userChoice));

        System.out.println("Thank you for using our program. Goodbye!");
    }

    private void callServiceAccordingToInput(String input) {
        switch (input) {
            case "1":
                boolean successfulSave;
                do {
                    successfulSave = getEventFromUserAndSaveIt();
                } while (!successfulSave);
                break;
            case "2":
                boolean successfulRemoval;
                do {
                    successfulRemoval = removeEventByUserPromptTime();
                } while (!successfulRemoval);
                break;
            case "3":
                final Optional<Event> evt = getEventByPromptingUserForDate();
                if(evt.isPresent()) {
                    System.out.println("EVENT FOUND: ");
                    System.out.println(evt.get());
                }
                break;
            case "4":
                service.displayAllEvents();
                break;
            default:
                System.out.println("Thank you for using our app! " +
                        "We welcome you back anytime!");
                break;
        }
    }

    private Optional<Event> getEventByPromptingUserForDate() {
        try {
            final LocalDateTime dateTime = getDateTimeFromPrompt();
            return service.getByStart(dateTime);
        } catch( Throwable ignored ) {
            System.out.println("Could not find event by start date. No event scheduled.");
            return Optional.empty();
        }
    }

    private boolean getEventFromUserAndSaveIt() {
        Event newEvent;
        try {
             newEvent = getNewEventByPromptingUser();
        } catch (IOException | IllegalArgumentException ex) {
            System.out.printf("Error occurred! %s\n", ex.getMessage());
            return false;
        }
        val isSaveSuccessful = service.addEvent(newEvent);

        if(isSaveSuccessful) {
            System.out.println("Event saved successfully!");
            System.out.println(newEvent);
        }else {
            System.out.println("Event was not properly saved. Please try again.");
        }

        return isSaveSuccessful;
    }

    private LocalDateTime getDateTimeFromPrompt() throws IOException {
        return getDateTimeFromString(
                getProperInputFromUser("Please enter the starting time of the event to be deleted." +
                        " Proper format is 'HH:mm'", validator::isTimeInputValid));
    }

    private boolean removeEventByUserPromptTime() {
        LocalDateTime evtStart = null;
        boolean removalSuccessful;
        try {
            evtStart = getDateTimeFromPrompt();
            removalSuccessful = service.removeEvent(evtStart);

            if(removalSuccessful) {
                System.out.println("Event removed successfully.");
                return true;
            }

            System.out.println("Event could not be removed. Try again.");
            return false;

        } catch (IOException ignored) {
            System.out.println("Input wrong. Try again.");
        }

        return false;
    }

    private Event getNewEventByPromptingUser() throws IOException {
        val eventName = getProperInputFromUser("Please enter the event name: ",
                validator::isStringInputValid);
        val eventDescription = getProperInputFromUser("Please enter the event description:",
                validator::isStringInputValid);
        val startTimeStringToken = getProperInputFromUser(
                "Input the starting time for said event. Proper format is 'HH:mm'.",
                validator::isTimeInputValid);
        val endingTimeStringToken = getProperInputFromUser(
                "Input the ending time for said event. Proper format is 'HH:mm'.",
                validator::isTimeInputValid);

        final LocalDateTime startingTime = getDateTimeFromString(startTimeStringToken);
        final LocalDateTime endingTime = getDateTimeFromString(endingTimeStringToken);

        if(startingTime.isAfter(endingTime)) {
            throw new IllegalArgumentException("Wrong input. Before date must not be after date. Try again.");
        }

        return new Event(startingTime, endingTime, eventName, eventDescription);
    }

    private LocalDateTime getDateTimeFromString(String input) {
        final LocalDate today = LocalDate.now();
        final LocalTime startingTime = LocalTime.parse(input, HUMAN_READABLE_TIME_FORMAT);

        return LocalDateTime.of(today, startingTime);
    }

    private String getProperInputFromUser(String displayText, Predicate<String> validatorFunc)
                throws IOException {
        System.out.println(displayText);

        String userInput;
        boolean isValidInput;
        do {
            userInput = reader.readLine().trim();
            isValidInput = validatorFunc.test(userInput);
            if(!isValidInput) { System.out.println("Your input is not valid. Try again. New input: "); }

        } while(!isValidInput);

        return userInput;
    }

    private String getUserMenuModeInput() throws IOException {
        return reader.readLine().trim();
    }
}
