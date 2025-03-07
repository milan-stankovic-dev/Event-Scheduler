package org.example.menu;

/**
 * Houses all the various messages to be shown to the app user
 * through the terminal menu.
 */
public interface MenuMessages {
    String WELCOME_MESSAGE = "Welcome to the Event Scheduler app! " +
            "This app allow you to add, remove and view scheduled events.\n";
    String MAIN_MENU = """
                    Type [1] to add new events.
                    Type [2] to remove events.
                    Type [3] to search events by start time.
                    Type [4] to search events by name.
                    Type [5] to list all scheduled events.
                    Type any other key to quit application.
                    
                    Your choice:
                    """;
    String GOODBYE_MESSAGE = "Thank you for using our app! " +
            "We welcome you back anytime!";
    String NO_EVENT_FOUND_MESSAGE = "Could not find event with those criteria. No event scheduled.";
    String EVENT_FOUND_MESSAGE = "EVENT FOUND: ";
    String EVENT_SAVED_MESSAGE =  "Event saved successfully!";
    String EVENT_NOT_SAVED_MESSAGE = "Event was not properly saved. Please try again.";
    String ENTER_START_DATE_MESSAGE  = "Input the starting time for said event. Proper format is 'HH:mm'. Example: 14:34";
    String ENTER_END_DATE_MESSAGE = "Input the ending time for said event. Proper format is 'HH:mm'. Example: 14:34";
    String ENTER_NAME_MESSAGE = "Please enter the event name:";
    String ENTER_DESCRIPTION_MESSAGE = "Please enter the event description:";
    String EVENT_DELETED_MESSAGE = "Event removed successfully!";
    String EVENT_NOT_DELETED_MESSAGE = "Event could not be removed. Try again.";
    String WRONG_INPUT_MESSAGE = "Input is invalid. Please try again.";
    String DATE_MISMATCH_ERROR_MESSAGE = """
                Mismatch in times detected. Please make sure to double-check
                and re-enter the starting time and ending time for your event.
                Event start must be after the current moment and before the event is set to end.
            """;
    String WRONG_DATE_FORMAT_MESSAGE = "Your time input is not valid. Try again. New input:";
    String RE_ENTER_EVENT_PROMPT = "Would you like to repeat this action for more events? " +
            "[Y] [N]";
    String EVENTS_QUERY_FOUND = "Here is a list of all events with given name:";
    String EVENTS_FOUND_MESSAGE = "Here is a list of all scheduled events, sorted by start time: ";
    String EVENTS_NOT_FOUND_MESSAGE = "Could not find any scheduled events.";
    String Y_OR_N_ONLY_WARNING = "Please enter [Y] or [N] to continue.";
    String BACK_TO_MENU_GREETING = "Welcome back to main menu.";
}
