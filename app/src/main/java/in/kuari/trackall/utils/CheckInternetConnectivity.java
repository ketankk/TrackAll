package in.kuari.trackall.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by root on 1/26/16.
 */
public class CheckInternetConnectivity {
    private Context context;

    CheckInternetConnectivity(Context context){
    this.context=context;
}
    public  static boolean  isConnected(Context context){
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=connectivityManager.getActiveNetworkInfo();

        if(info!=null&&info.isConnectedOrConnecting()){
            Log.d("Conn",info.getTypeName());
            return true;
        }

        return false;
    }
   public String getTextFromCipBoard(){
       ClipboardManager clipboard=(ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
       ClipData clip=clipboard.getPrimaryClip();
       String str=clip.toString();
       return str;
   }
}

