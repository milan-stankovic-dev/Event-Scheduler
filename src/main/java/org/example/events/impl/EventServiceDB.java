package org.example.events.impl;

import lombok.Cleanup;
import lombok.Getter;
import lombok.val;
import org.example.db.ConnectionFactory;
import org.example.db.DBCredentials;
import org.example.events.Event;
import org.example.events.EventService;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public class EventServiceDB implements EventService {
    @Getter
    private static final EventServiceDB instance = new EventServiceDB();
    private EventServiceDB() { }

    private final ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
    private final DBCredentials credentials = DBCredentials.getInstance();

    @Override
    public boolean addEvent(Event newEvent) {
        if(newEvent == null) {
            return false;
        }

        try {
        @Cleanup
        val conn = connectionFactory.establishDBConnection(
                credentials.getURL(),
                credentials.getUSER(),
                credentials.getPASS()
        );

        final String sql = """
                            INSERT INTO event(event_name,
                            event_description, start_time, end_time)
                            VALUES (?,?,?,?);
                            """;
            @Cleanup
            final PreparedStatement statement =
                    conn.prepareStatement(sql);
            statement.setString(1, newEvent.name());
            statement.setString(2, newEvent.description());
            statement.setTimestamp(3,
                    Timestamp.valueOf(newEvent.start()));
            statement.setTimestamp(4,
                    Timestamp.valueOf(newEvent.end()));
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.printf("Database error occurred. Could not save. Error: %s\n", e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public Optional<Event> getByStart(LocalDateTime start) {
        return Optional.empty();
    }

    @Override
    public Set<Event> getByName(String name) {
        return Set.of();
    }

    @Override
    public boolean removeEvent(LocalDateTime start) {
        return false;
    }

    @Override
    public void displayAllEvents() {

    }
}
