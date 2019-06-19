package emad.youtube.Model.Daily;

public class Daily {
    private Pagination pagination;
    private Data[] data;
    private Meta meta;

    public Daily() {
    }

    public Daily(Pagination pagination, Data[] data, Meta meta) {
        this.pagination = pagination;
        this.data = data;
        this.meta = meta;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public Data[] getData() {
        return data;
    }

    public void setData(Data[] data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
