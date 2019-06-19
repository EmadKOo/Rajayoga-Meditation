package emad.youtube.Model.Comment;

public class TopLevelComment {
    private String kind;
    private String etag;
    private String id;
    private CoreSnippet snippet;

    public TopLevelComment() {
    }

    public TopLevelComment(String kind, String etag, String id, CoreSnippet snippet) {
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

    public CoreSnippet getSnippet() {
        return snippet;
    }

    public void setSnippet(CoreSnippet snippet) {
        this.snippet = snippet;
    }
}
