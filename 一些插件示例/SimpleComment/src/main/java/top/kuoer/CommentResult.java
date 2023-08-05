package top.kuoer;


import java.util.List;

public class CommentResult {

    private Comment comment;
    private List<ReplyComment> replyComments;

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public List<ReplyComment> getReplyComments() {
        return replyComments;
    }

    public void setReplyComments(List<ReplyComment> replyComments) {
        this.replyComments = replyComments;
    }

    @Override
    public String toString() {
        return "CommentResult{" +
                "comment=" + comment +
                ", replyComments=" + replyComments +
                '}';
    }

}
