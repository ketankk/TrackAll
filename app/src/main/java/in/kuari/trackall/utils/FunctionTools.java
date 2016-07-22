package in.kuari.trackall.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by root on 1/26/16.
 */
public class FunctionTools {
    private static Context context;
    private Activity activity;

    public FunctionTools(Context context) {
        this.context = context;
        activity = (Activity) context;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if (info != null && info.isConnectedOrConnecting()) {
            Log.d("Conn", info.getTypeName());
            return true;
        }

        return false;
    }

    public String getTextFromClipBoard() {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = clipboard.getPrimaryClip();
        String str = clip.toString();
        return str;
    }

    public void takeScreenShot(String name) {
        String mPath = Environment.getExternalStorageDirectory().toString() + "/" + name + ".jpg";
        try {
            View view = activity.getWindow().getDecorView().getRootView();
            view.setDrawingCacheEnabled(true);

            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);

            File imgFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imgFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            openScreenshot(imgFile);

        } catch (Throwable e) {
            e.printStackTrace();
        }


    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        context.startActivity(intent);

    }

    public static int getRandomColor(){
        Random rnd=new Random();
        return Color.argb(255,rnd.nextInt(255),rnd.nextInt(255),rnd.nextInt(255));

    }
    public void backendAuth(final String token){

        final String android_id=Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("token",token);
        String URL="http://trackall.kuari.in/login/trackall/login_validator.php";
        String TAG="LOGIN";
        StringRequest request=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Log.d("res",jsonObject.getString("picture"));
                    String userName;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("res",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("err",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("token_id", token);
                params.put("device_id", android_id);

                   Log.d("tokee",android_id);
                return params;
            }
        };


        Volley.newRequestQueue(context).add(request);
    }

}

