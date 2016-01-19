package in.kuari.trackall.adapter;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.kuari.trackall.R;

/**
 * Created by sultan_mirza on 1/18/16.
 */
public class CourierListAdapter extends RecyclerView.Adapter<CourierListAdapter.ViewHolder> {
    ArrayList<String> unilist;
    ArrayList<String> list;

    public CourierListAdapter()

    {  unilist = new ArrayList<>();
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


    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.courier_row,parent,false);
       ViewHolder vh=new ViewHolder(v);

        return vh;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
String name=list.get(position);
        holder.courierName.setText(name);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
TextView courierName;
        public ViewHolder(View itemView) {
            super(itemView);
            courierName= (TextView) itemView.findViewById(R.id.courier_name);
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
                    Log.d("hh",s);
                }
            }
        }
       Log.d("hh","s");

       notifyDataSetChanged();
    }
}
