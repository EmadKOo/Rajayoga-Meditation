package emad.youtube.Model.PlayLists;

import java.io.Serializable;
import java.util.ArrayList;

import emad.youtube.Model.Video.DefaultThumb;
import emad.youtube.Model.Video.HeighThumb;
import emad.youtube.Model.Video.MeduimThum;

public class PlaylistThumbnails implements Serializable {
    DefaultThumb defaultThumbs;
    MeduimThum medium;
    HeighThumb high;

    public PlaylistThumbnails() {
    }

    public PlaylistThumbnails(DefaultThumb defaultThumbs, MeduimThum medium, HeighThumb high) {
        this.defaultThumbs = defaultThumbs;
        this.medium = medium;
        this.high = high;
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
}
