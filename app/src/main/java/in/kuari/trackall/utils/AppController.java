package in.kuari.trackall.utils;

import android.app.Application;


import in.kuari.trackall.R;

/**
 * Created by root on 2/11/16.
 */
public class AppController extends Application {

    private static final Object TAG =AppController.class.getSimpleName() ;
    /*private RequestQueue mRequestQueue;
    private static AppController mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
    mInstance=this;
    }

    public static synchronized AppController getmInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if(mRequestQueue==null){
            mRequestQueue= Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }
   public<T> void  addToRequestQueue(Request<T> req, String tag){
       req.setTag(TextUtils.isEmpty(tag)?TAG:tag);
       getRequestQueue().add(req);

   }
    public<T> void  addToRequestQueue(Request<T> req){
        req.setTag(TAG);
        getRequestQueue().add(req);

    }
    public void cancelPendingRequest(Object tag){
        if(mRequestQueue!=null)
        mRequestQueue.cancelAll(tag);
    }*/

    /**
     * Tracker used in google analytics
     */

}
