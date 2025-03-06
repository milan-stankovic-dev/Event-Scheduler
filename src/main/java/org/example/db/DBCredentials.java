package org.example.db;

import lombok.Getter;

import java.util.Map;

public class DBCredentials {
    @Getter
    private static final DBCredentials instance = new DBCredentials();
    private DBCredentials() { }
    private final Map<String,String> CONFIG = DBConfig.getInstance().getDotEnvVars();

    @Getter
    private final String URL = CONFIG.get("url");
    @Getter
    private final String USER = CONFIG.get("user");
    @Getter
    private final String PASS = CONFIG.get("pass");
}
