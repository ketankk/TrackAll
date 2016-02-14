package in.kuari.trackall.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import in.kuari.trackall.R;
import in.kuari.trackall.adapter.CourierListAdapter;
import in.kuari.trackall.adapter.ECommerceAdapter;
import in.kuari.trackall.adapter.SearchHistoryAdapter;
import in.kuari.trackall.bean.ECommerce;
import in.kuari.trackall.bean.SearchHistory;
import in.kuari.trackall.databases.SQLiteDBHandler;
import in.kuari.trackall.utils.ReadData;

public class ECommerceFragment extends Fragment {
    private RecyclerView recyclerView;
    private ECommerceAdapter adp;
    private Activity activity;
    public  EditText  ECName;
    private EditText  trackingID;
    private String trackID;

    public ECommerceFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ECommerceFragment newInstance(int columnCount) {
        ECommerceFragment fragment = new ECommerceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_ecommerce_list,container,false);

        activity=getActivity();

        //trackingID= (EditText) rootView.findViewById(R.id.input_ec_trackID);
        ECName= (EditText) rootView.findViewById(R.id.input_ec_name);


        recyclerView= (RecyclerView) rootView.findViewById(R.id.rc_all_ec);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        search(activity);

        adp=new ECommerceAdapter(activity);
        recyclerView.setAdapter(adp);

        return rootView;
    }

    void search(Activity activity){

        ECName.setFocusable(true);
        ECName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adp.filter(ECName.getText().toString().toLowerCase());

            }

            @Override
            public void afterTextChanged(Editable s) {



            }
        });
    }



}
