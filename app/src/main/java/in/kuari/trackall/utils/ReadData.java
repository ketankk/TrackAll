package in.kuari.trackall.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import in.kuari.trackall.R;
import in.kuari.trackall.bean.CourierBean;
import in.kuari.trackall.bean.FlightBean;

/**
 * Created by root on 1/24/16.
 */
public class ReadData {
   private Context context;
    private String str;
    private InputStream is;

    private List<CourierBean> couriers;

    public ReadData(Context context){
     this.context=context;
    }
    public List<CourierBean> getAllCourier(){
        couriers=new ArrayList();
        try {
             is=context.getResources().openRawResource(R.raw.courierdetail);

            BufferedReader reader=new BufferedReader(new InputStreamReader(is));
int count=0;
            while((str=reader.readLine())!=null){//Log.d("reader",str);
                String[] rows=str.split(",");

            CourierBean courier=new CourierBean();
                courier.setCourierID(Long.parseLong(rows[0]));
                courier.setCourierName(rows[1]);
                courier.setCourierTrackLink(rows[2]);
                courier.setCourierWebsite(rows[3]);
                courier.setCourierImagePath(rows[4]);
                couriers.add(courier);

            } is.close();
        }
        catch (IOException e) {
            e.printStackTrace();

        }
        //Sort according to name of courier
        Collections.sort(couriers, CourierBean.courierNameComp);
    return couriers;
    }
    public List<FlightBean> getAllFlights(){
       List<FlightBean> flights=new ArrayList();
        try {
            is=context.getResources().openRawResource(R.raw.flightsdetail);

            BufferedReader reader=new BufferedReader(new InputStreamReader(is));
            int count=0;
            while((str=reader.readLine())!=null){//Log.d("reader",str);
                String[] rows=str.split(",");

                FlightBean flight=new FlightBean();
                flight.setFlightID(Long.parseLong(rows[0]));
                flight.setFlightName(rows[1]);
                flight.setFlightWebsite(rows[2]);
                flights.add(flight);

            } is.close();
        }
        catch (IOException e) {
            e.printStackTrace();

        }
        //Sort according to name of courier
        Collections.sort(flights, FlightBean.flightNameComp);
        return flights;
    }

}
 /*// TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FlightsFragment newInstance(int columnCount) {
        FlightsFragment fragment = new FlightsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }*/