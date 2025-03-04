package org.example.menu;

import lombok.Getter;
import lombok.val;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class MenuHandler {
    @Getter
    private static final MenuHandler instance = new MenuHandler();

    private MenuHandler() { }

    public void runMenu() throws IOException {
        System.out.println("Welcome to the Event Scheduler app! " +
                "This app allow you to add, remove and view scheduled events.\n");
        System.out.println("""
                Type [1] to add new events.
                Type [2] to remove events.
                Type [3] to list all scheduled events.
                Type any other key to quit application.
                
                Your choice:
                """);
        val inputStream = new BufferedReader(new InputStreamReader(System.in));
        final String userChoice = inputStream.readLine().trim();

        if(!VALID_MENU_CHOICES.contains(userChoice)) {
            System.out.println("Thanks for using our program. Goodbye!");
            return;
        } else {
            System.out.printf("User choice: %s", userChoice);
        }
    }

    private final List<String> VALID_MENU_CHOICES = List.of(
        "1", "2", "3");
}
