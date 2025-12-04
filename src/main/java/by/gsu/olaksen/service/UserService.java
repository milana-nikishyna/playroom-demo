package by.gsu.olaksen.service;

import by.gsu.olaksen.config.AppConfig;
import by.gsu.olaksen.model.User;

public class UserService {
    public User authenticate(String username, String password) {
        return AppConfig.getInstance().getUsers().stream()
                .filter(u -> u.username().equals(username) && u.password().equals(password))
                .findFirst()
                .orElse(null);
    }
}