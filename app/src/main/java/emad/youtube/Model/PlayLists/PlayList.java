package emad.youtube.Model.PlayLists;

import emad.youtube.Model.Video.ItemData;
import emad.youtube.Model.Video.PageInfo;

public class PlayList {
    private String kind;
    private String etag;
    private PageInfo pageInfo;
    private PlayListItems[] items ;

    public PlayList() {
    }

    public PlayList(String kind, String etag, PageInfo pageInfo, PlayListItems[] items) {
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

    public PlayListItems[] getItems() {
        return items;
    }

    public void setItems(PlayListItems[] items) {
        this.items = items;
    }
}
