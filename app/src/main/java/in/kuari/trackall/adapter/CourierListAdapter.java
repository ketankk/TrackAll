package in.kuari.trackall.adapter;


import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.kuari.trackall.R;
import in.kuari.trackall.activities.MainActivity;
import in.kuari.trackall.activities.ShowResultActivity;
import in.kuari.trackall.entity.CourierEntity;
import in.kuari.trackall.fragments.Courier;
import in.kuari.trackall.utils.ReadData;

/**
 * Created by sultan_mirza on 1/18/16.
 */
public class CourierListAdapter extends RecyclerView.Adapter<CourierListAdapter.ViewHolder> {
    private List<CourierEntity> couriers;
    private List<CourierEntity> filteredCouriers;

    private Context context;
    private String trackID;
    private CourierEntity courier;
    public CourierListAdapter(Context context)

    {  this.context=context;
        populatelists();

//readSMS();
    }
    void populatelists(){
        ReadData readData=new ReadData(context);
       List<CourierEntity> courier1= readData.getAllCourier();

        couriers=courier1;
        filteredCouriers=courier1;

    }
public CourierListAdapter(Context context,String trackID){
    this.context=(context);
    this.trackID=trackID;
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
        courier=couriers.get(position);

        holder.courierName.setText(courier.getCourierName());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(trackID!=null)
                     CourierSelected(courier);
                else
                     CourierDetailPage();
            }
        });


    }

    @Override
    public int getItemCount() {
        return couriers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView courierName;
        private ImageView courierLogo;
        private View view;
        public ViewHolder(View itemView) {
            super(itemView);
            courierName= (TextView) itemView.findViewById(R.id.courier_name);
            courierLogo= (ImageView) itemView.findViewById(R.id.courier_logo);
            this.view=itemView;

        }

    }
   public void filter(String input){
    filteredCouriers.clear();
        int i=0;
        if(input.length()==0)
            filteredCouriers.addAll(couriers);
        else
        {
            for(CourierEntity s:couriers){
                if(s.getCourierName().contains(input)){
                    filteredCouriers.add(s);
                  //  Log.d("hh",s);
                }
            }
        }
//       Log.d("hh","s");

       notifyDataSetChanged();
    }

    /*void readSMS(){
        Uri uri=Uri.parse("content://sms/inbox");
        Cursor c=context.getContentResolver().query(uri,null,null,null,null);
//        Log.d("outside","cusrsor");

        while (c.moveToNext()){
           // Log.d("inside","cusrsor");
           unilist.add(c.getString(c.getColumnIndex("body")));
            list.add(c.getString(c.getColumnIndex("body")));

        }
    }*/
    void CourierSelected(CourierEntity courier){
        Intent intent=new Intent(context, ShowResultActivity.class);
        intent.putExtra("trackId",trackID);
        intent.putExtra("trackURL",courier.getCourierTrackLink());
        intent.putExtra("courierID",courier.getCourierID());

        context.startActivity(intent);//, ActivityOptions.makeSceneTransitionAnimation((Activity)context).toBundle());

    }
    void CourierDetailPage(){

    }

}
