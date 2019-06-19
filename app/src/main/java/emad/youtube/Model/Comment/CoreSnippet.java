package emad.youtube.Model.Comment;

public class CoreSnippet {

    private String authorDisplayName;
    private String authorProfileImageUrl;
    private String authorChannelUrl;
    private AuthorChannelID authorChannelId;
    private String videoId;
    private String textDisplay;
    private String textOriginal;
    private boolean canRate;
    private String viewerRating;
    private int likeCount;
    private String publishedAt;
    private String updatedAt;

    public CoreSnippet() {
    }

    public CoreSnippet(String authorDisplayName, String authorProfileImageUrl, String authorChannelUrl, AuthorChannelID authorChannelId, String videoId, String textDisplay, String textOriginal, boolean canRate, String viewerRating, int likeCount, String publishedAt, String updatedAt) {
        this.authorDisplayName = authorDisplayName;
        this.authorProfileImageUrl = authorProfileImageUrl;
        this.authorChannelUrl = authorChannelUrl;
        this.authorChannelId = authorChannelId;
        this.videoId = videoId;
        this.textDisplay = textDisplay;
        this.textOriginal = textOriginal;
        this.canRate = canRate;
        this.viewerRating = viewerRating;
        this.likeCount = likeCount;
        this.publishedAt = publishedAt;
        this.updatedAt = updatedAt;
    }

    public String getAuthorDisplayName() {
        return authorDisplayName;
    }

    public void setAuthorDisplayName(String authorDisplayName) {
        this.authorDisplayName = authorDisplayName;
    }

    public String getAuthorProfileImageUrl() {
        return authorProfileImageUrl;
    }

    public void setAuthorProfileImageUrl(String authorProfileImageUrl) {
        this.authorProfileImageUrl = authorProfileImageUrl;
    }

    public String getAuthorChannelUrl() {
        return authorChannelUrl;
    }

    public void setAuthorChannelUrl(String authorChannelUrl) {
        this.authorChannelUrl = authorChannelUrl;
    }

    public AuthorChannelID getAuthorChannelId() {
        return authorChannelId;
    }

    public void setAuthorChannelId(AuthorChannelID authorChannelId) {
        this.authorChannelId = authorChannelId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getTextDisplay() {
        return textDisplay;
    }

    public void setTextDisplay(String textDisplay) {
        this.textDisplay = textDisplay;
    }

    public String getTextOriginal() {
        return textOriginal;
    }

    public void setTextOriginal(String textOriginal) {
        this.textOriginal = textOriginal;
    }

    public boolean isCanRate() {
        return canRate;
    }

    public void setCanRate(boolean canRate) {
        this.canRate = canRate;
    }

    public String getViewerRating() {
        return viewerRating;
    }

    public void setViewerRating(String viewerRating) {
        this.viewerRating = viewerRating;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
