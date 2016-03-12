package in.kuari.trackall.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
public class BookMarkAdapter extends RecyclerView.Adapter<BookMarkAdapter.SearchViewHolder>{

    private List<SearchHistory> searchHistories;
    private List<SearchHistory> filterdeSearchHistories;
private View imgView;
    private Activity activity;

    public BookMarkAdapter(Activity activity, List<SearchHistory> searchHistories) {
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
     //  holder.bmrating.setRating(Float.parseFloat(searchHistory.getRating()));
        //Log.d("d",searchHistory.toString());
       // Toast.makeText(activity,"g"+searchHistory.getRating(), Toast.LENGTH_SHORT).show();

        // holder.view.setBackgroundColor(Colors.getRandomColor());


        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!FunctionTools.isConnected(activity)) {
                    Snackbar.make(v, "No Internet Connection", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }else
                    OnClickBookMarkitem(searchHistory);
            }
        });

        holder.menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(activity,"hh",Toast.LENGTH_SHORT).show();
               // Log.d("gg","hh");
                PopupMenu menu=new PopupMenu(activity,holder.menuBtn);
                menu.getMenuInflater().inflate(R.menu.bookmark_menu,menu.getMenu());
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        SelectItem(item.getItemId(),searchHistory,position);
                        return true;
                    }
                });
menu.show();
                   }
        });

    } @Override
    public int getItemCount() {
        return filterdeSearchHistories.size();
    }

    static class SearchViewHolder extends RecyclerView.ViewHolder{

        TextView trackId;
        TextView name;
        View view;
        ImageView menuBtn;
        TextView histDate;
        RatingBar bmrating;
        public SearchViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            trackId= (TextView) itemView.findViewById(R.id.hist_id);
            name= (TextView) itemView.findViewById(R.id.hist_name);
            menuBtn= (ImageView) itemView.findViewById(R.id.bookmarkMenu);
            histDate= (TextView) itemView.findViewById(R.id.hist_date);
           // bmrating= (RatingBar) itemView.findViewById(R.id.bm_rating);
        }
    }
    private  void SelectItem(int id,SearchHistory searchHistory,int pos){
        //Toast.makeText(activity,id+"",Toast.LENGTH_SHORT).show();
        switch (id){
            case R.id.bm_menu_share:shareBookmark(searchHistory);
                break;
            case R.id.bm_menu_delete:bookMarkDeleteConf(searchHistory,pos);
                break;
            case R.id.bm_menu_rate:rateBookmark(searchHistory);
                break;
            default:
        }
    }
//Alert Dialog for confirming if user want to delete BookMark
    private void bookMarkDeleteConf(final SearchHistory searchHistory, final int pos){
        String id=searchHistory.getTrackId();
        String name=searchHistory.getName();
        new AlertDialog.Builder(activity)
                .setTitle("Delete BookMark "+name+"-"+id.toUpperCase())
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


   void OnClickBookMarkitem(SearchHistory searchHistory)
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

    private void deleteBookMark(SearchHistory searchHistory,int position){
        SQLiteDBHandler handler=new SQLiteDBHandler(activity);

        boolean flag= handler.deleteHistory(searchHistory.getId()+"");


        String id=searchHistory.getTrackId();
        String name=searchHistory.getName();

        if (flag) {
            Snackbar.make(activity.getWindow().getDecorView().findViewById(android.R.id.content), id + "-" + name + " Deleted ", Snackbar.LENGTH_SHORT).setAction("UNDO", null).show();
            //
            // Toast.makeText(activity,"delete"+position,Toast.LENGTH_LONG).show();
            filterdeSearchHistories.remove(position);
            notifyDataSetChanged();

        }
    }
    private void rateBookmark(SearchHistory searchHistory){
        SQLiteDBHandler handler=new SQLiteDBHandler(activity);
        Snackbar.make(activity.getWindow().getDecorView().findViewById(android.R.id.content),"Coming Soon..",Snackbar.LENGTH_SHORT).show();

    }
    private void shareBookmark(SearchHistory searchHistory){
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Track this AWB No.: "+searchHistory.getTrackId()+" of "+searchHistory.getName()+" on trackAll!\nInstall TrackAll http://bit.ly/1R30Vtu";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "TrackAll");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            activity.startActivity(Intent.createChooser(sharingIntent, "Share via"));


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
}
