package org.example.menu;

import lombok.Getter;
import lombok.val;
import org.example.events.Event;
import org.example.events.EventService;
import org.example.events.EventServiceProvider;
import org.example.exception.UserQuitException;
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
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

import static org.example.date_format.DateFormats.HUMAN_READABLE_TIME_FORMAT;
import static org.example.menu.MenuMessages.*;

/**
 * Handles user menu logic
 */
public class MenuHandler {
    /**
     * List of valid menu navigation keys. Each of these is supposed to open a
     * new menu dialog
     */
    private final List<String> VALID_MENU_CHOICES = List.of(
            "1", "2", "3", "4", "5");
    /**
     * Yes or no navigation inputs to check for user's consent
     */
    private final List<String> VALID_CONTINUE_CHOICES = List.of("Y", "N");
    /**
     * Singleton instance
     */
    @Getter
    private static final MenuHandler instance = new MenuHandler();
    /**
     * Private singleton constructor
     */
    private MenuHandler() { }
    /**
     * User input stream
     */
    private final BufferedReader reader =
            new BufferedReader(new InputStreamReader(System.in));
    /**
     * User input validator
     */
    private final UserInputValidator validator = UserInputValidator.getInstance();
    /**
     * EventService instance. The proper implementation provided to this interface is
     * determined by the EventServiceProvider class.
     */
    private final EventService service = EventServiceProvider.getInstance().getEventService();
    /**
     * Main menu method of this class. Runs all other menus
     * @throws IOException if I/O stream fails
     */
    public void runMenu() throws IOException {
        System.out.println(WELCOME_MESSAGE);
        var userChoice = "";

        do {
            System.out.println(MAIN_MENU);
            try {
                userChoice = readInputFromUser();
                callServiceAccordingToInput(userChoice);
            } catch (UserQuitException ex) {
                System.out.println(ex.getMessage());
            }
        } while(VALID_MENU_CHOICES.contains(userChoice));
    }

    /**
     * Runs different operations according to user's input:
     *  * 1 -> saves new event(s)
     *  * 2 -> removes existing event(s)
     *  * 3 -> gets event by starting date time
     *  * 4 -> gets event(s) by name
     *  * 5 -> finds all saved events
     *  * all other inputs -> quits the app
     * @param input User input
     * @throws IOException if I/O stream fails
     */
    private void callServiceAccordingToInput(String input) throws IOException {
        switch (input) {
            case "1":
                promptContinuouslyUntilCRUD(this::getEventFromUserAndSaveIt);
                break;
            case "2":
                promptContinuouslyUntilCRUD(this::removeEventByUserPromptTime);
                break;
            case "3":
                final Optional<Event> evt = getEventByPromptingUserForDateTime();
                if(evt.isPresent()) {
                    System.out.println(EVENT_FOUND_MESSAGE);
                    System.out.println(evt.get());
                } else {
                    System.out.println(NO_EVENT_FOUND_MESSAGE);
                }
                break;
            case "4":
                final Set<Event> events = getEventsByPromptingUserForName();
                if(events.isEmpty()) {
                    System.out.println(NO_EVENT_FOUND_MESSAGE);
                } else {
                    events.forEach(System.out::println);
                }
                break;
            case "5":
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
    /**
     * Generic method for repeated prompting of the user until something is written in the
     * database (CUD operations)
     * @param operationFunc Operation to be executed. Supposed to return true if operation
     *                      was successful, false otherwise.
     * @throws IOException if I/O stream fails
     */
    private void promptContinuouslyUntilCRUD(BooleanSupplier operationFunc) throws IOException {
        boolean shouldContinue;
        do {
            reAttemptOperationUnlessSuccessful(operationFunc);
            System.out.println(RE_ENTER_EVENT_PROMPT);
            shouldContinue = promptUserForYesOrNo();
        } while(shouldContinue);
    }
    /**
     * Continuously prompts user until the user gives a 'Y' or 'N'.
     * Used to check for user's consent.
     * @return True if user has typed 'Y' after continuous prompting,
     * false if user has typed 'N' after continuous prompting.
     * @throws IOException if I/O stream fails
     */
    private boolean promptUserForYesOrNo() throws IOException {
        String userChoice;
        boolean userChoseYorN;
        do {
            userChoice = readInputFromUser().toUpperCase();
            userChoseYorN = VALID_CONTINUE_CHOICES.contains(userChoice);
            if(!userChoseYorN) {
                System.out.println(Y_OR_N_ONLY_WARNING);
            }
        } while(!userChoseYorN);

        return userChoice.equals("Y");
    }
    /**
     * Re-attempts operation until successful outcome. Used in CUD-type operations
     * @param operation CUD type function that returns true if operation completed successfully,
     *                  false otherwise
     */
    private void reAttemptOperationUnlessSuccessful(BooleanSupplier operation) {
        boolean successfulSave;
        do {
            successfulSave = operation.getAsBoolean();
        } while (!successfulSave);
    }
    /**
     * Continuously prompts user for date-time and searches the saved records for
     * an event with given date-time
     * @return Optional with Event at the date-time fetched from the user,
     * empty optional if no event was scheduled for that date-time.
     */
    private Optional<Event> getEventByPromptingUserForDateTime() {
        try {
            final LocalDateTime dateTime = getDateTimeFromPrompt();
            return service.getByStart(dateTime);
        } catch( Throwable ignored ) {
            System.out.println(NO_EVENT_FOUND_MESSAGE);
            return Optional.empty();
        }
    }
    /**
     * Gets events from EventService by continuously prompting user until they input a valid
     * event name
     * @return Set of events with given name, from EventService
     * @throws IOException if I/O stream fails
     */
    private Set<Event> getEventsByPromptingUserForName() throws IOException {
        final String name = getProperInputFromUser(ENTER_NAME_MESSAGE, validator::isStringInputValid);

        return service.getByName(name);
    }
    /**
     * Prompts the user to create a new event object, then schedules and saves it.
     * @return True if scheduling was successful, false otherwise
     */
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
    /**
     * Continuously prompts user to get a LocalDateTime instance
     * @return LocalDateTime instance gathered from user's input
     * @throws IOException if I/O stream fails
     */
    private LocalDateTime getDateTimeFromPrompt() throws IOException {
        return getDateTimeFromString(
                getProperInputFromUser(ENTER_START_DATE_MESSAGE,
                        validator::isTimeInputValid));
    }

    /**
     * Calls the EventService to remove an event at a date-time
     * prompted from the user.
     * @return true if removal is successful, false otherwise
     */
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
    /**
     * Gets the event object from the user by continuous prompting and error handling.
     * @return Event object gathered from the user's input
     * @throws IOException if I/O stream fails
     */
    private Event getNewEventByPromptingUser() throws IOException {
        val eventName = getProperInputFromUser(ENTER_NAME_MESSAGE,
                validator::isStringInputValid);
        val eventDescription = getProperInputFromUser(ENTER_DESCRIPTION_MESSAGE,
                validator::isStringInputValid);

        LocalDateTime startingTime, endingTime;
        String startTimeStringToken, endingTimeStringToken;

        boolean areDatesValid;
        do {
            startTimeStringToken = getProperInputFromUser(ENTER_START_DATE_MESSAGE, validator::isTimeInputValid);
            endingTimeStringToken = getProperInputFromUser(ENTER_END_DATE_MESSAGE, validator::isTimeInputValid);

            startingTime = getDateTimeFromString(startTimeStringToken);
            endingTime = getDateTimeFromString(endingTimeStringToken);

            areDatesValid = validator.areStartEndTimesValid(startingTime, endingTime);
            if (!areDatesValid) {
                System.out.println(DATE_MISMATCH_ERROR_MESSAGE);
            }

        } while (!areDatesValid);

        return new Event(startingTime, endingTime, eventName, eventDescription);
    }
    /**
     * Converts user's time input into a date-time instance. Defaults to 'today'
     * for date part
     * @param input User's text input
     * @return LocalDateTime instance
     * @throws IOException if I/O stream fails
     */
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
    /**
     * Generic method that continuously prompts user for input, until the validator func is not satisfied
     * @param displayText Text to display to the user
     * @param validatorFunc Function that validates user's input
     * @return Correct user input as a String
     * @throws IOException if I/O stream fails
     */
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
    /**
     * Reads input from the user
     * @return User's input
     * @throws IOException if I/O stream fails
     */
    private String readInputFromUser() throws IOException {
        final String input = reader.readLine().trim();
        if("Q".equalsIgnoreCase(input)) {
            throw new UserQuitException(BACK_TO_MENU_GREETING);
        }
        return input;
    }
}
