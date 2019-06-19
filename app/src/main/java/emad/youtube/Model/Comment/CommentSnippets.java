package emad.youtube.Model.Comment;

public class CommentSnippets {
    private String videoId;
    private TopLevelComment topLevelComment;
    private boolean canReply;
    private int totalReplyCount;
    private boolean isPublic;

    public CommentSnippets() {
    }

    public CommentSnippets(String videoId, TopLevelComment topLevelComment, boolean canReply, int totalReplyCount, boolean isPublic) {
        this.videoId = videoId;
        this.topLevelComment = topLevelComment;
        this.canReply = canReply;
        this.totalReplyCount = totalReplyCount;
        this.isPublic = isPublic;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public TopLevelComment getTopLevelComment() {
        return topLevelComment;
    }

    public void setTopLevelComment(TopLevelComment topLevelComment) {
        this.topLevelComment = topLevelComment;
    }

    public boolean isCanReply() {
        return canReply;
    }

    public void setCanReply(boolean canReply) {
        this.canReply = canReply;
    }

    public int getTotalReplyCount() {
        return totalReplyCount;
    }

    public void setTotalReplyCount(int totalReplyCount) {
        this.totalReplyCount = totalReplyCount;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}
