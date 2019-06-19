package emad.youtube.Model.Latest;

import java.io.Serializable;

public class Latest implements Serializable {
    private String title;
    private String description;
    private String publishedAt;
    private String videoId;
    private String url;
    private String channelTitle;


    public Latest() {
    }

    public Latest(String title, String description, String publishedAt, String videoId, String url, String channelTitle) {
        this.title = title;
        this.description = description;
        this.publishedAt = publishedAt;
        this.videoId = videoId;
        this.url = url;
        this.channelTitle = channelTitle;
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

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
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
}
