package emad.youtube.Model.Video;

import java.util.ArrayList;

public class Thumbnails {
    DefaultThumb defaultThumbs;
    MeduimThum mediumThumbs;
    HeighThumb high;

    public Thumbnails() {
    }

    public Thumbnails(DefaultThumb defaultThumbs, MeduimThum mediumThumbs, HeighThumb high) {
        this.defaultThumbs = defaultThumbs;
        this.mediumThumbs = mediumThumbs;
        this.high = high;
    }

    public DefaultThumb getDefaultThumbs() {
        return defaultThumbs;
    }

    public void setDefaultThumbs(DefaultThumb defaultThumbs) {
        this.defaultThumbs = defaultThumbs;
    }

    public MeduimThum getMediumThumbs() {
        return mediumThumbs;
    }

    public void setMediumThumbs(MeduimThum mediumThumbs) {
        this.mediumThumbs = mediumThumbs;
    }

    public HeighThumb getHigh() {
        return high;
    }

    public void setHigh(HeighThumb high) {
        this.high = high;
    }
}
