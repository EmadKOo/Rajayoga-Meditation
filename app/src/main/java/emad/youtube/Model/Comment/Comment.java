package emad.youtube.Model.Comment;

import emad.youtube.Model.Video.PageInfo;

public class Comment {
    private String kind;
    private String etag;
    private PageInfo pageInfo;
    private CommentItem[] items;

    public Comment() {
    }

    public Comment(String kind, String etag, PageInfo pageInfo, CommentItem[] items) {
        this.kind = kind;
        this.etag = etag;
        this.pageInfo = pageInfo;
        this.items = items;
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

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public CommentItem[] getItems() {
        return items;
    }

    public void setItems(CommentItem[] items) {
        this.items = items;
    }
}
