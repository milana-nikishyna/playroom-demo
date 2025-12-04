package by.gsu.olaksen.config;

import by.gsu.olaksen.model.Role;
import by.gsu.olaksen.model.User;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public enum AppConfig {
    INSTANCE;
    private final Map<String, String> dbProperties;
    private final List<User> users;

    AppConfig() {
        try (var dbConfig = getClass().getClassLoader().getResourceAsStream("app.yml");
             var userConfig = getClass().getClassLoader().getResourceAsStream("users.yml")) {
            Yaml dbYaml = new Yaml();
            Yaml usersYaml = new Yaml();

            List<Map<String, String>> rawUsers = usersYaml.load(userConfig);
            this.users = rawUsers.stream()
                    .map(map -> new User(
                            map.get("username"),
                            map.get("password"),
                            Role.valueOf(map.get("role"))
                    )).toList();

            this.dbProperties = dbYaml.load(dbConfig);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config files", e);
        }
    }

    public String getDbProperty(String key) {
        return dbProperties.get(key);
    }

    public List<User> getUsers() {
        return users;
    }

    public static AppConfig getInstance() {
        return INSTANCE;
    }

}
