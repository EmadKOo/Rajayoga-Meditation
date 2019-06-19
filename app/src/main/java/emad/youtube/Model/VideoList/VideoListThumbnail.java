package emad.youtube.Model.VideoList;

import java.io.Serializable;

import emad.youtube.Model.Video.DefaultThumb;
import emad.youtube.Model.Video.HeighThumb;
import emad.youtube.Model.Video.MeduimThum;

public class VideoListThumbnail implements Serializable {
    DefaultThumb defaultThumbs;
    MeduimThum medium;
    HeighThumb high;
    HeighThumb standard;

    public VideoListThumbnail() {
    }

    public VideoListThumbnail(DefaultThumb defaultThumbs, MeduimThum medium, HeighThumb high, HeighThumb standard) {
        this.defaultThumbs = defaultThumbs;
        this.medium = medium;
        this.high = high;
        this.standard = standard;
    }

    public DefaultThumb getDefaultThumbs() {
        return defaultThumbs;
    }

    public void setDefaultThumbs(DefaultThumb defaultThumbs) {
        this.defaultThumbs = defaultThumbs;
    }

    public MeduimThum getMedium() {
        return medium;
    }

    public void setMedium(MeduimThum medium) {
        this.medium = medium;
    }

    public HeighThumb getHigh() {
        return high;
    }

    public void setHigh(HeighThumb high) {
        this.high = high;
    }

    public HeighThumb getStandard() {
        return standard;
    }

    public void setStandard(HeighThumb standard) {
        this.standard = standard;
    }
}
