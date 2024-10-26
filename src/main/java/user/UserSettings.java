package user;

public class UserSettings {
    private final String email;
    private final String name;

    public UserSettings(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public static UserSettings fromUser(User user) {
        return new UserSettings(user.getEmail(), user.getName());
    }

    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
}
