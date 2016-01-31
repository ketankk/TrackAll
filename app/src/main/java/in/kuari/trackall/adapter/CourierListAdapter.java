package in.kuari.trackall.adapter;


import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
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
    public CourierListAdapter(Activity activity)

    {  this.activity=activity;
        couriers = new ArrayList<>();
        filteredCouriers=new ArrayList<>();

        populatelists();

//readSMS();
    }
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
        Picasso.with(activity).load(courier.getCourierImagePath()).error(R.drawable.ic_courier).into(holder.courierLogo);
    holder.view.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!FunctionTools.isConnected(activity)) {
                Snackbar.make(v, "No Internet Connection", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
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
        private  RoundedImageView courierLogo;
        public ViewHolder(View itemView) {
            super(itemView);
            courierName= (TextView) itemView.findViewById(R.id.courier_name);
            courierLogo = (RoundedImageView)itemView.findViewById(R.id.courier_logo);
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
        Toast.makeText(activity,trackID+"hh",Toast.LENGTH_LONG).show();

        Log.d("trackId",trackID+"");
        if(trackID.length()>0)
            SaveSearchHistory(courier,trackID);
        Intent intent=new Intent(activity, ShowResultActivity.class);
        intent.putExtra("trackId",trackID);
        intent.putExtra("comingFrom",0);
        intent.putExtra("courierID",courier.getCourierID());

        activity.startActivity(intent);//, ActivityOptions.makeSceneTransitionAnimation((Activity)context).toBundle());

    }
    void CourierDetailPage(CourierBean courier){
        Log.d("gg",courier.toString());
        Intent intent=new Intent(activity, ShowResultActivity.class);

        intent.putExtra("comingFrom",1);
        intent.putExtra("courierWeb",courier.getCourierWebsite());

        activity.startActivity(intent);
    }

    void SaveSearchHistory(CourierBean courier,String trackID){
        SQLiteDBHandler handler=new SQLiteDBHandler(activity);
        SearchHistory history=new SearchHistory();
        history.setName(courier.getCourierName());
        history.setTrackId(trackID);
        history.setCourierID(courier.getCourierID()+"");

        handler.addSearch(history);

    }

}
