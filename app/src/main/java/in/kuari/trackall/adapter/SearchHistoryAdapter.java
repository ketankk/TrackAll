package in.kuari.trackall.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import in.kuari.trackall.bean.SearchHistory;
import in.kuari.trackall.databases.SQLiteDBHandler;
import in.kuari.trackall.utils.Colors;
import in.kuari.trackall.utils.FunctionTools;

/**
 * Created by root on 1/31/16.
 */
public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.SearchViewHolder>{

    private List<SearchHistory> searchHistories;
    private List<SearchHistory> filterdeSearchHistories;
private View imgView;
    private Activity activity;

    public SearchHistoryAdapter(Activity activity, List<SearchHistory> searchHistories) {
        this.activity=activity;
        this.searchHistories=searchHistories;
        filterdeSearchHistories=new ArrayList<>();
       // this.imgView=imgView;
        filterdeSearchHistories.addAll(searchHistories);
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view=  LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_row,parent,false);
//if(getItemCount()==0)
   // imgView.setVisibility(View.VISIBLE);

        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SearchViewHolder holder, final int position) {
        /*if(getItemCount()==0)
            imgView.setVisibility(View.VISIBLE);
*/
       final SearchHistory searchHistory= filterdeSearchHistories.get(position);
        holder.name.setText(searchHistory.getName());
        holder.trackId.setText(searchHistory.getTrackId());
        holder.histDate.setText(searchHistory.getTime());
        holder.view.setBackgroundColor(Colors.getRandomColor());


        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!FunctionTools.isConnected(activity)) {
                    Snackbar.make(v, "No Internet Connection", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }else
                    OnClickHistoryitem(searchHistory);
            }
        });

        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bookMarkDeleteConf(searchHistory,position);
               //else                    // Toast.makeText(activity,"Cdelete"+position,Toast.LENGTH_LONG).show();
            }
        });

    }
//Alert Dialog for confirming if user want to delete BookMark
    private void bookMarkDeleteConf(final SearchHistory searchHistory, final int pos){
        String id=searchHistory.getTrackId();
        String name=searchHistory.getName();
        new AlertDialog.Builder(activity)
                .setTitle("Delete BookMark "+name+"-"+id)
                .setMessage("Are you sure want to delete this Bookmark?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteBookMark(searchHistory,pos);
                    }
                })
                .setNegativeButton("No",null)
                .show();
    }
private void deleteBookMark(SearchHistory searchHistory,int position) {
    String id=searchHistory.getTrackId();
    String name=searchHistory.getName();

    boolean flag = deleteHistory(searchHistory);
    if (flag) {
        Snackbar.make(activity.getWindow().getDecorView().findViewById(android.R.id.content), id + "-" + name + " Deleted ", Snackbar.LENGTH_SHORT).setAction("UNDO", null).show();
        //
        // Toast.makeText(activity,"delete"+position,Toast.LENGTH_LONG).show();
        filterdeSearchHistories.remove(position);
        notifyDataSetChanged();
    }
}
    @Override
    public int getItemCount() {
               return filterdeSearchHistories.size();
    }

     static class SearchViewHolder extends RecyclerView.ViewHolder{

         TextView trackId;
         TextView name;
         View view;
         ImageView deletebtn;
         TextView histDate;
         public SearchViewHolder(View itemView) {
             super(itemView);
             view=itemView;
             trackId= (TextView) itemView.findViewById(R.id.hist_id);
             name= (TextView) itemView.findViewById(R.id.hist_name);
             deletebtn= (ImageView) itemView.findViewById(R.id.searchHistDel);
             histDate= (TextView) itemView.findViewById(R.id.hist_date);

         }
     }
   void OnClickHistoryitem(SearchHistory searchHistory)
   {
       String trackID=searchHistory.getTrackId();
        long courierID=Long.parseLong(searchHistory.getCourierID());
       //Toast.makeText(activity,trackID+"hh"+courierID,Toast.LENGTH_LONG).show();

          Intent intent=new Intent(activity, ShowResultActivity.class);
           intent.putExtra("trackId",trackID);
           intent.putExtra("comingFrom",0);
           intent.putExtra("courierID",courierID);

       activity.startActivity(intent);//, ActivityOptions.makeSceneTransitionAnimation((Activity)context).toBundle());


    }
public void filter(String input){
    filterdeSearchHistories.clear();
    int i=0;

    if(input.length()==0)
        filterdeSearchHistories.addAll(searchHistories);
    else
    {
        for(SearchHistory s:searchHistories){

            if((s.getTrackId().toLowerCase().contains(input)||s.getName().toLowerCase().contains(input))){
                filterdeSearchHistories.add(s);

            }
        }
    }

    notifyDataSetChanged();
}
    private boolean deleteHistory(SearchHistory searchHistory){
        SQLiteDBHandler handler=new SQLiteDBHandler(activity);

        return handler.deleteHistory(searchHistory.getId()+"");
    }
}
