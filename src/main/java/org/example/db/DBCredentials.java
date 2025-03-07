package org.example.db;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import lombok.val;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that houses all connection based credentials, extracted from
 * environment variables
 */
public class DBCredentials {
    /**
     * Singleton instance
     */
    @Getter
    private static final DBCredentials instance = new DBCredentials();
    /**
     * Private singleton constructor
     */
    private DBCredentials() { }
    /**
     * Url constant
     */
    @Getter
    private final String URL = getDotEnvVars().get("url");
    /**
     * Username constant
     */
    @Getter
    private final String USER = getDotEnvVars().get("user");
    /**
     * Password constant
     */
    @Getter
    private final String PASS = getDotEnvVars().get("pass");
    /**
     * Extracts environment variables for db connection
     * @return Map of three entries. The keys include:
     *  * url - database url
     *  * user - database username
     *  * pass - database password
     */
    private Map<String, String> getDotEnvVars() {
        val dotEnv = Dotenv.load();

        return new HashMap<>() {{
            put("url", dotEnv.get("DB_URL"));
            put("user", dotEnv.get("DB_USER"));
            put("pass", dotEnv.get("DB_PASS"));
        }};
    }
}
