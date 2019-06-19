package emad.youtube.Model.Daily;

public class User {
    private String id;
    private String full_name;
    private String profile_picture;
    private String username;

    public User() {
    }

    public User(String id, String full_name, String profile_picture, String username) {
        this.id = id;
        this.full_name = full_name;
        this.profile_picture = profile_picture;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
