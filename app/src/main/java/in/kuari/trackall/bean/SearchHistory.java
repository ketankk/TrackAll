package in.kuari.trackall.bean;

/**
 * Created by root on 1/31/16.
 */
public class SearchHistory {
    private long id;
    private String name;
    private String trackId;
    private String courierID;

    public String getCourierID() {
        return courierID;
    }

    public void setCourierID(String courierID) {
        this.courierID = courierID;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTrackId() {
        return trackId;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    @Override
    public String toString() {
        return "SearchHistory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", trackId='" + trackId + '\'' +
                ", courierID='" + courierID + '\'' +
                '}';
    }
}
