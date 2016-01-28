package in.kuari.trackall.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import in.kuari.trackall.R;
import in.kuari.trackall.activities.ShowResultActivity;
import in.kuari.trackall.bean.CourierBean;
import in.kuari.trackall.utils.CheckInternetConnectivity;
import in.kuari.trackall.utils.ReadData;

/**
 * Created by sultan_mirza on 1/18/16.
 */
public class CourierListAdapter extends RecyclerView.Adapter<CourierListAdapter.ViewHolder> {
    private List<CourierBean> couriers;
    private List<CourierBean> filteredCouriers;

    private Context context;
    private String trackID;
  //  private CourierBean courier;
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
   final CourierBean courier = filteredCouriers.get(position);

//final CourierBean courier1=courier;
    holder.courierName.setText(courier.getCourierName());
        Picasso.with(context).load(courier.getCourierImagePath()).into(holder.courierLogo);
    holder.view.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!CheckInternetConnectivity.isConnected(context)) {
                Snackbar.make(v, "No Internet Connection", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            } else {
                if (trackID != null) {
                    // Toast.makeText(context,courier1.toString(),Toast.LENGTH_LONG).show();
                    CourierSelected(courier);
                } else
                    CourierDetailPage(courier);
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
            for(CourierBean s:couriers){
               // Log.d("courierName",s.toString());

                if((s.getCourierName().toLowerCase()).contains(input)){
                    filteredCouriers.add(s);
                  //  Log.d("courierName2",s.toString());

                }
            }
        }

       notifyDataSetChanged();
    }


    void CourierSelected(CourierBean courier){
        Log.d("gg",courier.toString());
        Intent intent=new Intent(context, ShowResultActivity.class);
        intent.putExtra("trackId",trackID);
        intent.putExtra("comingFrom",0);
        intent.putExtra("courierID",courier.getCourierID());

        context.startActivity(intent);//, ActivityOptions.makeSceneTransitionAnimation((Activity)context).toBundle());

    }
    void CourierDetailPage(CourierBean courier){
        Log.d("gg",courier.toString());
        Intent intent=new Intent(context, ShowResultActivity.class);

        intent.putExtra("comingFrom",1);
        intent.putExtra("courierWeb",courier.getCourierWebsite());

        context.startActivity(intent);
    }

}
