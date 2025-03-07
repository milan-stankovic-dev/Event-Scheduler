package org.example.db;

import lombok.Getter;

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
     * Extracted config values, in the form of a map.
     */
    private final Map<String,String> CONFIG = DBConfig.getInstance().getDotEnvVars();
    /**
     * Url constant
     */
    @Getter
    private final String URL = CONFIG.get("url");
    /**
     * Username constant
     */
    @Getter
    private final String USER = CONFIG.get("user");
    /**
     * Password constant
     */
    @Getter
    private final String PASS = CONFIG.get("pass");
}
