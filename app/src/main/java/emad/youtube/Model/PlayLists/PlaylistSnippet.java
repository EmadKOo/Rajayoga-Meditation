package emad.youtube.Model.PlayLists;

import java.io.Serializable;

import emad.youtube.Model.Video.Thumbnails;
import emad.youtube.Model.Video.VideoResource;

public class PlaylistSnippet implements Serializable {
    private String publishedAt;
    private String channelId;
    private String title;
    private String description;
    private PlaylistThumbnails thumbnails;
    private String channelTitle;
    private Localized localized;

    public PlaylistSnippet() {
    }

    public PlaylistSnippet(String publishedAt, String channelId, String title, String description, PlaylistThumbnails thumbnails, String channelTitle, Localized localized) {
        this.publishedAt = publishedAt;
        this.channelId = channelId;
        this.title = title;
        this.description = description;
        this.thumbnails = thumbnails;
        this.channelTitle = channelTitle;
        this.localized = localized;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
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

    public PlaylistThumbnails getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(PlaylistThumbnails thumbnails) {
        this.thumbnails = thumbnails;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public Localized getLocalized() {
        return localized;
    }

    public void setLocalized(Localized localized) {
        this.localized = localized;
    }


}
