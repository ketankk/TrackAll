package in.kuari.trackall.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import in.kuari.trackall.R;
import in.kuari.trackall.activities.ShowResultActivity;
import in.kuari.trackall.entity.CourierEntity;
import in.kuari.trackall.utils.MLRoundedImageView;
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
        couriers = new ArrayList<>();
        filteredCouriers=new ArrayList<>();

        populatelists();

//readSMS();
    }
    void populatelists(){
        ReadData readData=new ReadData(context);

        couriers=readData.getAllCourier();
        filteredCouriers=readData.getAllCourier();

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
    courier = filteredCouriers.get(position);


    holder.courierName.setText(courier.getCourierName());
//holder.courierLogo.setImageDrawable(R.drawable.(courier.getCourierImagePath()));
    holder.view.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (trackID != null)
                CourierSelected(courier);
            else
                CourierDetailPage();
        }
    });

}


    @Override
    public int getItemCount() {
        return filteredCouriers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView courierName;
        private ImageView courierLogo;
        private View view;
        private  MLRoundedImageView circularImageView;
        public ViewHolder(View itemView) {
            super(itemView);
            courierName= (TextView) itemView.findViewById(R.id.courier_name);
            courierLogo= (ImageView) itemView.findViewById(R.id.courier_logo);
            this.view=itemView;
            // circularImageView = (MLRoundedImageView)itemView.findViewById(R.id.courier_logo);

           /* circularImageView.setBorderColor(context.getResources().getColor(R.color.GrayLight));
           circularImageView.setBorderWidth(10);
            circularImageView.setSelectorColor(getResources().getColor(R.color.BlueLightTransparent));
            circularImageView.setSelectorStrokeColor(getResources().getColor(R.color.BlueDark));
            circularImageView.setSelectorStrokeWidth(10);
            circularImageView.addShadow();*/

        }

    }
   public void filter(String input){
      // Log.d("inside input",input+filteredCouriers.size()+"c"+couriers.size());

       filteredCouriers.clear();
        int i=0;
       //Log.d("inside input2",input+"c"+couriers.size());

       if(input.length()==0)
            filteredCouriers.addAll(couriers);
        else
        {
            for(CourierEntity s:couriers){
               // Log.d("courierName",s.toString());

                if((s.getCourierName().toLowerCase()).contains(input)){
                    filteredCouriers.add(s);
                  //  Log.d("courierName2",s.toString());

                }
            }
        }

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
