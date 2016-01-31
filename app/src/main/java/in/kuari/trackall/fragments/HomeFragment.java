package in.kuari.trackall.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hudomju.swipe.OnItemClickListener;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.SwipeableItemClickListener;
import com.hudomju.swipe.adapter.RecyclerViewAdapter;

import java.util.List;

import in.kuari.trackall.R;
import in.kuari.trackall.adapter.SearchHistoryAdapter;
import in.kuari.trackall.bean.SearchHistory;
import in.kuari.trackall.databases.SQLiteDBHandler;

/**
 * Created by sultan_mirza on 1/18/16.
 */
public class HomeFragment extends Fragment{

    private EditText trackID;
    private Button submitButton;
private RecyclerView  recyclerView;
    private SearchHistoryAdapter adp;
    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.home_layout,container,false);
        Activity activity=getActivity();
        trackID = (EditText) rootView.findViewById(R.id.input_trackID_or_name);

       recyclerView= (RecyclerView) rootView.findViewById(R.id.list_search_history);

        RecyclerView.LayoutManager manager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);

        SQLiteDBHandler handler=new SQLiteDBHandler(activity);
        List<SearchHistory> searchHistories=handler.getAllSearches();
        SearchHistory h=new SearchHistory();
          adp=new SearchHistoryAdapter(activity,searchHistories);
        recyclerView.setAdapter(adp);
        search(activity);
        return rootView;
    }

    void search(Activity activity){

        trackID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adp.filter(trackID.getText().toString().toLowerCase());

            }

            @Override
            public void afterTextChanged(Editable s) {



            }
        });
    }


}
