package in.kuari.trackall.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import in.kuari.trackall.R;
import in.kuari.trackall.activities.ListAllCourier;

/**
 * Created by sultan_mirza on 1/18/16.
 */
public class HomeFragment extends Fragment{

    private EditText trackID;
    private Button submitButton;

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.home_layout,container,false);
        Activity activity=getActivity();
        trackID = (EditText) rootView.findViewById(R.id.input_trackID);
        submitButton= (Button) rootView.findViewById(R.id.submit_trackID);
           submitButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   clickSubmit();
               }
           });

        return rootView;
    }

    void clickSubmit(){
        String trackId=trackID.getText().toString();
        if(trackId.length()==0){
            trackID.setError("Enter Tracking ID");
        }else {
            Intent intent = new Intent(getActivity(), ListAllCourier.class);
            intent.putExtra("trackID", trackId);
            startActivity(intent);
        }

    }
}
