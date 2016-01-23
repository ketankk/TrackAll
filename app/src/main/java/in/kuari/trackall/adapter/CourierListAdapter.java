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

import in.kuari.trackall.R;
import in.kuari.trackall.activities.MainActivity;
import in.kuari.trackall.activities.ShowResultActivity;

/**
 * Created by sultan_mirza on 1/18/16.
 */
public class CourierListAdapter extends RecyclerView.Adapter<CourierListAdapter.ViewHolder> {
    ArrayList<String> unilist;
    ArrayList<String> list;
private Context context;
    private String trackID;
    public CourierListAdapter(Context context)

    {  this.context=context;
        unilist = new ArrayList<>();
        list = new ArrayList<>();
        unilist.add("ff");
        unilist.add("nhff");
        unilist.add("qff");
        unilist.add("asaff");
        unilist.add("lccff");
        unilist.add("oeufff");

        list.add("ff");
        list.add("nhff");
        list.add("qff");
        list.add("asaff");
        list.add("lccff");
        list.add("oeufff");

//readSMS();
    }
public CourierListAdapter(Context context,String trackID){
    this.context=(context);

    this.trackID=trackID;
}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.courier_row,parent,false);
       ViewHolder vh=new ViewHolder(v);


        return vh;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
final String name=list.get(position);
        holder.courierName.setText(name);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(trackID!=null)
                     CourierSelected(name);
                else
                     CourierDetailPage();
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
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
    list.clear();
        int i=0;
        if(input.length()==0)
            list.addAll(unilist);
        else
        {
            for(String s:unilist){
                if(s.contains(input)){
                    list.add(s);
                  //  Log.d("hh",s);
                }
            }
        }
//       Log.d("hh","s");

       notifyDataSetChanged();
    }

    void readSMS(){
        Uri uri=Uri.parse("content://sms/inbox");
        Cursor c=context.getContentResolver().query(uri,null,null,null,null);
//        Log.d("outside","cusrsor");

        while (c.moveToNext()){
           // Log.d("inside","cusrsor");
           unilist.add(c.getString(c.getColumnIndex("body")));
            list.add(c.getString(c.getColumnIndex("body")));

        }
    }
    void CourierSelected(String cId){
        Intent intent=new Intent(context, ShowResultActivity.class);
        intent.putExtra("trackId",trackID);
        intent.putExtra("courierID",cId);
        context.startActivity(intent);//, ActivityOptions.makeSceneTransitionAnimation((Activity)context).toBundle());

    }
    void CourierDetailPage(){

    }

}
