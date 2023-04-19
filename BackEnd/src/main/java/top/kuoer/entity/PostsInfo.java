package top.kuoer.entity;

public class PostsInfo {

    private int id;
    private String title;
    private long date;
    private int year;
    private int label;
    private String labelName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    @Override
    public String toString() {
        return "PostsInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", year=" + year +
                ", label=" + label +
                ", labelName='" + labelName + '\'' +
                '}';
    }
}
