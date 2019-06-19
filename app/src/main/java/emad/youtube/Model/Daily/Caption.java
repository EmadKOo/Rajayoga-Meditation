package emad.youtube.Model.Daily;

public class Caption {
    private String id;
    private String text;
    private String created_time;
    private From from;

    public Caption() {
    }

    public Caption(String id, String text, String created_time, From from) {
        this.id = id;
        this.text = text;
        this.created_time = created_time;
        this.from = from;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public From getFrom() {
        return from;
    }

    public void setFrom(From from) {
        this.from = from;
    }
}
