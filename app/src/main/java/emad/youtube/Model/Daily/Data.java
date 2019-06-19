package emad.youtube.Model.Daily;

public class Data {
    private String id;
    private User user;
    private Images images;
    private String created_time;
    private Caption caption;
    private boolean user_has_liked;
    private Likes likes;
    private String[] tags;
    private String filter;
    private Comments comments;
    private String type;
    private String link;
    private String location;
    private String attribution;
    private String[] users_in_photo;


    public Data() {
    }

    public Data(String id, User user, Images images, String created_time, Caption caption, boolean user_has_liked, Likes likes, String[] tags, String filter, Comments comments, String type, String link, String location, String attribution, String[] users_in_photo) {
        this.id = id;
        this.user = user;
        this.images = images;
        this.created_time = created_time;
        this.caption = caption;
        this.user_has_liked = user_has_liked;
        this.likes = likes;
        this.tags = tags;
        this.filter = filter;
        this.comments = comments;
        this.type = type;
        this.link = link;
        this.location = location;
        this.attribution = attribution;
        this.users_in_photo = users_in_photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public Caption getCaption() {
        return caption;
    }

    public void setCaption(Caption caption) {
        this.caption = caption;
    }

    public boolean isUser_has_liked() {
        return user_has_liked;
    }

    public void setUser_has_liked(boolean user_has_liked) {
        this.user_has_liked = user_has_liked;
    }

    public Likes getLikes() {
        return likes;
    }

    public void setLikes(Likes likes) {
        this.likes = likes;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Comments getComments() {
        return comments;
    }

    public void setComments(Comments comments) {
        this.comments = comments;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAttribution() {
        return attribution;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

    public String[] getUsers_in_photo() {
        return users_in_photo;
    }

    public void setUsers_in_photo(String[] users_in_photo) {
        this.users_in_photo = users_in_photo;
    }
}
