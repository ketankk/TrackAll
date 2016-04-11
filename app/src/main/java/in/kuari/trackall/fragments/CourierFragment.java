package in.kuari.trackall.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;
import java.util.jar.Manifest;

import in.kuari.trackall.R;
import in.kuari.trackall.adapter.CourierListAdapter;
import in.kuari.trackall.bean.CourierBean;
import in.kuari.trackall.utils.AppController;
import in.kuari.trackall.utils.ReadData;

public class CourierFragment extends Fragment {
    private RecyclerView recyclerView;
    private CourierListAdapter adp;
    private Activity activity;
    public  EditText  courierName;
    private EditText  trackingID;
    private String trackID;
    private Button barCodebtn;
    private static final String TAG = "CourierFragment";
    private String[] permissions = {android.Manifest.permission.CAMERA};


    private static final int REQUST_CAMERA_PERMISSION_CODE=115;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_courier,container,false);

        activity=getActivity();

        trackingID= (EditText) rootView.findViewById(R.id.input_trackID);
        courierName= (EditText) rootView.findViewById(R.id.input_courier_name);
        barCodebtn= (Button) rootView.findViewById(R.id.barcodeinput);
barCodebtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        scanSomething();
    }
});

            recyclerView= (RecyclerView) rootView.findViewById(R.id.rc_all_courier);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

         List<CourierBean> couriers= populatelists();


        adp=new CourierListAdapter(activity,trackingID,couriers);
        recyclerView.setAdapter(adp);
        search();
        analytics();
        return rootView;
    }

    /**
     *
     * @return List<CourierBean>
     *Function calls getAllCourier() func of ReadData class
     *     */

    private List<CourierBean> populatelists(){
        ReadData readData=new ReadData(activity);

        return readData.getAllCourier();

    }
 void search(){

final EditText courierName1=courierName;
        courierName1.setFocusable(true);
        courierName1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adp.filter(courierName1.getText().toString().toLowerCase());

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
 switch (requestCode){
            case IntentIntegrator.REQUEST_CODE:
               IntentResult result=IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if(result!=null)
                    trackID=result.getContents();
                Log.d("id",trackID+"cc");
               trackingID.setText(trackID);
                break;
        }
    }


    public void scanSomething() {
       if(checkCameraPermission())
       IntentIntegrator.forFragment(this).initiateScan();//Barc code scanner Intent
        else
       checkForPermission();
        if(!checkCameraPermission())
            Toast.makeText(activity, "Can't access camera", Toast.LENGTH_SHORT).show();
    }
    private boolean checkCameraPermission()
    {

        String permission = "android.permission.CAMERA";
        int res = activity.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
    Tracker mTracker;
    private void analytics(){
        AppController appController= (AppController) getActivity().getApplication();
        mTracker=appController.getDefaultTracker();
    }
    @Override
    public void onResume() {
        super.onResume();
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        GoogleAnalytics.getInstance(getActivity()).dispatchLocalHits();
    }
    private boolean checkForPermission() {

        if (ContextCompat.checkSelfPermission(activity, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissions, REQUST_CAMERA_PERMISSION_CODE);
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case REQUST_CAMERA_PERMISSION_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    break;
                }


        }
    }
}
