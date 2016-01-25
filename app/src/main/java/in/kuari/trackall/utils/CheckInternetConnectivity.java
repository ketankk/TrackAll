package in.kuari.trackall.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by root on 1/26/16.
 */
public class CheckInternetConnectivity {


    public  static boolean  isConnected(Context context){
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=connectivityManager.getActiveNetworkInfo();

        if(info!=null&&info.isConnectedOrConnecting()){
            Log.d("Conn",info.getTypeName());
            return true;
        }

        return false;
    }
}
