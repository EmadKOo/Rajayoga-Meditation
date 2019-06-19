package emad.youtube.Model.Favourite;

import java.io.Serializable;

public class Favourite implements Serializable {
    private String videoID;
    private String title;
    private String description;
    private String publishedAt;
    private String url;
    private String channelTitle;
    private String reaction;

    public Favourite() {
    }

    public Favourite(String videoID, String title, String description, String publishedAt, String url, String channelTitle, String reaction) {
        this.videoID = videoID;
        this.title = title;
        this.description = description;
        this.publishedAt = publishedAt;
        this.url = url;
        this.channelTitle = channelTitle;
        this.reaction = reaction;
    }

    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }
}
