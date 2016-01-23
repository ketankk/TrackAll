package in.kuari.trackall.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.kuari.trackall.R;
import in.kuari.trackall.adapter.CourierListAdapter;

public class Courier extends Fragment {
    private RecyclerView recyclerView;
    private CourierListAdapter adp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.activity_courier,container,false);


        recyclerView= (RecyclerView) rootView.findViewById(R.id.rc_all_courier);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adp=new CourierListAdapter(getActivity());

        recyclerView.setAdapter(adp);

        return rootView;
    }


}
