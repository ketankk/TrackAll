package in.kuari.trackall.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import in.kuari.trackall.R;
import in.kuari.trackall.bean.CourierBean;
import in.kuari.trackall.bean.ECommerce;
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

                Log.d("Count",rows[0]+""+rows.length);
            CourierBean courier=new CourierBean();
                courier.setCourierID(Long.parseLong(rows[0]));
                courier.setCourierName(rows[1]);
                courier.setCourierTrackLink(rows[2]);
                courier.setCourierWebsite(rows[3]);
                courier.setCourierImagePath(rows[4]);
                    //check if contact number and email-id is there
                    if (rows.length>5&&rows[5] != null && rows[5].length() > 0)
                        courier.setCourierContact(rows[5].trim());
                    if (rows.length>6&&rows[6] != null && rows[6].length() > 0)
                        courier.setCourierEmail(rows[6].trim());

              //  Log.d("Couriers",courier.toString());
                couriers.add(courier);

            } is.close();
        }
        catch (IOException e) {
            e.printStackTrace();

        }
        //Sort according to name of ic_courier
        Collections.sort(couriers, CourierBean.courierNameComp);
    return couriers;
    }
    public List<FlightBean> getAllFlights(){
       List<FlightBean> flights=new ArrayList();
        try {
            is=context.getResources().openRawResource(R.raw.flightsdetail);

            BufferedReader reader=new BufferedReader(new InputStreamReader(is));
            int count=0;
            while((str=reader.readLine())!=null){
               //Log.d("reader",str);
                String[] rows=str.split(",");

                FlightBean flight=new FlightBean();
                flight.setFlightID(Long.parseLong(rows[0]));
                flight.setFlightName(rows[1]);
                flight.setFlightWebsite(rows[2]);
                flight.setFlightLogo(rows[3]);
                flights.add(flight);

            }
            reader.close();
            is.close();
        }
        catch (IOException e) {
            e.printStackTrace();

        }
        //Sort according to name of ic_courier
        Collections.sort(flights, FlightBean.flightNameComp);
        return flights;
    }
    public List<ECommerce> getAllECommerce(){
        List<ECommerce> eCommerces=new ArrayList();
        try {
            is=context.getResources().openRawResource(R.raw.ecommercedetail);

            BufferedReader reader=new BufferedReader(new InputStreamReader(is));
            int count=0;
            while((str=reader.readLine())!=null){
               // Log.d("reader",str);
                String[] rows=str.split(",");

                ECommerce eCommerce=new ECommerce();
                eCommerce.setId(Long.parseLong(rows[0]));
                eCommerce.setName(rows[1]);
                eCommerce.setURL(rows[2]);
                eCommerce.setImgPath(rows[3]);
                eCommerces.add(eCommerce);

            }
            reader.close();
            is.close();
        }
        catch (IOException e) {
            e.printStackTrace();

        }
        //Sort according to name of ic_courier
        Collections.sort(eCommerces, ECommerce.eCommerceComp);
        return eCommerces;
    }

   public CourierBean getCourierByID(int id){
        List<CourierBean>couriers=getAllCourier();
       for(CourierBean courier:couriers)
       {
           if(courier.getCourierID()==id)
               return courier;
       }
   return new CourierBean();
   }
    public ECommerce getECommerceByID(int id){
        List<ECommerce>eCommerces=getAllECommerce();
        for(ECommerce eCommerce:eCommerces)
        {
            if(eCommerce.getId()==id)
                return eCommerce;
        }
        return new ECommerce();
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
    }*//*
 int getScale(){
     Display display=((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
     int width=display.getWidth();
     Double val = new Double(width)/new Double(PIC_WIDTH);
     val = val * 100d;
     WebView web = new WebView(this);
     web.setPadding(0, 0, 0, 0);
     web.setInitialScale(getScale());
     return val.intValue();
 }*/

           /* circularImageView.setBorderColor(context.getResources().getColor(R.color.GrayLight));
           circularImageView.setBorderWidth(10);
            circularImageView.setSelectorColor(getResources().getColor(R.color.BlueLightTransparent));
            circularImageView.setSelectorStrokeColor(getResources().getColor(R.color.BlueDark));
            circularImageView.setSelectorStrokeWidth(10);
            circularImageView.addShadow();*/
/*void readData(){

    File path =context.getFilesDir();
    Log.d("FileDir",path.toString());
    File file=new File(path,"DataList.txt");
    try {
        FileOutputStream outputStream=new FileOutputStream(file);
        outputStream.write("textca".getBytes());
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    String str="";
    try {
        InputStream inputStream=context.openFileInput("DataList.txt");
        InputStreamReader inputStreamReader=new InputStreamReader(inputStream);

        BufferedReader reader=new BufferedReader(inputStreamReader);
        while((str=reader.readLine())!=null){
            Log.d("reader",str);
        }inputStream.close();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }

}*/
