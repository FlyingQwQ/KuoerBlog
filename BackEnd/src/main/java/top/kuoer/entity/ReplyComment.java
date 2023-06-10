package top.kuoer.entity;

import org.springframework.lang.Nullable;

import java.util.List;

public class ReplyComment {

    @Nullable
    private int id;
    private String name;
    private String value;
    private String label;
    @Nullable
    private long date;
    private int replyid;
    private List<ReplyComment> replyComments;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getReplyid() {
        return replyid;
    }

    public void setReplyid(int replyid) {
        this.replyid = replyid;
    }

    public List<ReplyComment> getReplyComments() {
        return replyComments;
    }

    public void setReplyComments(List<ReplyComment> replyComments) {
        this.replyComments = replyComments;
    }

    @Override
    public String toString() {
        return "ReplyComment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", label='" + label + '\'' +
                ", date=" + date +
                ", replyid=" + replyid +
                ", replyComments=" + replyComments +
                '}';
    }
}
