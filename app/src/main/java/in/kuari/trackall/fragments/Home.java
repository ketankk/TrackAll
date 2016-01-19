package in.kuari.trackall.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import in.kuari.trackall.R;

/**
 * Created by sultan_mirza on 1/18/16.
 */
public class Home extends Fragment{
    public Home() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("ii","OncreteHome");
        View rootView=inflater.inflate(R.layout.home_layout,container,false);
        EditText trackID= (EditText) container.findViewById(R.id.input_trackID);

        return rootView;
    }
}
