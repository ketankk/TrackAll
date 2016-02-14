package in.kuari.trackall.adapter;


import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import in.kuari.trackall.R;
import in.kuari.trackall.activities.ShowResultActivity;
import in.kuari.trackall.bean.CourierBean;
import in.kuari.trackall.bean.SearchHistory;
import in.kuari.trackall.databases.SQLiteDBHandler;
import in.kuari.trackall.utils.FunctionTools;
import in.kuari.trackall.utils.ReadData;

/**
 * Created by sultan_mirza on 1/18/16.
 */
public class CourierListAdapter extends RecyclerView.Adapter<CourierListAdapter.ViewHolder> {
    private List<CourierBean> couriers;
    private List<CourierBean> filteredCouriers;

    private Activity activity;
    private EditText trackingID;
  //  private CourierBean ic_courier;
    /*public CourierListAdapter(Activity activity)

    {  this.activity=activity;
        couriers = new ArrayList<>();
        filteredCouriers=new ArrayList<>();

        populatelists();

//readSMS();
    }*/
    void populatelists(){
        ReadData readData=new ReadData(activity);

        couriers=readData.getAllCourier();
        filteredCouriers=readData.getAllCourier();

    }
public CourierListAdapter(Activity activity,EditText trackingID){
    this.activity=activity;
    this.trackingID=trackingID;
    populatelists();

}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.courier_row,parent,false);
       ViewHolder vh=new ViewHolder(v);
     return vh;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
   final CourierBean courier = filteredCouriers.get(position);

    holder.courierName.setText(courier.getCourierName());
        SharedPreferences pref=activity.getSharedPreferences("TRACKALL",Context.MODE_PRIVATE);
        boolean loadLogo=pref.getBoolean("LoadLogo",true);
        boolean ff=pref.getBoolean("FirstTime",true);
        //Log.d("logo",loadLogo+" "+ff);
    if(loadLogo)
        Picasso.with(activity).load(courier.getCourierImagePath()).error(R.drawable.ic_menu_courier).into(holder.courierLogo);
    holder.view.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!FunctionTools.isConnected(activity)) {
                Snackbar.make(v, "No Internet Connection", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                String trck=trackingID.getText().toString();
                if(trck.length()>0)
                Popup(courier,trck);
            } else {
                if (trackingID != null)
                    CourierSelected(courier);

            }
        }
    });

}


    @Override
    public int getItemCount() {
        return filteredCouriers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView courierName;
        private View view;
        private  ImageView courierLogo;
        public ViewHolder(View itemView) {
            super(itemView);
            courierName= (TextView) itemView.findViewById(R.id.courier_name);
            courierLogo = (ImageView)itemView.findViewById(R.id.courier_logo);
            this.view=itemView;


        }

    }
   public void filter(String courierName){
       filteredCouriers.clear();
        int i=0;

       if(courierName.length()==0)
            filteredCouriers.addAll(couriers);
        else
        {
            for(CourierBean s:couriers){

                if((s.getCourierName().toLowerCase()).contains(courierName)){
                    filteredCouriers.add(s);

                }
            }
        }

       notifyDataSetChanged();
    }


    void CourierSelected(CourierBean courier){
        String trackID=trackingID.getText().toString();
        //Toast.makeText(activity,trackID+"hh",Toast.LENGTH_LONG).show();

        Log.d("trackId",trackID+"");
        if(trackID.length()>0)
            SaveSearchHistory(courier,trackID);
        Intent intent=new Intent(activity, ShowResultActivity.class);
        intent.putExtra("trackId",trackID);
        intent.putExtra("comingFrom",0);
        intent.putExtra("courierID",courier.getCourierID());

        ClipboardManager manager= (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip=ClipData.newPlainText("TrackingID",trackID);
        manager.setPrimaryClip(clip);
        Toast.makeText(activity,trackID+" Copied to ClipBoard",Toast.LENGTH_SHORT).show();
        activity.startActivity(intent);//, ActivityOptions.makeSceneTransitionAnimation((Activity)context).toBundle());

    }
    /*void CourierDetailPage(CourierBean courier){
        Log.d("gg",courier.toString());
        Intent intent=new Intent(activity, ShowResultActivity.class);

        intent.putExtra("comingFrom",1);
        intent.putExtra("courierWeb",courier.getCourierWebsite());

        activity.startActivity(intent);
    }
*/
    void SaveSearchHistory(CourierBean courier,String trackID){
        SQLiteDBHandler handler=new SQLiteDBHandler(activity);
        SearchHistory history=new SearchHistory();
        history.setName(courier.getCourierName());
        history.setTrackId(trackID);
        history.setCourierID(courier.getCourierID()+"");

        handler.addSearch(history);

    }

    void Popup(final CourierBean courierBean, final String trackingID){
        new AlertDialog.Builder(activity)
                .setTitle("No Internet Connection")
                .setMessage("BookMark -"+trackingID.toUpperCase()+" with "+courierBean.getCourierName())
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        SaveSearchHistory(courierBean,trackingID);
                    }
                }).setNegativeButton("No",null)
                .show();
    }
}
