package emad.youtube.Model.Comment;

public class CommentItem {

    private String kind;
    private String etag;
    private String id;
    private CommentSnippets snippet;

    public CommentItem() {
    }

    public CommentItem(String kind, String etag, String id, CommentSnippets snippet) {
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

    public CommentSnippets getSnippet() {
        return snippet;
    }

    public void setSnippet(CommentSnippets snippet) {
        this.snippet = snippet;
    }
}
