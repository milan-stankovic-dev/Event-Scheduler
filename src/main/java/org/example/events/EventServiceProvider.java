package org.example.events;

import lombok.Getter;
import org.example.events.impl.EventServiceDB;
import org.example.events.impl.EventServiceInMemory;
import org.example.liquibase.LiquibaseIntegration;

public class EventServiceProvider {
    @Getter
    private static final EventServiceProvider instance = new EventServiceProvider();
    private EventServiceProvider() { }
    private final LiquibaseIntegration liquibase = LiquibaseIntegration.getInstance();

    public EventService getEventService() {
        if(liquibase.runLiquibaseScripts(
                "db/changelog/db.changelog-master.xml")) {
            return EventServiceDB.getInstance();
        }
        System.out.println("Could not connect to database. Resorting to in-memory saving.");
        return EventServiceInMemory.getInstance();
    }
}