package in.kuari.trackall.bean;

import java.util.Comparator;

/**
 * Created by root on 1/29/16.
 */
public class FlightBean {
    private long flightID;
    private String flightName;
    private String flightWebsite;
    private String flightLogo;

    public String getFlightWebsite() {
        return flightWebsite;
    }

    public void setFlightWebsite(String flightWebsite) {
        this.flightWebsite = flightWebsite;
    }

    public long getFlightID() {
        return flightID;
    }

    public void setFlightID(long flightID) {
        this.flightID = flightID;
    }

    public String getFlightName() {
        return flightName;
    }

    public void setFlightName(String flightName) {
        this.flightName = flightName;
    }

    public String getFlightLogo() {
        return flightLogo;
    }

    public void setFlightLogo(String flightLogo) {
        this.flightLogo = flightLogo;
    }

    //For sorting the list according to name of company
    public static Comparator<FlightBean> flightNameComp=new Comparator<FlightBean>() {
        @Override
        public int compare(FlightBean lhs, FlightBean rhs) {
            String name1 = lhs.getFlightName().toUpperCase();
            String name2 = rhs.getFlightName().toUpperCase();

            return name1.compareTo(name2);
        }
    };
}
