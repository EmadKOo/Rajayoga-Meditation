package emad.youtube.Model.PlayLists;

import java.io.Serializable;

public class PlayListItems implements Serializable {
    private String kind;
    private String etag;
    private String id;
    private PlaylistSnippet snippet;

    public PlayListItems() {
    }

    public PlayListItems(String kind, String etag, String id, PlaylistSnippet snippet) {
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

    public PlaylistSnippet getSnippet() {
        return snippet;
    }

    public void setSnippet(PlaylistSnippet snippet) {
        this.snippet = snippet;
    }
}
