package org.example.events;

import lombok.Getter;
import org.example.events.impl.EventServiceDB;
import org.example.events.impl.EventServiceInMemory;
import org.example.liquibase.LiquibaseIntegration;

/**
 * Provides appropriate EventService instance. Prioritizes db persistence over
 * in-memory persistence.
 */
public class EventServiceProvider {
    /**
     * Singleton instance
     */
    @Getter
    private static final EventServiceProvider instance = new EventServiceProvider();
    /**
     * Private singleton constructor
     */
    private EventServiceProvider() { }
    /**
     * Liquibase Integration instance. Used to run SQL scripts
     */
    private final LiquibaseIntegration liquibase = LiquibaseIntegration.getInstance();
    /**
     * Provides the best version of the EventService implementation possible.
     * Prioritizes database-related instance over in-memory implementation
     * @return Instance of EventService
     */
    public EventService getEventService() {
        if(liquibase.runLiquibaseScripts(
                "db/changelog/db.changelog-master.xml")) {
            System.out.println("Connected to the database successfully.");
            return EventServiceDB.getInstance();
        }
        System.out.println("Could not connect to database. Resorting to in-memory saving.");
        return EventServiceInMemory.getInstance();
    }
}