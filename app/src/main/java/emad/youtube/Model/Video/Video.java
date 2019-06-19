package emad.youtube.Model.Video;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Video {

    private String kind;
    private String etag;
    private String nextPageToken;
    private PageInfo pageInfo;
    private ItemData[] items ;


    public Video() {
    }

    public Video(String kind, String etag, String nextPageToken, PageInfo pageInfo, ItemData[] items) {
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

    public ItemData[] getItems() {
        return items;
    }

    public void setItems(ItemData[] items) {
        this.items = items;
    }
}
