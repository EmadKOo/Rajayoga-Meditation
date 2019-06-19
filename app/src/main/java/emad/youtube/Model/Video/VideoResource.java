package emad.youtube.Model.Video;

import java.io.Serializable;

public class VideoResource implements Serializable {
    private String kind;
    private String videoId;

    public VideoResource() {
    }

    public VideoResource(String kind, String videoId) {
        this.kind = kind;
        this.videoId = videoId;
    }


    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
