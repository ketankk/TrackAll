package in.kuari.trackall.entity;

/**
 * Created by sultan_mirza on 1/20/16.
 */
public class CourierEntity {
    private long courierID;
    private String courierName;
    private String  courierImagePath;
    private String  courierWebsite;

    public String getCourierTrackLink() {
        return courierTrackLink;
    }

    public void setCourierTrackLink(String courierTrackLink) {
        this.courierTrackLink = courierTrackLink;
    }

    public long getCourierID() {
        return courierID;
    }

    public void setCourierID(long courierID) {
        this.courierID = courierID;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public String getCourierImagePath() {
        return courierImagePath;
    }

    public void setCourierImagePath(String courierImagePath) {
        this.courierImagePath = courierImagePath;
    }

    public String getCourierWebsite() {
        return courierWebsite;
    }

    public void setCourierWebsite(String courierWebsite) {
        this.courierWebsite = courierWebsite;
    }

    private String  courierTrackLink;

}
