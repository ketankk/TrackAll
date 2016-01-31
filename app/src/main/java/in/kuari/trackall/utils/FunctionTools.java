package in.kuari.trackall.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by root on 1/26/16.
 */
public class FunctionTools {
    private Context context;
Activity activity;
    public FunctionTools(Context context){
    this.context=context;
        activity= (Activity) context;
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
   public void takeScreenShot(String name){
        String mPath= Environment.getExternalStorageDirectory().toString()+"/"+name+".jpg";
       try {
       View view= activity.getWindow().getDecorView().getRootView();
       view.setDrawingCacheEnabled(true);

       Bitmap bitmap=Bitmap.createBitmap(view.getDrawingCache());
       view.setDrawingCacheEnabled(false);

       File imgFile=new File(mPath);

           FileOutputStream outputStream=new FileOutputStream(imgFile);
           int quality=100;
           bitmap.compress(Bitmap.CompressFormat.JPEG,quality,outputStream);
           outputStream.flush();
           outputStream.close();
           openScreenshot(imgFile);

       } catch (Throwable e) {
           e.printStackTrace();
       }


   }
    void openScreenshot(File imageFile){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri=Uri.fromFile(imageFile);
        intent.setDataAndType(uri,"image/*");
        context.startActivity(intent);

    }
}

