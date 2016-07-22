package in.kuari.trackall.adapter;


import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import in.kuari.trackall.R;
import in.kuari.trackall.activities.ShowResultActivity;
import in.kuari.trackall.bean.BookMark;
import in.kuari.trackall.bean.CourierBean;
import in.kuari.trackall.databases.SQLiteDBHandler;
import in.kuari.trackall.utils.FunctionTools;
import in.kuari.trackall.utils.ReadData;

import static in.kuari.trackall.R.drawable.ic_flight;
import static in.kuari.trackall.R.drawable.ic_guest;
/**
 * Created by sultan_mirza on 1/18/16.
 */
public class CourierListAdapter extends RecyclerView.Adapter<CourierListAdapter.CourierViewHolder> {
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
    void populatelists(List<CourierBean> couriers1) {
        ReadData readData = new ReadData(activity);

        couriers = new ArrayList<>();
        couriers.addAll(couriers1);
        filteredCouriers = new ArrayList<>();
        filteredCouriers.addAll(couriers1);
        /*for (CourierBean bean:couriers)
        Log.d("cc",bean.toString());*/

    }

    public CourierListAdapter(Activity activity, EditText trackingID, List<CourierBean> couriers) {

        this.activity = activity;
        this.trackingID = trackingID;
        populatelists(couriers);

    }

    @Override
    public CourierViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.courier_row, parent, false);
        CourierListAdapter.CourierViewHolder vh = new CourierViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final CourierViewHolder holder, int position) {
        final CourierBean courier = filteredCouriers.get(position);

        holder.courierName.setText(courier.getCourierName());
        holder.courierEmail.setText(courier.getCourierEmail());
        holder.courierContact.setText(courier.getCourierContact());


        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
showHidePanel(holder);
            }
        });
        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCourier(courier.getCourierContact());
            }
        });
        holder.emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMail(courier.getCourierEmail());
            }
        });



        SharedPreferences pref = activity.getSharedPreferences("TRACKALL", Context.MODE_PRIVATE);
        boolean loadLogo = pref.getBoolean("LoadLogo", true);
        boolean ff = pref.getBoolean("FirstTime", true);
        //Log.d("logo",loadLogo+" "+ff);
        if (loadLogo)
            Picasso.with(activity).load(courier.getCourierImagePath()).placeholder(R.drawable.ic_menu_courier).error(R.drawable.ic_menu_courier).into(holder.courierLogo);

        holder.courierName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(trackingID.length()==0){
                    trackingID.setError("Enter Valid id");
                }else {
                    if (!FunctionTools.isConnected(activity)) {
                        Snackbar.make(v, "No Internet Connection", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        String trck = trackingID.getText().toString();
                        if (trck.length() > 0)
                            popup(courier, trck);
                    } else {
                        if (trackingID != null)
                            CourierSelected(courier);

                    }
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return filteredCouriers.size();
    }


    public void filter(String courierName) {
        filteredCouriers.clear();
        int i = 0;

        if (courierName.length() == 0)
            filteredCouriers.addAll(couriers);
        else {
            for (CourierBean s : couriers) {

                if ((s.getCourierName().toLowerCase()).contains(courierName)) {
                    filteredCouriers.add(s);

                }
            }
        }

        notifyDataSetChanged();
    }


    void CourierSelected(CourierBean courier) {
        String trackID = trackingID.getText().toString();
        //Toast.makeText(activity,trackID+"hh",Toast.LENGTH_LONG).show();

        Log.d("trackId", trackID + "");
        if (trackID.length() > 0)
            bookMarkSave(courier, trackID);
        Intent intent = new Intent(activity, ShowResultActivity.class);
        intent.putExtra("trackId", trackID);
        intent.putExtra("comingFrom", 1);//1-courier,2-ecommerce,3-flights
        intent.putExtra("courierID", courier.getCourierID());

        ClipboardManager manager = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("TrackingID", trackID);
        manager.setPrimaryClip(clip);
        Toast.makeText(activity, trackID.toUpperCase() + " Copied to ClipBoard", Toast.LENGTH_SHORT).show();
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
    private void bookMarkSave(CourierBean courier, String trackID) {
        SQLiteDBHandler handler = new SQLiteDBHandler(activity);
        BookMark bookMark = new BookMark();
        bookMark.setName(courier.getCourierName());
        bookMark.setTrackId(trackID);
        bookMark.setCourierID(courier.getCourierID() + "");
        bookMark.setbType(1);
        handler.addSearch(bookMark);

    }

    void popup(final CourierBean courierBean, final String trackingID) {
        new AlertDialog.Builder(activity)
                .setTitle("No Internet Connection")
                .setMessage("BookMark -" + trackingID.toUpperCase() + " with " + courierBean.getCourierName())
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        bookMarkSave(courierBean, trackingID);
                    }
                }).setNegativeButton("No", null)
                .show();
    }

    private void showHidePanel(CourierViewHolder viewHolder){
        if(viewHolder.hiddenLayout.getVisibility()==View.VISIBLE) {
            viewHolder.showHideButton.setImageDrawable(ContextCompat.getDrawable(activity, android.R.drawable.arrow_down_float));
            viewHolder.hiddenLayout.setVisibility(View.GONE);
        }else {
            viewHolder.showHideButton.setImageDrawable(ContextCompat.getDrawable(activity, android.R.drawable.arrow_up_float));
            viewHolder.hiddenLayout.setVisibility(View.VISIBLE);

        }
    }
    /**
     * call this method when user wants to send email to courier,open
     * @param toEmail
     */
    private void openMail(String toEmail){
        Intent intent = new Intent(Intent.ACTION_SENDTO);

        Uri uri=Uri.parse("mailto:"+toEmail);
        intent.setData(uri);
        activity.startActivity(intent);

    }

    /**
     * call this method when user clicks on dialer button in front of contact no.
     * @param number
     */


    private void callCourier(String number) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        activity.startActivity(intent);

}

    static class CourierViewHolder extends RecyclerView.ViewHolder{

        private TextView courierName;
        private View view;
        private  ImageView courierLogo;
        private RatingBar ratingCourier;
        private TextView courierContact;
        private TextView courierEmail;
        private ImageView callButton;
        private ImageView emailButton;
        private ImageView showHideButton;
        private LinearLayout hiddenLayout;

        public CourierViewHolder(View itemView) {
            super(itemView);
            courierName= (TextView) itemView.findViewById(R.id.courier_name);
            courierLogo = (ImageView)itemView.findViewById(R.id.courier_logo);
            ratingCourier= (RatingBar) itemView.findViewById(R.id.courier_rating);
            courierContact= (TextView) itemView.findViewById(R.id.courier_contact_no);
            courierEmail= (TextView) itemView.findViewById(R.id.courier_email);
            callButton= (ImageView) itemView.findViewById(R.id.courier_call);
            emailButton= (ImageView) itemView.findViewById(R.id.courier_mail_send);
            showHideButton= (ImageView) itemView.findViewById(R.id.show_hide_detail);

hiddenLayout= (LinearLayout) itemView.findViewById(R.id.hidden_panel);

            this.view=itemView;


        }

    }
}
