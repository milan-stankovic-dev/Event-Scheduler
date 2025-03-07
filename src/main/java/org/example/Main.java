package org.example;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.menu.MenuHandler;

import java.io.IOException;

/**
 * Main class for the project
 */
@Slf4j
public class Main {
    /**
     * Main method
     * @param args Command line input args
     */
    public static void main(String[] args) {
        try {
            val menuHandler = MenuHandler.getInstance();
            menuHandler.runMenu();
        } catch (IOException eof) {
            log.error("There was an error related to user menu inputs. Error contents: ${}",
                    eof.getMessage());
        }
    }
}
