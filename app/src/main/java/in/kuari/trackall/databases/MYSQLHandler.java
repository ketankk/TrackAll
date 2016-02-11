package in.kuari.trackall.databases;

import android.app.Activity;
import android.app.ProgressDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import in.kuari.trackall.utils.AppController;

/**
 * Created by root on 2/11/16.
 */
public class MYSQLHandler {
    private String TAG="FEEDBACK";
    private Activity activity;
    private String URL="";
    public MYSQLHandler(Activity activity){
        this.activity=activity;
    }
    public void SendMail(final String msg){
        final ProgressDialog dialog=new ProgressDialog(activity);
        dialog.setMessage("Sending Message....");
        dialog.show();
        StringRequest request=new StringRequest(Request.Method.POST,URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        dialog.hide();
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    dialog.hide();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("msg",msg);
                return params;
            }
        };

        AppController.getmInstance().addToRequestQueue(request,TAG);
    }

}
