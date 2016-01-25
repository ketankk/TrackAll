package in.kuari.trackall.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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

public class CourierFragment extends Fragment {
    private RecyclerView recyclerView;
    private CourierListAdapter adp;
    private Context context;
    private EditText  courierName;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.activity_courier,container,false);

        context=getActivity();
        recyclerView= (RecyclerView) rootView.findViewById(R.id.rc_all_courier);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adp=new CourierListAdapter(getActivity());

        recyclerView.setAdapter(adp);

search(rootView);

        return rootView;
    }
    void search(View view){

        courierName= (EditText) view.findViewById(R.id.input_courier_name);
        courierName.setFocusable(true);
       // Log.d("ss",courierName.getText().toString().toLowerCase());
        courierName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.d("ssqq",courierName.getText().toString().toLowerCase());
                adp.filter(courierName.getText().toString().toLowerCase());

            }

            @Override
            public void afterTextChanged(Editable s) {



            }
        });
    }
/*void readData(){

    File path =context.getFilesDir();
    Log.d("FileDir",path.toString());
    File file=new File(path,"DataList.txt");
    try {
        FileOutputStream outputStream=new FileOutputStream(file);
        outputStream.write("textca".getBytes());
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    String str="";
    try {
        InputStream inputStream=context.openFileInput("DataList.txt");
        InputStreamReader inputStreamReader=new InputStreamReader(inputStream);

        BufferedReader reader=new BufferedReader(inputStreamReader);
        while((str=reader.readLine())!=null){
            Log.d("reader",str);
        }inputStream.close();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }

}*/
}
