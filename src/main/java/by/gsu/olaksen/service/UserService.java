package by.gsu.olaksen.service;

import by.gsu.olaksen.config.AppConfig;
import by.gsu.olaksen.model.User;

public class UserService {
    public User authenticate(String username, String password) {
        return AppConfig.INSTANCE.getUsers().stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }
}