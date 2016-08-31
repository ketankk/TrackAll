package in.kuari.trackall.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.List;

import in.kuari.trackall.R;
import in.kuari.trackall.adapter.FlightsAdapter;
import in.kuari.trackall.bean.FlightBean;
import in.kuari.trackall.utils.AppController;
import in.kuari.trackall.utils.ReadData;


public class FlightsFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 2;
    private static final String TAG = "FlightsFragment";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FlightsFragment() {
    }

    public static Fragment newInstance(int position) {
        FlightsFragment fragment = new FlightsFragment();
        Bundle args = new Bundle();
        // args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ecommerce_list, container, false);
        ReadData readData=new ReadData(getActivity());
        List<FlightBean> flights=readData.getAllFlights();

        // Set the adapter
     /*   if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
         }*/
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rc_all_ec);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        recyclerView.setAdapter(new FlightsAdapter(flights,getActivity()));

        analytics();
        return view;
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



}
