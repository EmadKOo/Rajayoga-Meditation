package emad.youtube.Model.VideoList;

import java.io.Serializable;

import emad.youtube.Model.Video.Thumbnails;
import emad.youtube.Model.Video.VideoResource;

public class VideoListSnippets implements Serializable {
    private String publishedAt;
    private String channelId;
    private String title;
    private String description;
    private VideoListThumbnail thumbnails;
    private String channelTitle;
    private String playlistId;
    private int position;
    private VideoResource resourceId;

    public VideoListSnippets() {
    }

    public VideoListSnippets(String publishedAt, String channelId, String title, String description, VideoListThumbnail thumbnails, String channelTitle, String playlistId, int position, VideoResource resourceId) {
        this.publishedAt = publishedAt;
        this.channelId = channelId;
        this.title = title;
        this.description = description;
        this.thumbnails = thumbnails;
        this.channelTitle = channelTitle;
        this.playlistId = playlistId;
        this.position = position;
        this.resourceId = resourceId;
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

    public VideoListThumbnail getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(VideoListThumbnail thumbnails) {
        this.thumbnails = thumbnails;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public VideoResource getResourceId() {
        return resourceId;
    }

    public void setResourceId(VideoResource resourceId) {
        this.resourceId = resourceId;
    }
}
