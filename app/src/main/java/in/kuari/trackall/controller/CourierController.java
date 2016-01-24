package in.kuari.trackall.controller;

import android.content.Context;
import android.util.Log;
import android.webkit.WebView;

import in.kuari.trackall.couriers.DTDC;
import in.kuari.trackall.utils.CourierDao;

/**
 * Created by sultan_mirza on 1/17/16.
 */
public class CourierController {
    private WebView webView;
    private long courierID;
    private Context context;

    public CourierController(Context context,WebView webView){
        this.context=context;
        this.webView=webView;

    }


    public void populateView(long id){
        CourierDao obj=null;
        int i=(int)id;
        switch (i){
            case 1:
            obj=new DTDC(webView,context);

            Log.d("popuktaview","kkk");
                break;
            case 2:
break;
            default:
                obj=new DTDC(webView,context);

                break;
        }


obj.load();;

    }

}
