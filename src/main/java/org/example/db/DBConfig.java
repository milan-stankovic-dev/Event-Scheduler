package org.example.db;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import lombok.val;

import java.util.HashMap;
import java.util.Map;

public class DBConfig {
    @Getter
    private static final DBConfig instance = new DBConfig();
    private DBConfig() { }

    public Map<String, String> getDotEnvVars() {
        val dotEnv = Dotenv.load();

        return new HashMap<>() {{
            put("url", dotEnv.get("DB_URL"));
            put("user", dotEnv.get("DB_USER"));
            put("pass", dotEnv.get("DB_PASS"));
        }};
    }
}
