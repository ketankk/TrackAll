package in.kuari.trackall.databases;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.kuari.trackall.utils.AppController;

/**
 * Created by root on 2/11/16.
 */
public class MYSQLHandler {
    private String TAG="FEEDBACK";
    private Context context;
    public MYSQLHandler(Context context){
        this.context=context;
    }
    public void SendMail(final String msg){

         String URL="http://trackall.kuari.in/connection.php";

        final ProgressDialog dialog=new ProgressDialog(context);
        dialog.setMessage("Sending Message....");
        dialog.show();
        StringRequest request=new StringRequest(Request.Method.POST,URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Log.d("g",response);
                      //  Toast.makeText(activity,response,Toast.LENGTH_SHORT).show();

                        dialog.hide();
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                     //  Toast.makeText(activity,msg,Toast.LENGTH_SHORT).show();

                        dialog.hide();
                    }
                }){@Override
        protected Map<String,String> getParams(){
            Map<String,String> params = new HashMap<String, String>();
            params.put("msg", msg);
            return params;
        }

        };

        AppController.getmInstance().addToRequestQueue(request,TAG);
    }
    public void sendgcmtokentoserver(final String token){
         String URL="http://trackall.kuari.in/gcmtoken_server.php";

        StringRequest request=new StringRequest(Request.Method.POST,URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Log.d("g",response);
                        //  Toast.makeText(activity,response,Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                //  Toast.makeText(activity,msg,Toast.LENGTH_SHORT).show();

            }
        }){@Override
        protected Map<String,String> getParams(){
            Map<String,String> params = new HashMap<String, String>();
            params.put("gcmtoken", token);
            return params;
        }

        };

     //   AppController.getmInstance().addToRequestQueue(request,TAG);
    }
}
