package in.kuari.trackall.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import in.kuari.trackall.R;
import in.kuari.trackall.activities.ShowResultActivity;
import in.kuari.trackall.bean.FlightBean;
import in.kuari.trackall.utils.FunctionTools;


public class TransportationAdapter extends RecyclerView.Adapter<TransportationAdapter.ViewHolder> {

    private final List<FlightBean> flights;
    private final Context context;
    private String pnr;

    public TransportationAdapter(List<FlightBean> flights, Context context) {
        this.flights = flights;
        this.context =context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_flights, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final FlightBean flight=flights.get(position);

        holder.mView.setBackgroundColor(FunctionTools.getRandomColor());
        holder.flightName.setText(flight.getFlightName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
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
        public final View mView;
        public final TextView flightName;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            flightName = (TextView) view.findViewById(R.id.flightname);
        }

    }
    void loadFlightWeb(FlightBean flight){
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
        inputPnr.setMaxEms(10);
        inputPnr.setInputType(InputType.TYPE_CLASS_PHONE);
        inputPnr.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});


        final Intent intent=new Intent(context, ShowResultActivity.class);
        intent.putExtra("flightName",flight.getFlightName());
        intent.putExtra("comingFrom",3);

        AlertDialog.Builder dialog=new AlertDialog.Builder(activity);
        dialog.setMessage("Enter PNR no.").setView(inputPnr).setPositiveButton("Go", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pnr=inputPnr.getText().toString();
                intent.putExtra("webURL",flight.getFlightWebsite()+pnr);
                context.startActivity(intent);


            }
        }).setNegativeButton("Cancel",null);

dialog.show();
        //Toast.makeText(context,flight.getFlightName()+pnrTrain, Toast.LENGTH_LONG).show();

    }
}
