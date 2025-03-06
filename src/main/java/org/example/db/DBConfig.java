package org.example.db;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import lombok.val;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for fetching db credentials from environment variables.
 */
public class DBConfig {
    /**
     * Singleton instance
     */
    @Getter
    private static final DBConfig instance = new DBConfig();
    /**
     * Private singleton constructor
     */
    private DBConfig() { }

    /**
     * Extracts environment variables for db connection
     * @return Map of three entries. The keys include:
     *  * url - database url
     *  * user - database username
     *  * pass - database password
     */
    public Map<String, String> getDotEnvVars() {
        val dotEnv = Dotenv.load();

        return new HashMap<>() {{
            put("url", dotEnv.get("DB_URL"));
            put("user", dotEnv.get("DB_USER"));
            put("pass", dotEnv.get("DB_PASS"));
        }};
    }
}
