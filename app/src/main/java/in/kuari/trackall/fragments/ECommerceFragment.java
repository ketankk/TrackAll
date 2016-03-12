package in.kuari.trackall.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import in.kuari.trackall.R;
import in.kuari.trackall.adapter.ECommerceAdapter;

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
