package org.example.menu;

import lombok.Getter;
import lombok.val;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class MenuHandler {
    private final List<String> VALID_MENU_CHOICES = List.of(
            "1", "2", "3");

    @Getter
    private static final MenuHandler instance = new MenuHandler();

    private MenuHandler() { }

    public void runMenu() throws IOException {
        System.out.println("Welcome to the Event Scheduler app! " +
                "This app allow you to add, remove and view scheduled events.\n");
        var userChoice = "";

        do {
            System.out.println("""
                    Type [1] to add new events.
                    Type [2] to remove events.
                    Type [3] to list all scheduled events.
                    Type any other key to quit application.
                    
                    Your choice:
                    """);
                userChoice = getUserInput();

        } while(VALID_MENU_CHOICES.contains(userChoice));

        System.out.println("Thank you for using our program. Goodbye!");
    }

    private void callServiceAccordingToInput(String input) {
        switch (input) {
            case "1":
            case "2":
            case "3":
            System.out.printf("User choice: %s", input);
            break;
            default:
                System.out.println("Unknown error occurred. Please test the switch statement.");
        }
    }

    private String getUserInput() throws IOException {
        val inputStream = new BufferedReader(new InputStreamReader(System.in));
        return inputStream.readLine().trim();
    }


}
