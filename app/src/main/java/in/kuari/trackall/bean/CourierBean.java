package in.kuari.trackall.bean;

import java.util.Comparator;

/**
 * Created by sultan_mirza on 1/20/16.
 */
public class CourierBean {
    private long courierID;
    private String courierName;
    private String  courierImagePath;
    private String  courierWebsite;
    private String  courierTrackLink;

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

    @Override
    public String toString() {
        return "CourierBean{" +
                "courierID=" + courierID +
                ", courierName='" + courierName + '\'' +
                ", courierImagePath='" + courierImagePath + '\'' +
                ", courierWebsite='" + courierWebsite + '\'' +
                ", courierTrackLink='" + courierTrackLink + '\'' +
                '}';
    }
    //For sorting the list according to name of company
    public static Comparator<CourierBean> courierNameComp=new Comparator<CourierBean>() {
        @Override
        public int compare(CourierBean lhs, CourierBean rhs) {
            String name1 = lhs.getCourierName().toUpperCase();
            String name2 = rhs.getCourierName().toUpperCase();

            return name1.compareTo(name2);
        }
    };




}
