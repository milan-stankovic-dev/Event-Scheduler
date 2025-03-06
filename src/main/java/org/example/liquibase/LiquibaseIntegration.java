package org.example.liquibase;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.core.PostgresDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.Cleanup;
import lombok.Getter;
import lombok.val;
import org.example.db.ConnectionFactory;
import org.example.db.DBCredentials;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LiquibaseIntegration {
    @Getter
    private static final LiquibaseIntegration instance = new LiquibaseIntegration();
    private LiquibaseIntegration() { }
    private final DBCredentials credentials = DBCredentials.getInstance();

    public boolean runLiquibaseScripts(String changelogPath) {
        final ConnectionFactory connFactory = ConnectionFactory.getInstance();

        final Logger liquibaseLogger = Logger.getLogger("liquibase");
        liquibaseLogger.setLevel(Level.OFF);

        final PrintStream originalOut = System.out;
        final PrintStream originalErr = System.err;

        val byteArrayOutputStream = new ByteArrayOutputStream();
        val printStream = new PrintStream(byteArrayOutputStream);

        System.setOut(printStream);
        System.setErr(printStream);

        try {
            @Cleanup
            val connection = connFactory.establishDBConnection(
                    credentials.getURL(),
                    credentials.getUSER(),
                    credentials.getPASS());
            @Cleanup
            val liquibaseConnection = new JdbcConnection(connection);

            @Cleanup
            val database = new PostgresDatabase();
            database.setConnection(liquibaseConnection);
            val liquibase = new Liquibase(changelogPath,
                    new ClassLoaderResourceAccessor(), database);

            liquibase.update(new Contexts());

            return true;
        } catch (Throwable t) {
            return false;
        } finally {
            System.setErr(originalErr);
            System.setOut(originalOut);
        }
    }
}
