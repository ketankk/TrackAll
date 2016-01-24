package in.kuari.trackall.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import in.kuari.trackall.R;
import in.kuari.trackall.entity.CourierEntity;

/**
 * Created by root on 1/24/16.
 */
public class ReadData {
   private Context context;
    private String str;
    private InputStream is;

    private List<CourierEntity> couriers;

    public ReadData(Context context){
     this.context=context;
    }
    public List<CourierEntity> getAllCourier(){
        couriers=new ArrayList();
        try {
             is=context.getResources().openRawResource(R.raw.courierdetail);

            BufferedReader reader=new BufferedReader(new InputStreamReader(is));

            while((str=reader.readLine())!=null){Log.d("reader",str);
                String[] rows=str.split(",");

            CourierEntity courier=new CourierEntity();
                courier.setCourierID(Long.parseLong(rows[0]));
                courier.setCourierName(rows[1]);
                courier.setCourierImagePath(rows[2]);
                courier.setCourierTrackLink(rows[3]);
                courier.setCourierWebsite(rows[4]);
                couriers.add(courier);

            } is.close();
        }
        catch (IOException e) {
            e.printStackTrace();

        }
    return couriers;
    }

}
