package in.kuari.trackall.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
        IntentIntegrator.forFragment(this).initiateScan();
    }

}
