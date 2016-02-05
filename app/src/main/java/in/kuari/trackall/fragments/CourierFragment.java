package in.kuari.trackall.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import in.kuari.trackall.R;
import in.kuari.trackall.adapter.CourierListAdapter;
import in.kuari.trackall.utils.ConstantValues;

public class CourierFragment extends Fragment {
    private RecyclerView recyclerView;
    private CourierListAdapter adp;
    private Activity activity;
    public  EditText  courierName;
    private EditText  trackingID;
    private String trackID;
int mColumnCount=2;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.activity_courier,container,false);

        activity=getActivity();

        trackingID= (EditText) rootView.findViewById(R.id.input_trackID);
        courierName= (EditText) rootView.findViewById(R.id.input_courier_name);
/*
        if (rootView instanceof RecyclerView) {
            Context context = rootView.getContext();
            RecyclerView recyclerView = (RecyclerView) rootView;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }*/



            recyclerView= (RecyclerView) rootView.findViewById(R.id.rc_all_courier);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        search(activity);

        adp=new CourierListAdapter(activity,trackingID);
        recyclerView.setAdapter(adp);

        return rootView;
    }

 void search(Activity activity){

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

}
