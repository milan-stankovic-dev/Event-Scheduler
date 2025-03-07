This is a demo app for scheduling events in Java. It gives its users the ability to schedule new events at a given time, delete events, search for particular events as well as display all previously scheduled events. All scheduled events are stored in the database, if possible, or in-memory if the database is not available. When running this console app, the user is greeted by this screen:

    Connected to the database successfully.
    Welcome to the Event Scheduler app! This app allow you to add, remove and view scheduled events.

    Type [1] to add new events.
    Type [2] to remove events.
    Type [3] to search events by start time.
    Type [4] to list all scheduled events.
    Type any other key to quit application.
    
    Your choice:

According to the user's choice other menus can open for different use-cases. Those include:
1) The insert menu:
   
      Please enter the event name:
      Example event
      Please enter the event description:
      Example event description
      Input the starting time for said event. Proper format is 'HH:mm'. Example: 14:34
      12:10
      Input the ending time for said event. Proper format is 'HH:mm'. Example: 14:34
      12:30
      Event saved successfully!
      * name: Example event *
          - description: Example event description,
          - start time: Date: 07.03.2025 at: 12:10,
          - end time: Date: 07.03.2025 at: 12:30
      
      Would you like to repeat this action for more events? [Y] [N]

(If the user pressed the y key, then enter, they are prompted to add multiple events. Pressing 
the N key means they are to be returned to the main menu. Any other key results in the 'Please enter [Y] or [N] to continue.' message)

2) The remove menu:

    Input the starting time for said event. Proper format is 'HH:mm'. Example: 14:34
    12:10
    Event removed successfully!
    Would you like to repeat this action for more events? [Y] [N]

If the event the user is attempting to remove does not exist (i.e it has already been deleted, or the user
has entered an erroneous time, this message is displayed:

    Event could not be removed. Try again.
    
3) The search menu:

    Input the starting time for said event. Proper format is 'HH:mm'. Example: 14:34
    14:40
    EVENT FOUND: 
    * name: Team meeting *
        - description: Discussing the upcoming tasks,
        - start time: Date: 07.03.2025 at: 14:40,
        - end time: Date: 07.03.2025 at: 15:00

  If the event could not be found, this message is displayed to the user:

      Could not find event by start date. No event scheduled.

Note: As there is *NO OVERLAP ALLOWED* in scheduled events, every event has a unique starting time. This operation may return up to
one unique event.

  4) The display all events menu:

     Here is a list of all scheduled events, sorted by start time: 
* name: Team meeting *
    - description: Discussing the upcoming tasks,
    - start time: Date: 07.03.2025 at: 14:40,
    - end time: Date: 07.03.2025 at: 15:00

* name: Example Event *
    - description: Example event description,
    - start time: Date: 06.03.2025 at: 17:00,
    - end time: Date: 06.03.2025 at: 17:30

If the user presses any other key, the app is closed. Pressing 'q' at any time closes the prompt and returns the user to the main menu.
Pressing any other key in the main menu closes the app with this message:

    Thank you for using our app! We welcome you back anytime!
