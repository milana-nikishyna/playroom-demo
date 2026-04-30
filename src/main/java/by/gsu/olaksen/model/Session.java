package by.gsu.olaksen.model;

public enum Session {
    INSTANCE;
    private User currentUser;

    public void setUser(User user) {
        currentUser = user;
    }

    public User getUser() {
        return currentUser;
    }

    public void clear() {
        currentUser = null;
    }

    public static Session getInstance(){
        return INSTANCE;
    }
}
