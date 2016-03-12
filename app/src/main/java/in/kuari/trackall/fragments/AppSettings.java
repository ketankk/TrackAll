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

import in.kuari.trackall.R;
import in.kuari.trackall.adapter.CourierListAdapter;

public class AppSettings extends Fragment {
    private ToggleButton toggle;
    private Activity activity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity=getActivity();

        View rootView=inflater.inflate(R.layout.fragment_settings,container,false);
        toggle= (ToggleButton) rootView.findViewById(R.id.toggle_logo);

        SharedPreferences pref=activity.getSharedPreferences("TRACKALL",Context.MODE_PRIVATE);
        boolean loadLogo=pref.getBoolean("LoadLogo",true);
        if(loadLogo){
            toggle.setChecked(true);
        }else toggle.setChecked(false);

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
     //   Toast.makeText(activity,"f"+loadLogo,Toast.LENGTH_SHORT).show();
        return rootView;
    }


}
