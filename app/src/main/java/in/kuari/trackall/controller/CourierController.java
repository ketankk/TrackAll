package in.kuari.trackall.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import in.kuari.trackall.activities.MainActivity;
import in.kuari.trackall.couriers.DTDC;

/**
 * Created by sultan_mirza on 1/17/16.
 */
public class CourierController {
    private WebView webView;

    private Context context;
    public CourierController(Context context,WebView webView){
        this.context=context;
        this.webView=webView;

    }


    public void populateView(int id){
DTDC obj= new DTDC(webView,context);
        Log.d("popuktaview","kkk");

obj.load();;

    }

}
