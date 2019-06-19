package emad.youtube.Model.VideoList;

import java.io.Serializable;

import emad.youtube.Model.Video.VideoSnippets;

public class ItemsListData implements Serializable {
    private String kind;
    private String etag;
    private String id;
    private VideoListSnippets snippet;

    public ItemsListData() {
    }

    public ItemsListData(String kind, String etag, String id, VideoListSnippets snippet) {
        this.kind = kind;
        this.etag = etag;
        this.id = id;
        this.snippet = snippet;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public VideoListSnippets getSnippet() {
        return snippet;
    }

    public void setSnippet(VideoListSnippets snippet) {
        this.snippet = snippet;
    }
}
