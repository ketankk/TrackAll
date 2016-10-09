package in.kuari.trackall.databases;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by root on 2/11/16.
 */

public class MYSQLHandler {
    private String TAG="FEEDBACK";
   private Context context;
    public MYSQLHandler(Context context){
        this.context=context;
    }
    public void sendfcmtokentoserver(final String token){
         String URL="http://trackall.kuari.in/gcmtoken_server.php";
        Log.d(TAG,"sendFCM");

        StringRequest request=new StringRequest(Request.Method.POST,URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,response);
                        if(response.equals("1")){
                            SharedPreferences.Editor editor =context.getSharedPreferences("TRACKALL", MODE_PRIVATE).edit();
                            editor.putBoolean("FCMreg", true);
                            editor.apply();
                        }
                        //  Toast.makeText(activity,response,Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                //  Toast.makeText(activity,msg,Toast.LENGTH_SHORT).show();

            }
        }){@Override
        protected Map<String,String> getParams(){
            Map<String,String> params = new HashMap<>();
            params.put("fcmtoken", token);
            return params;
        }

        };

   Volley.newRequestQueue(context).add(request);
    }
}
