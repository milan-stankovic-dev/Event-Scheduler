package org.example.liquibase;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.DatabaseConnection;
import liquibase.database.core.PostgresDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.Cleanup;
import lombok.Getter;
import lombok.val;
import org.example.db.ConnectionFactory;
import org.example.db.DBConfig;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LiquibaseIntegration {
    @Getter
    private static final LiquibaseIntegration instance = new LiquibaseIntegration();
    private LiquibaseIntegration() { }

    public boolean runLiquibaseScripts(String changelogPath) {
        final DBConfig dotenvConfig = DBConfig.getInstance();
        final ConnectionFactory connFactory = ConnectionFactory.getInstance();
        final Map<String, String> dotenvVars = dotenvConfig.getDotEnvVars();

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
                    dotenvVars.get("url"),
                    dotenvVars.get("user"),
                    dotenvVars.get("pass"));
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
