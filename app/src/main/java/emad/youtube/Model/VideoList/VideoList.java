package emad.youtube.Model.VideoList;

import emad.youtube.Model.Video.ItemData;
import emad.youtube.Model.Video.PageInfo;

public class VideoList {
    private String kind;
    private String etag;
    private String nextPageToken;
    private PageInfo pageInfo;
    private ItemsListData[] items ;

    public VideoList() {
    }

    public VideoList(String kind, String etag, String nextPageToken, PageInfo pageInfo, ItemsListData[] items) {
        this.kind = kind;
        this.etag = etag;
        this.nextPageToken = nextPageToken;
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

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public ItemsListData[] getItems() {
        return items;
    }

    public void setItems(ItemsListData[] items) {
        this.items = items;
    }
}
