package in.kuari.trackall.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import java.util.List;

import in.kuari.trackall.R;
import in.kuari.trackall.activities.ShowResultActivity;
import in.kuari.trackall.bean.BookMark;
import in.kuari.trackall.bean.FlightBean;
import in.kuari.trackall.databases.SQLiteDBHandler;
import in.kuari.trackall.utils.FunctionTools;


public class FlightsAdapter extends RecyclerView.Adapter<FlightsAdapter.ViewHolder> {

    private static final String TAG = "FlightsAdapter";
    private final List<FlightBean> flights;
    private final Context context;
    private String pnr;
    private FirebaseAnalytics mFirebaseAnalytics;

    public FlightsAdapter(List<FlightBean> flights, Context context) {
        this.flights = flights;
        this.context =context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_ecommerce, parent, false);
        analytics("onCreateViewHolder");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final FlightBean flight=flights.get(position);

       // holder.mView.setBackgroundColor(FunctionTools.getRandomColor());
        holder.flightName.setText(flight.getFlightName());
        Picasso.with(context).load(flight.getFlightLogo()).into(holder.flightLogo);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!FunctionTools.isConnected(context)) {
                    Snackbar.make(v, "No Internet Connection", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }else

                    loadFlightWeb(flight);
            }
        });
    }

    @Override
    public int getItemCount() {
        return flights.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView flightName;
        private View view;
        private ImageView flightLogo;
        public ViewHolder(View itemView) {
            super(itemView);
            flightName= (TextView) itemView.findViewById(R.id.ec_name);
            flightLogo = (ImageView)itemView.findViewById(R.id.ec_logo);
            this.view=itemView;


        }


    }
    void loadFlightWeb(FlightBean flight){
        analytics("loadFlightWeb");
        String pnrTrain="";
        //Toast.makeText(context,flight.getFlightName(),Toast.LENGTH_LONG).show();
        if(flight.getFlightID()==1){
            getPNR(flight);
        }else {

            Intent intent = new Intent(context, ShowResultActivity.class);
            intent.putExtra("webURL", flight.getFlightWebsite() + pnrTrain);
            intent.putExtra("flightName", flight.getFlightName());
            intent.putExtra("comingFrom", 2);

            //Toast.makeText(context,flight.getFlightName()+pnrTrain, Toast.LENGTH_LONG).show();

            context.startActivity(intent);
        }
    }
    private void getPNR(final FlightBean flight){
        Activity activity= (Activity) context;
        final EditText inputPnr=new EditText(activity);
        inputPnr.setHint("10-digit pnr");
        inputPnr.setMaxEms(10);
        inputPnr.setInputType(InputType.TYPE_CLASS_PHONE);
        inputPnr.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});


        final Intent intent=new Intent(context, ShowResultActivity.class);
        intent.putExtra("flightName",flight.getFlightName());
        intent.putExtra("comingFrom",2);

        final AlertDialog alertDialog=new AlertDialog.Builder(activity)
                .setMessage("Enter PNR no.")
                .setView(inputPnr)
                .setPositiveButton("Go", null)
                .setNegativeButton("Cancel",null)
                .create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
Button posBtn=alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                posBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pnr=inputPnr.getText().toString();
                        if(validatePnr(pnr)) {
                            alertDialog.dismiss();
                            intent.putExtra("webURL", flight.getFlightWebsite() + pnr);
                            bookMarkPNR(flight,pnr);
                            context.startActivity(intent);
                        }else inputPnr.setError("Please Enter valid pnr");
                    }
                });
            }
        });


alertDialog.show();
        //Toast.makeText(context,flight.getFlightName()+pnrTrain, Toast.LENGTH_LONG).show();
analytics("getPNR");
    }
    private boolean validatePnr(String PNR){


        String regex="^[0-9]{10}$";
        Log.d("valid",PNR.matches(regex)+"");
       return PNR.matches(regex);
    }
    private void bookMarkPNR(FlightBean flight, String trackID) {
        SQLiteDBHandler handler = new SQLiteDBHandler(context);
        BookMark bookMark = new BookMark();
        bookMark.setName(flight.getFlightName());
        bookMark.setTrackId(trackID);
        bookMark.setCourierID("1");
        bookMark.setbType(2);
        handler.addSearch(bookMark);

    }
    void popup(final FlightBean flight, final String trackingID) {
        new android.support.v7.app.AlertDialog.Builder(context)
                .setTitle("No Internet Connection")
                .setMessage("BookMark -" + trackingID.toUpperCase() + " with " + flight.getFlightName())
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        bookMarkPNR(flight, trackingID);
                    }
                }).setNegativeButton("No", null)
                .show();
    }
    private void analytics(String from){

        mFirebaseAnalytics= FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        bundle.putString(TAG,from);

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}
