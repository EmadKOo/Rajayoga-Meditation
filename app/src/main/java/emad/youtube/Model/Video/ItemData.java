package emad.youtube.Model.Video;

import java.util.ArrayList;

public class ItemData {
    private String kind;
    private String etag;
    private String id;
    private VideoSnippets snippet;


    public ItemData() {
    }

    public ItemData(String kind, String etag, String id, VideoSnippets snippet) {
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

    public VideoSnippets getVideoSnippets() {
        return snippet;
    }

    public void setVideoSnippets(VideoSnippets snippet) {
        this.snippet = snippet;
    }
}
