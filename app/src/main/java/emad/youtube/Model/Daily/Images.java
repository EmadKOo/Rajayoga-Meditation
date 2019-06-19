package emad.youtube.Model.Daily;

public class Images {
    private Thumbnail thumbnail;
    private Thumbnail low_resolution;
    private Thumbnail standard_resolution;


    public Images() {
    }

    public Images(Thumbnail thumbnail, Thumbnail low_resolution, Thumbnail standard_resolution) {
        this.thumbnail = thumbnail;
        this.low_resolution = low_resolution;
        this.standard_resolution = standard_resolution;

    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Thumbnail getLow_resolution() {
        return low_resolution;
    }

    public void setLow_resolution(Thumbnail low_resolution) {
        this.low_resolution = low_resolution;
    }

    public Thumbnail getStandard_resolution() {
        return standard_resolution;
    }

    public void setStandard_resolution(Thumbnail standard_resolution) {
        this.standard_resolution = standard_resolution;
    }

}
