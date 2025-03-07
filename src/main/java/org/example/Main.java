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
        val menuHandler = MenuHandler.getInstance();
        try {
            menuHandler.runMenu();
        } catch (IOException e) {
            System.out.println("Major I/O related error occurred! " +
                    "Please make sure your terminal is operating correctly.");
        } catch (Throwable t) {
            System.out.println("Unknown Error occurred and the program had to stop! " +
                    "Please contact tech support. " + t.getMessage());
        }
    }
}
