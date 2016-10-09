package in.kuari.trackall.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
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

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

import in.kuari.trackall.R;
import in.kuari.trackall.adapter.CourierListAdapter;
import in.kuari.trackall.bean.CourierBean;
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

        setAdapter();
        search();
        return rootView;
    }

    private void setAdapter(){
        List<CourierBean> couriers= populatelists();

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
       GridLayoutManager gridLayoutManager= new GridLayoutManager(getActivity(),3);
        recyclerView.setLayoutManager(layoutManager);

//couriers.clear();//Todo change
        adp=new CourierListAdapter(activity,trackingID,couriers);
        recyclerView.setAdapter(adp);
    }

    /**
     *
     * @return List<CourierBean>
     *Function calls getAllCourier() func of ReadData class
     *     */

    private List<CourierBean> populatelists(){
        //TODO check if prefrence is working
      boolean temp=  PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("notifications_reminder",true);

        Log.d("notifications_reminder",temp+"");
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
        //super.onActivityResult(requestCode, resultCode, data);
        Log.d("scanner",requestCode+"-"+resultCode);
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
       if(checkCameraPermission()) {

           FragmentIntentIntegrator intentIntegrator=new FragmentIntentIntegrator(this);
           Log.d("scanner","requestCode+resultCode");
           intentIntegrator.initiateScan();//Barc code scanner Intent


       }
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
    public static CourierFragment newInstance(int position) {
        CourierFragment fragment = new CourierFragment();
        Bundle args = new Bundle();
        // args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);

        return fragment;
    }
}
