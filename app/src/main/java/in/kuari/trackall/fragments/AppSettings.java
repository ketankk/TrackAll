package in.kuari.trackall.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import in.kuari.trackall.R;
import in.kuari.trackall.adapter.CourierListAdapter;
import in.kuari.trackall.utils.AppController;

public class AppSettings extends Fragment {
    private ToggleButton toggleLogo;
    private ToggleButton toggleNotification;
    private Activity activity;
    private SharedPreferences pref;
    private static final String TAG = "AppSettings";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity=getActivity();

        View rootView=inflater.inflate(R.layout.fragment_settings,container,false);
        toggleLogo= (ToggleButton) rootView.findViewById(R.id.toggle_logo);
        toggleNotification= (ToggleButton) rootView.findViewById(R.id.toggle_notification);

         pref=activity.getSharedPreferences("TRACKALL",Context.MODE_PRIVATE);
        loadLogoSetting();
        reminderSetting();

     //   Toast.makeText(activity,"f"+loadLogo,Toast.LENGTH_SHORT).show();
        analytics();
        return rootView;
    }


    private void loadLogoSetting(){
        boolean loadLogo=pref.getBoolean("LoadLogo",true);
        if(loadLogo){
            toggleLogo.setChecked(true);
        }else toggleLogo.setChecked(false);

        toggleLogo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            SharedPreferences.Editor editor = activity.getSharedPreferences("TRACKALL", Context.MODE_PRIVATE).edit();

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editor.putBoolean("LoadLogo",true);
                    editor.apply();

                }else {
                    editor.putBoolean("LoadLogo",false);
                    editor.apply();
                }
            }
        });
    }

    private void reminderSetting(){
        boolean loadNoti=pref.getBoolean("LoadNoti",true);
        if(loadNoti){
            toggleNotification.setChecked(false);
        }else toggleNotification.setChecked(true);

        toggleNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = activity.getSharedPreferences("TRACKALL", Context.MODE_PRIVATE).edit();

                if(isChecked){
                    editor.putBoolean("LoadNoti",true);

                }else {
                    editor.putBoolean("LoadNoti",false);

                }editor.apply();
            }
        });

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
