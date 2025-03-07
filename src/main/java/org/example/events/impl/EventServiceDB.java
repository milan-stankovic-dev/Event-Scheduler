package org.example.events.impl;

import lombok.Cleanup;
import lombok.Getter;
import lombok.val;
import org.example.db.ConnectionFactory;
import org.example.db.DBCredentials;
import org.example.events.Event;
import org.example.events.EventService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Database implementation of the EventService interface
 */
public class EventServiceDB implements EventService {
    /**
     * Singleton instance
     */
    @Getter
    private static final EventServiceDB instance = new EventServiceDB();
    /**
     * Private singleton constructor
     */
    private EventServiceDB() { }
    /**
     * Connection factory instance
     */
    private final ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
    /**
     * Database credentials instance
     */
    private final DBCredentials credentials = DBCredentials.getInstance();
    /**
     * Database error message
     */
    private final String DB_ERROR_MESSAGE = "Database error occurred. Could not complete task. Error: ";

    @Override
    public boolean addEvent(Event newEvent) {
        if(newEvent == null) { return false; }

        try {
        @Cleanup
        val conn = connectionFactory.establishDBConnection(
                credentials.getURL(),
                credentials.getUSER(),
                credentials.getPASS());

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
            System.out.println(DB_ERROR_MESSAGE + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public Optional<Event> getByStart(LocalDateTime start) {
        if(start == null) { return Optional.empty(); }

        try {
            @Cleanup
            val conn = connectionFactory.establishDBConnection(
                credentials.getURL(),
                credentials.getUSER(),
                credentials.getPASS());

            final String sql = """
                    SELECT *
                    FROM event
                    WHERE start_time = ?
                    """;

            @Cleanup
            final PreparedStatement statement =
                    conn.prepareStatement(sql);
            statement.setTimestamp(1, Timestamp.valueOf(start));

            final ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return Optional.of(new Event(
                        rs.getTimestamp("start_time").toLocalDateTime(),
                        rs.getTimestamp("end_time").toLocalDateTime(),
                        rs.getString("event_name"),
                        rs.getString("event_description")
                ));
            }

            return Optional.empty();

        } catch (SQLException ex) {
            System.out.println(DB_ERROR_MESSAGE + ex.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Set<Event> getByName(String name) {
        if(name == null || name.length() < 2) { return Set.of(); }

        try {
            @Cleanup
            val conn = connectionFactory.establishDBConnection(
                    credentials.getURL(),
                    credentials.getUSER(),
                    credentials.getPASS());

            final String sql = """
                    SELECT *
                    FROM event
                    WHERE event_name = ?
                    """;

            @Cleanup
            final PreparedStatement statement =
                    conn.prepareStatement(sql);
            statement.setString(1, name.trim());

            final ResultSet rs = statement.executeQuery();

            val resultAsASet = new HashSet<Event>();
            while (rs.next()) {
                val event = new Event(
                        rs.getTimestamp("start_time").toLocalDateTime(),
                        rs.getTimestamp("end_time").toLocalDateTime(),
                        rs.getString("event_name"),
                        rs.getString("event_description")
                );
                resultAsASet.add(event);
            }

            return resultAsASet;

        } catch (SQLException ex) {
            System.out.println(DB_ERROR_MESSAGE + ex.getMessage());
            return Set.of();
        }
    }

    @Override
    public boolean removeEvent(LocalDateTime start) {
        if(start == null) { return false; }

        try {
            @Cleanup
            val conn = connectionFactory.establishDBConnection(
                    credentials.getURL(),
                    credentials.getUSER(),
                    credentials.getPASS());

            final String sql = """
                    DELETE
                    FROM event
                    WHERE start_time = ?
                    """;

            @Cleanup
            final PreparedStatement statement =
                    conn.prepareStatement(sql);
            statement.setTimestamp(1, Timestamp.valueOf(start));

            final int rowsAffected = statement.executeUpdate();

            return rowsAffected >= 1;

        } catch (SQLException ex) {
            System.out.println(DB_ERROR_MESSAGE + ex.getMessage());
            return false;
        }
    }

    @Override
    public Set<Event> getAllEvents() {
        try {
            @Cleanup
            val conn = connectionFactory.establishDBConnection(
                    credentials.getURL(),
                    credentials.getUSER(),
                    credentials.getPASS());

            final String sql = """
                    SELECT *
                    FROM event
                    ORDER BY start_time;
                    """;

            @Cleanup
            final PreparedStatement statement =
                    conn.prepareStatement(sql);

            final ResultSet rs = statement.executeQuery();

            val resultAsASet = new HashSet<Event>();
            while(rs.next()) {
                val event = new Event(
                        rs.getTimestamp("start_time").toLocalDateTime(),
                        rs.getTimestamp("end_time").toLocalDateTime(),
                        rs.getString("event_name"),
                        rs.getString("event_description")
                );
                resultAsASet.add(event);
            }

            return resultAsASet;
        } catch (SQLException ex) {
            System.out.println(DB_ERROR_MESSAGE + ex.getMessage());
            return Set.of();
        }
    }
}
